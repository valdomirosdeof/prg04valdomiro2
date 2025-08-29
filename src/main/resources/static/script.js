/* script.js - CRUD unificado + formulários (listagens + cadastro/edição)
   API_BASE = '' se backend no mesmo host/porta; caso contrário, ex: 'http://localhost:8080'
*/
const API_BASE = ''; // ajuste se necessário

/* ---------- Helpers ---------- */
function normalizeSearchText(text = '') {
  return text.toString().normalize('NFD').replace(/[\u0300-\u036f]/g, '').toLowerCase();
}

async function apiFetch(url, options = {}) {
  const defaultHeaders = { 'Accept': 'application/json', 'Content-Type': 'application/json' };
  options.headers = Object.assign({}, defaultHeaders, options.headers || {});
  const resp = await fetch(API_BASE + url, options);
  const contentType = resp.headers.get('content-type') || '';

  if (resp.ok) return contentType.includes('application/json') ? resp.json() : null;

  // parse body
  let body = null;
  try {
    body = contentType.includes('application/json') ? await resp.json() : await resp.text();
  } catch (e) {
    try { body = await resp.text(); } catch (_) { body = null; }
  }

  const err = { status: resp.status, raw: body };
  if (body) {
    if (body.fieldsMessage || body.fields) {
      err.type = 'validation';
      err.fields = body.fields ? body.fields.split(',').map(f => f.trim()) : [];
      err.message = body.fieldsMessage || body.details || JSON.stringify(body);
    } else if (body.title || body.details) {
      err.type = 'business';
      err.message = body.details || body.title || JSON.stringify(body);
    } else if (body.message) {
      err.type = 'error';
      err.message = body.message;
    } else {
      err.type = 'unknown';
      err.message = JSON.stringify(body);
    }
  } else {
    err.type = 'http';
    err.message = resp.statusText || 'Erro na requisição';
  }
  throw err;
}

function showFormErrors(containerSelector, error) {
  const container = document.querySelector(containerSelector);
  if (!container) return;
  container.innerHTML = '';
  if (!error) return;
  const msg = error.message || 'Erro desconhecido';
  container.innerHTML = `<div>${escapeHtml(msg)}</div>`;
}

function escapeHtml(str = '') {
  return String(str).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
}

function createActionButtons(editHandler, deleteHandler) {
  const wrapper = document.createElement('div');
  wrapper.style.display = 'flex';
  wrapper.style.gap = '8px';

  const btnEdit = document.createElement('button');
  btnEdit.className = 'botao-editar';
  btnEdit.type = 'button';
  btnEdit.textContent = 'EDITAR';
  btnEdit.addEventListener('click', editHandler);
  wrapper.appendChild(btnEdit);

  const btnDelete = document.createElement('button');
  btnDelete.className = 'botao-excluir';
  btnDelete.type = 'button';
  btnDelete.textContent = 'EXCLUIR';
  btnDelete.addEventListener('click', deleteHandler);
  wrapper.appendChild(btnDelete);

  return wrapper;
}

function debounce(fn, ms) {
  let t;
  return (...args) => {
    clearTimeout(t);
    t = setTimeout(() => fn(...args), ms);
  };
}

function getIdFromQuery() {
  const params = new URLSearchParams(window.location.search);
  return params.get('id') ? Number(params.get('id')) : null;
}

