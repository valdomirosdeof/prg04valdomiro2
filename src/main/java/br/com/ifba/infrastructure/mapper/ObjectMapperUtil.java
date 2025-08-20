package br.com.ifba.infrastructure.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ObjectMapperUtil {
    private static final ModelMapper MODEL_MAPPER;

    static {
        MODEL_MAPPER = new ModelMapper();
    }

    public <Input, Output> Output map(final Input object, final Class<Output> clazz) {
        MODEL_MAPPER.getConfiguration().setAmbiguityIgnored(true).setMatchingStrategy(MatchingStrategies.STRICT).setFieldMatchingEnabled(true).setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        Output c = MODEL_MAPPER.map(object, clazz);

        return c;
    }

    public <Input, Output> List<Output> mapAll(final List<Input> objects, final Class<Output> clazz) {
        MODEL_MAPPER.getConfiguration().setAmbiguityIgnored(true).setMatchingStrategy(MatchingStrategies.STRICT).setFieldMatchingEnabled(true).setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        return objects.stream()
                .map(object -> MODEL_MAPPER.map(object, clazz))
                .collect(Collectors.toList());
    }
}