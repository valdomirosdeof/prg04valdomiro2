package br.com.ifba.infrastructure.exception;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler{
    @Value(value = "${server.error.include-exception}")
    private boolean printStackTrace;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ValidationExceptionDetails> handleMethodArgumentNotValid(
            MethodArgumentNotValidException methodArgumentNotValidException
    ) {
        List<FieldError> fieldErrors = methodArgumentNotValidException.getFieldErrors();

        String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(", "));
        String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                ValidationExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("Bad Request Exception, Campos Fields")
                        .details("Cheque os campo(s) com erros")
                        .developerMessage(methodArgumentNotValidException.getClass().getName())
                        .fields(fields)
                        .fieldsMessage(fieldsMessage)
                        .build(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<BusinessExceptionDetails> handleBusinessException(BusinessException ex) {
        return new ResponseEntity<>(
                BusinessExceptionDetails.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.NOT_FOUND.value())
                        .title("Recurso não encontrado")
                        .details(ex.getMessage())
                        .developerMessage(ex.getClass().getName())
                        .build(),
                HttpStatus.NOT_FOUND);
    }
    /*@ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleBusinessException(final BusinessException businessException, final WebRequest request){
        final String mensagemErro = businessException.getMessage();

        logger.error(mensagemErro, businessException);

        return construirMensagemDeErro(businessException, mensagemErro, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Object> construirMensagemDeErro(final Exception exception, final String message, final HttpStatus httpStatus, final WebRequest request){
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), message);

        if(this.printStackTrace){
            errorResponse.setStacktrace(ExceptionUtils.getStackTrace(exception));
        }

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }*/
}