/* ---------- Generic CRUD Module for list pages ---------- */
function CRUDModule(config) {
  const { endpoints, selectors, redirectAdd, redirectEdit, fields } = config;

  async function loadAll() {
    try {
      const data = await apiFetch(endpoints.list);
      renderTable(data || []);
    } catch (err) {
      console.error('Erro ao carregar dados', err);
      // opcional: mostrar mensagem visual na página
    }
  }

  function renderTable(items = []) {
    const tbody = document.querySelector(selectors.tbody);
    if (!tbody) return;
    tbody.innerHTML = '';

    items.forEach(item => {
      const tr = document.createElement('tr');

      fields.forEach(f => {
        const td = document.createElement('td');
        td.className = 'celula-dados';
        try {
          td.textContent = typeof f.value === 'function' ? (f.value(item) ?? '') : (item[f.key] ?? '');
        } catch (e) {
          td.textContent = '';
        }
        tr.appendChild(td);
      });

      const tdActions = document.createElement('td');
      tdActions.className = 'celula-acoes';
      tdActions.appendChild(createActionButtons(
        () => { window.location.href = redirectEdit + '?id=' + item.id; },
        () => confirmAndDelete(item.id)
      ));
      tr.appendChild(tdActions);

      tbody.appendChild(tr);
    });
  }

  async function confirmAndDelete(id) {
    if (!confirm('Deseja realmente excluir este registro?')) return;
    try {
      // endpoints.delete pode ser string ou função
      const deleteUrl = typeof endpoints.delete === 'function' ? endpoints.delete(id) : endpoints.delete;
      await apiFetch(deleteUrl, { method: 'DELETE' });
      await loadAll();
    } catch (err) {
      alert(err.message || 'Erro ao excluir registro');
    }
  }

  async function searchHandler() {
    const input = document.querySelector(selectors.search);
    const q = normalizeSearchText(input ? input.value : '');
    try {
      const data = await apiFetch(endpoints.list);
      const filtered = (data || []).filter(item => {
        return fields.some(f => {
          const raw = typeof f.value === 'function' ? (f.value(item) ?? '') : (item[f.key] ?? '');
          return normalizeSearchText(String(raw)).includes(q);
        });
      });
      renderTable(filtered);
    } catch (err) {
      console.error('Erro na busca', err);
    }
  }

  function init() {
    const tbody = document.querySelector(selectors.tbody);
    if (!tbody) return;

    const searchInput = document.querySelector(selectors.search);
    if (searchInput) searchInput.addEventListener('input', debounce(searchHandler, 250));

    const btnAdd = document.querySelector(selectors.addButton);
    if (btnAdd) btnAdd.addEventListener('click', () => window.location.href = redirectAdd);

    loadAll();
  }

  return { init, loadAll };
}

/* ---------- CRUD modules (list pages) ---------- */
const usuariosModule = CRUDModule({
  endpoints: { list: '/usuarios/findall', delete: (id) => `/usuarios/delete/${id}` },
  selectors: { tbody: '#usuarios-tbody', search: '#usuarios-search', addButton: '#usuarios-add' },
  redirectAdd: 'cadastro-usuario.html',
  redirectEdit: 'edicao-usuario.html',
  fields: [
    { key: 'id' },
    { key: 'nome' },
    { key: 'login' },
    { key: 'email' },
    { key: 'perfisDeUsuario', value: item => (item.perfisDeUsuario || []).map(p => p.nivelDeAcesso || p.nivelAcesso || '').join(', ') },
    { key: 'pessoa', value: item => item.pessoa?.nome || '' }
  ]
});

const perfisModule = CRUDModule({
  endpoints: { list: '/perfis-de-usuario/findall', delete: (id) => `/perfis-de-usuario/delete/${id}` },
  selectors: { tbody: '#perfis-tbody', search: '#perfis-search', addButton: '#perfis-add' },
  redirectAdd: 'cadastro-perfil-usuario.html',
  redirectEdit: 'edicao-perfil-usuario.html',
  fields: [
    { key: 'id' },
    { key: 'nivelDeAcesso' },
    { key: 'permissoes' },
    { key: 'descricaoDoPerfil' },
    { key: 'usuario', value: item => item.usuario?.login || item.usuario?.nome || '' }
  ]
});

const pessoasModule = CRUDModule({
  endpoints: { list: '/pessoas/findall', delete: (id) => `/pessoas/delete/${id}` },
  selectors: { tbody: '#pessoas-tbody', search: '#pessoas-search', addButton: '#pessoas-add' },
  redirectAdd: 'cadastro-pessoa.html',
  redirectEdit: 'edicao-pessoa.html',
  fields: [{ key: 'id' }, { key: 'nome' }, { key: 'telefone' }, { key: 'endereco' }]
});

const clientesModule = CRUDModule({
  endpoints: { list: '/clientes/findall', delete: (id) => `/clientes/delete/${id}` },
  selectors: { tbody: '#clientes-tbody', search: '#clientes-search', addButton: '#clientes-add' },
  redirectAdd: 'cadastro-cliente.html',
  redirectEdit: 'edicao-cliente.html',
  fields: [
    { key: 'id' }, { key: 'nome' }, { key: 'telefone' }, { key: 'endereco' }, { key: 'cpf' },
    { key: 'historicoDeCompras' },
    { key: 'limiteDeCredito', value: item => item.limiteDeCredito != null ? item.limiteDeCredito : '' }
  ]
});

const fornecedoresModule = CRUDModule({
  endpoints: { list: '/fornecedores/findall', delete: (id) => `/fornecedores/delete/${id}` },
  selectors: { tbody: '#fornecedores-tbody', search: '#fornecedores-search', addButton: '#fornecedores-add' },
  redirectAdd: 'cadastro-fornecedor.html',
  redirectEdit: 'edicao-fornecedor.html',
  fields: [
    { key: 'id' }, { key: 'nome' }, { key: 'telefone' }, { key: 'endereco' },
    { key: 'cnpj' }, { key: 'listaDeProdutos' },
    { key: 'prazoDeEntrega', value: item => item.prazoDeEntrega != null ? item.prazoDeEntrega : '' }
  ]
});

/* ---------- Generic Form Module for cadastro/edição pages ---------- */
function FormModule(config) {
  const { endpoints, selectors, redirectBack, mapping, populateFuncs = [] } = config;

  async function initForm() {
    const form = document.querySelector(selectors.form);
    if (!form) return; // not this page

    // If populate functions present, run them (ex: fill selects)
    await Promise.all(populateFuncs.map(fn => fn()));

    const id = getIdFromQuery();
    if (id) {
      // editing: fetch item and populate
      try {
        const url = typeof endpoints.get === 'function' ? endpoints.get(id) : endpoints.get;
        const data = await apiFetch(url);
        populateFields(data);
        // scroll to form
        form.scrollIntoView({ behavior: 'smooth' });
      } catch (err) {
        showFormErrors(selectors.formErrors, err);
      }
    }

    form.addEventListener('submit', submitHandler);
  }

  function populateFields(data) {
    // mapping is array of objects: { field: 'dom selector' , from: 'dtoKey' , transform?: fn }
    mapping.forEach(m => {
      const el = document.querySelector(m.field);
      if (!el) return;
      const value = typeof m.from === 'function' ? m.from(data) : (data?.[m.from] ?? '');
      if (el.tagName === 'SELECT') {
        // select: if multiple, set selected options
        if (el.multiple && Array.isArray(value)) {
          const values = value.map(v => String(v));
          Array.from(el.options).forEach(o => o.selected = values.includes(String(o.value)));
        } else {
          el.value = value ?? '';
        }
      } else if (el.type === 'checkbox') {
        el.checked = !!value;
      } else {
        el.value = value ?? '';
      }
    });
  }

  async function submitHandler(e) {
    e.preventDefault();
    const id = getIdFromQuery();
    const payload = {};

    // build payload from mapping
    mapping.forEach(m => {
      const el = document.querySelector(m.field);
      if (!el) return;
      let v;
      if (el.tagName === 'SELECT') {
        if (el.multiple) {
          // multiple select -> array
          v = Array.from(el.selectedOptions).map(o => o.value).filter(x => x !== '');
          if (m.multipleAsObjects) {
            // e.g. perfisDeUsuario: [{id: 1}, {id:2}]
            v = v.map(val => ({ id: Number(val) }));
          } else {
            // plain array of values
            v = v.map(val => isNaN(val) ? val : Number(val));
          }
        } else {
          v = el.value !== '' ? el.value : null;
          if (m.asNumber && v != null) v = Number(v);
        }
      } else if (el.type === 'number') {
        v = el.value !== '' ? Number(el.value) : null;
      } else {
        v = el.value;
      }

      if (m.to) {
        // custom destination key or function
        if (typeof m.to === 'function') m.to(payload, v);
        else payload[m.to] = v;
      } else {
        payload[m.from] = v;
      }
    });

    // special-case: some DTOs require fields / conversions (ex.: usuario expects pessoa: {id:...})
    try {
      if (id) {
        // update: must include id
        payload.id = Number(id);

        // If backend requires senha non-null (UsuarioPutRequestDto has senha @NotNull), ensure not empty:
        // If form contains a senha field, we must ensure it's provided on edit (cannot infer old senha from GET)
        const senhaEl = document.querySelector(selectors.passwordField);
        if (senhaEl) {
          const senhaVal = senhaEl.value || '';
          if (!senhaVal) {
            throw { message: 'A senha é obrigatória no momento da edição. Informe uma nova senha.' };
          }
          // set senha in payload (mapping should include it normally)
        }

        const url = endpoints.update;
        await apiFetch(url, { method: 'PUT', body: JSON.stringify(payload) });
        alert('Registro atualizado com sucesso');
      } else {
        // create
        const url = endpoints.create;
        await apiFetch(url, { method: 'POST', body: JSON.stringify(payload) });
        alert('Registro criado com sucesso');
      }
      // redirect back to list
      window.location.href = redirectBack;
    } catch (err) {
      console.error('Erro no submit', err);
      showFormErrors(selectors.formErrors, err);
    }
  }

  return { initForm };
}

/* ---------- Form modules for each entity ---------- */

/* Usuário form (cadastro/edição)
   - selectors: form fields ids are same as in your HTML (ex.: #usuario-nome, #usuario-login, etc.)
   - needs to populate pessoa select and perfis select
*/
const UsuarioForm = (function() {
  const endpoints = {
    get: (id) => `/usuarios/findbyid/${id}`,
    create: '/usuarios/save',
    update: '/usuarios/update'
  };
  const selectors = {
    form: '#usuario-form',
    formErrors: '#usuario-form-errors',
    passwordField: '#usuario-senha'
  };

  const mapping = [
    { field: '#usuario-nome', from: 'nome' },
    { field: '#usuario-login', from: 'login' },
    { field: '#usuario-email', from: 'email' },
    { field: '#usuario-senha', from: 'senha' }, // must be present on create & update
    // pessoa (select) -> payload.pessoa = { id: Number(...) } or null
    { field: '#usuario-pessoa-id', from: 'pessoa', to: (payload, v) => {
        payload.pessoa = v ? { id: Number(v) } : null;
      }, asNumber: true
    },
    // perfis multi-select -> payload.perfisDeUsuario = [{id: N}, ...]
    { field: '#usuario-perfis-ids', from: 'perfisDeUsuario', to: 'perfisDeUsuario', multipleAsObjects: true }
  ];

  async function populatePessoaSelect() {
    const sel = document.querySelector('#usuario-pessoa-id');
    if (!sel) return;
    sel.innerHTML = '<option value="">-- Nenhuma --</option>';
    try {
      const pessoas = await apiFetch('/pessoas/findall');
      (pessoas || []).forEach(p => {
        const opt = document.createElement('option');
        opt.value = p.id ?? '';
        opt.textContent = `${p.nome} (${p.telefone || ''})`;
        sel.appendChild(opt);
      });
    } catch (e) {
      console.warn('Erro ao popular pessoas select', e);
    }
  }

  async function populatePerfisSelect() {
    const sel = document.querySelector('#usuario-perfis-ids');
    if (!sel) return;
    sel.innerHTML = ''; // multiple
    try {
      const perfis = await apiFetch('/perfis-de-usuario/findall');
      (perfis || []).forEach(p => {
        const opt = document.createElement('option');
        opt.value = p.id ?? '';
        opt.textContent = p.nivelDeAcesso || p.nivelDeAcesso;
        sel.appendChild(opt);
      });
    } catch (e) {
      console.warn('Erro ao popular perfis select', e);
    }
  }

  const formModule = FormModule({
    endpoints,
    selectors: { form: selectors.form, formErrors: selectors.formErrors, passwordField: selectors.passwordField },
    redirectBack: 'usuarios.html',
    mapping,
    populateFuncs: [populatePessoaSelect, populatePerfisSelect]
  });

  // But override populateFields to also select multiple perfis when editing
  async function init() {
    const form = document.querySelector('#usuario-form');
    if (!form) return;
    // run populate funcs first so selects have options
    await Promise.all([populatePessoaSelect(), populatePerfisSelect()]);

    const id = getIdFromQuery();
    if (id) {
      try {
        const usuario = await apiFetch(`/usuarios/findbyid/${id}`);
        // fill simple fields
        document.querySelector('#usuario-nome').value = usuario.nome || '';
        document.querySelector('#usuario-login').value = usuario.login || '';
        document.querySelector('#usuario-email').value = usuario.email || '';
        // senha: backend GET doesn't return senha, DTO requires senha on PUT -> leave empty and show hint
        document.querySelector('#usuario-senha').value = '';
        // pessoa
        if (usuario.pessoa && usuario.pessoa.id) {
          const s = document.querySelector('#usuario-pessoa-id');
          if (s) s.value = usuario.pessoa.id;
        }
        // perfis
        if (Array.isArray(usuario.perfisDeUsuario) && usuario.perfisDeUsuario.length) {
          const perfisIds = usuario.perfisDeUsuario.map(p => String(p.id));
          const perfisSelect = document.querySelector('#usuario-perfis-ids');
          if (perfisSelect) {
            Array.from(perfisSelect.options).forEach(o => {
              o.selected = perfisIds.includes(String(o.value));
            });
          }
        }
      } catch (err) {
        showFormErrors('#usuario-form-errors', err);
      }
    }

    form.addEventListener('submit', async (e) => {
      e.preventDefault();
      const idLocal = getIdFromQuery();
      // build payload similar to FormModule mapping
      const nome = document.querySelector('#usuario-nome').value;
      const login = document.querySelector('#usuario-login').value;
      const email = document.querySelector('#usuario-email').value;
      const senha = document.querySelector('#usuario-senha').value;
      const pessoaId = document.querySelector('#usuario-pessoa-id') ? document.querySelector('#usuario-pessoa-id').value : null;

      const perfisSelect = document.querySelector('#usuario-perfis-ids');
      const selectedPerfis = [];
      if (perfisSelect) {
        Array.from(perfisSelect.selectedOptions).forEach(opt => {
          if (opt.value) selectedPerfis.push({ id: Number(opt.value) });
        });
      }

      const payload = {
        nome, login, email, senha,
        pessoa: pessoaId ? { id: Number(pessoaId) } : null,
        perfisDeUsuario: selectedPerfis
      };

      try {
        if (idLocal) {
          payload.id = Number(idLocal);
          // senha required by backend - ensure present
          if (!payload.senha) throw { message: 'A senha é obrigatória ao atualizar o usuário. Informe uma nova senha.' };
          await apiFetch('/usuarios/update', { method: 'PUT', body: JSON.stringify(payload) });
          alert('Usuário atualizado com sucesso');
        } else {
          await apiFetch('/usuarios/save', { method: 'POST', body: JSON.stringify(payload) });
          alert('Usuário criado com sucesso');
        }
        window.location.href = 'usuarios.html';
      } catch (err) {
        console.error(err);
        showFormErrors('#usuario-form-errors', err);
      }
    });
  }

  return { init };
})();

/* Perfil form */
const PerfilForm = (function() {
  const endpoints = {
    get: (id) => `/perfis-de-usuario/findbyid/${id}`,
    create: '/perfis-de-usuario/save',
    update: '/perfis-de-usuario/update'
  };
  const selectors = { form: '#perfil-form', formErrors: '#perfil-form-errors' };

  async function populateUsuarioSelect() {
    const sel = document.querySelector('#perfil-usuario-id');
    if (!sel) return;
    sel.innerHTML = '<option value="">-- selecione --</option>';
    try {
      const users = await apiFetch('/usuarios/findall');
      (users || []).forEach(u => {
        const opt = document.createElement('option');
        opt.value = u.id ?? '';
        opt.textContent = `${u.nome} (${u.login})`;
        sel.appendChild(opt);
      });
    } catch (e) {
      console.warn('Erro ao popular usuario select', e);
    }
  }

  const mapping = [
    { field: '#perfil-nivel', from: 'nivelDeAcesso' },
    { field: '#perfil-permissoes', from: 'permissoes' },
    { field: '#perfil-descricao', from: 'descricaoDoPerfil' },
    { field: '#perfil-usuario-id', from: 'usuarioId', to: (payload, v) => { payload.usuarioId = v ? Number(v) : null; }, asNumber: true }
  ];

  const fm = FormModule({
    endpoints,
    selectors: { form: selectors.form, formErrors: selectors.formErrors },
    redirectBack: 'perfis-usuario.html',
    mapping,
    populateFuncs: [populateUsuarioSelect]
  });

  // small override to support initial GET population (select must be filled first)
  async function init() {
    const form = document.querySelector('#perfil-form');
    if (!form) return;
    await populateUsuarioSelect();
    const id = getIdFromQuery();
    if (id) {
      try {
        const p = await apiFetch(`/perfis-de-usuario/findbyid/${id}`);
        document.querySelector('#perfil-id').value = p.id ?? '';
        document.querySelector('#perfil-nivel').value = p.nivelDeAcesso ?? '';
        document.querySelector('#perfil-permissoes').value = p.permissoes ?? '';
        document.querySelector('#perfil-descricao').value = p.descricaoDoPerfil ?? '';
        if (p.usuario && p.usuario.id) document.querySelector('#perfil-usuario-id').value = p.usuario.id;
      } catch (err) {
        showFormErrors('#perfil-form-errors', err);
      }
    }

    form.addEventListener('submit', async (e) => {
      e.preventDefault();
      const idLocal = getIdFromQuery();
      const nivel = document.querySelector('#perfil-nivel').value;
      const permissoes = document.querySelector('#perfil-permissoes').value;
      const descricao = document.querySelector('#perfil-descricao').value;
      const usuarioId = document.querySelector('#perfil-usuario-id').value || null;

      const payload = {
        nivelDeAcesso: nivel,
        permissoes,
        descricaoDoPerfil: descricao,
        usuarioId: usuarioId ? Number(usuarioId) : null
      };

      try {
        if (idLocal) {
          payload.id = Number(idLocal);
          await apiFetch('/perfis-de-usuario/update', { method: 'PUT', body: JSON.stringify(payload) });
          alert('Perfil atualizado com sucesso');
        } else {
          await apiFetch('/perfis-de-usuario/save', { method: 'POST', body: JSON.stringify(payload) });
          alert('Perfil criado com sucesso');
        }
        window.location.href = 'perfis-usuario.html';
      } catch (err) {
        console.error(err);
        showFormErrors('#perfil-form-errors', err);
      }
    });
  }

  return { init };
})();

/* Pessoa form */
const PessoaForm = (function() {
  const endpoints = { get: (id) => `/pessoas/findbyid/${id}`, create: '/pessoas/save', update: '/pessoas/update' };
  const selectors = { form: '#pessoa-form', formErrors: '#pessoa-form-errors' };

  async function init() {
    const form = document.querySelector('#pessoa-form');
    if (!form) return;
    const id = getIdFromQuery();
    if (id) {
      try {
        const p = await apiFetch(`/pessoas/findbyid/${id}`);
        document.querySelector('#pessoa-id').value = p.id ?? '';
        document.querySelector('#pessoa-nome').value = p.nome ?? '';
        document.querySelector('#pessoa-telefone').value = p.telefone ?? '';
        document.querySelector('#pessoa-endereco').value = p.endereco ?? '';
      } catch (err) {
        showFormErrors('#pessoa-form-errors', err);
      }
    }

    form.addEventListener('submit', async (e) => {
      e.preventDefault();
      const idLocal = getIdFromQuery();
      const nome = document.querySelector('#pessoa-nome').value;
      const telefone = document.querySelector('#pessoa-telefone').value;
      const endereco = document.querySelector('#pessoa-endereco').value;
      const payload = { nome, telefone, endereco };

      try {
        if (idLocal) {
          payload.id = Number(idLocal);
          await apiFetch('/pessoas/update', { method: 'PUT', body: JSON.stringify(payload) });
          alert('Pessoa atualizada com sucesso');
        } else {
          await apiFetch('/pessoas/save', { method: 'POST', body: JSON.stringify(payload) });
          alert('Pessoa criada com sucesso');
        }
        window.location.href = 'pessoas.html';
      } catch (err) {
        console.error(err);
        showFormErrors('#pessoa-form-errors', err);
      }
    });
  }

  return { init };
})();

/* Cliente form */
const ClienteForm = (function() {
  const endpoints = { get: (id) => `/clientes/findbyid/${id}`, create: '/clientes/save', update: '/clientes/update' };
  const selectors = { form: '#cliente-form', formErrors: '#cliente-form-errors' };

  async function init() {
    const form = document.querySelector('#cliente-form');
    if (!form) return;
    const id = getIdFromQuery();
    if (id) {
      try {
        const c = await apiFetch(`/clientes/findbyid/${id}`);
        document.querySelector('#cliente-id').value = c.id ?? '';
        document.querySelector('#cliente-nome').value = c.nome ?? '';
        document.querySelector('#cliente-telefone').value = c.telefone ?? '';
        document.querySelector('#cliente-endereco').value = c.endereco ?? '';
        document.querySelector('#cliente-cpf').value = c.cpf ?? '';
        document.querySelector('#cliente-historico').value = c.historicoDeCompras ?? '';
        document.querySelector('#cliente-limite').value = c.limiteDeCredito ?? '';
      } catch (err) {
        showFormErrors('#cliente-form-errors', err);
      }
    }

    form.addEventListener('submit', async (e) => {
      e.preventDefault();
      const idLocal = getIdFromQuery();
      const nome = document.querySelector('#cliente-nome').value;
      const telefone = document.querySelector('#cliente-telefone').value;
      const endereco = document.querySelector('#cliente-endereco').value;
      const cpf = document.querySelector('#cliente-cpf').value;
      const historico = document.querySelector('#cliente-historico').value;
      const limite = document.querySelector('#cliente-limite').value ? Number(document.querySelector('#cliente-limite').value) : null;

      const payload = { nome, telefone, endereco, cpf, historicoDeCompras: historico, limiteDeCredito: limite };

      try {
        if (idLocal) {
          payload.id = Number(idLocal);
          await apiFetch('/clientes/update', { method: 'PUT', body: JSON.stringify(payload) });
          alert('Cliente atualizado com sucesso');
        } else {
          await apiFetch('/clientes/save', { method: 'POST', body: JSON.stringify(payload) });
          alert('Cliente criado com sucesso');
        }
        window.location.href = 'clientes.html';
      } catch (err) {
        console.error(err);
        showFormErrors('#cliente-form-errors', err);
      }
    });
  }

  return { init };
})();

/* Fornecedor form */
const FornecedorForm = (function() {
  const endpoints = { get: (id) => `/fornecedores/findbyid/${id}`, create: '/fornecedores/save', update: '/fornecedores/update' };
  const selectors = { form: '#fornecedor-form', formErrors: '#fornecedor-form-errors' };

  async function init() {
    const form = document.querySelector('#fornecedor-form');
    if (!form) return;
    const id = getIdFromQuery();
    if (id) {
      try {
        const f = await apiFetch(`/fornecedores/findbyid/${id}`);
        document.querySelector('#fornecedor-id').value = f.id ?? '';
        document.querySelector('#fornecedor-nome').value = f.nome ?? '';
        document.querySelector('#fornecedor-telefone').value = f.telefone ?? '';
        document.querySelector('#fornecedor-endereco').value = f.endereco ?? '';
        document.querySelector('#fornecedor-cnpj').value = f.cnpj ?? '';
        document.querySelector('#fornecedor-lista-produtos').value = f.listaDeProdutos ?? '';
        document.querySelector('#fornecedor-prazo').value = f.prazoDeEntrega ?? '';
      } catch (err) {
        showFormErrors('#fornecedor-form-errors', err);
      }
    }

    form.addEventListener('submit', async (e) => {
      e.preventDefault();
      const idLocal = getIdFromQuery();
      const nome = document.querySelector('#fornecedor-nome').value;
      const telefone = document.querySelector('#fornecedor-telefone').value;
      const endereco = document.querySelector('#fornecedor-endereco').value;
      const cnpj = document.querySelector('#fornecedor-cnpj').value;
      const lista = document.querySelector('#fornecedor-lista-produtos').value;
      const prazo = document.querySelector('#fornecedor-prazo').value ? Number(document.querySelector('#fornecedor-prazo').value) : null;

      const payload = { nome, telefone, endereco, cnpj, listaDeProdutos: lista, prazoDeEntrega: prazo };

      try {
        if (idLocal) {
          payload.id = Number(idLocal);
          await apiFetch('/fornecedores/update', { method: 'PUT', body: JSON.stringify(payload) });
          alert('Fornecedor atualizado com sucesso');
        } else {
          await apiFetch('/fornecedores/save', { method: 'POST', body: JSON.stringify(payload) });
          alert('Fornecedor criado com sucesso');
        }
        window.location.href = 'fornecedores.html';
      } catch (err) {
        console.error(err);
        showFormErrors('#fornecedor-form-errors', err);
      }
    });
  }

  return { init };
})();

/* ---------- Init on DOM ready: init list modules and form modules (only those present) ---------- */
document.addEventListener('DOMContentLoaded', () => {
  // list pages
  try { usuariosModule.init(); } catch (e) {}
  try { perfisModule.init(); } catch (e) {}
  try { pessoasModule.init(); } catch (e) {}
  try { clientesModule.init(); } catch (e) {}
  try { fornecedoresModule.init(); } catch (e) {}

  // forms (cadastro / edição)
  try { UsuarioForm.init(); } catch (e) {}
  try { PerfilForm.init(); } catch (e) {}
  try { PessoaForm.init(); } catch (e) {}
  try { ClienteForm.init(); } catch (e) {}
  try { FornecedorForm.init(); } catch (e) {}
});