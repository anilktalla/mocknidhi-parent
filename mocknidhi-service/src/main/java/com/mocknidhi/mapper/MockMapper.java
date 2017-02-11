package com.mocknidhi.mapper;

import com.mocknidhi.protocol.mock.model.Mock;
import com.mocknidhi.protocol.mock.model.MockHeaders;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by anilkumartalla on 2/5/17.
 */
@Component
public class MockMapper implements OrikaMapperFactoryConfigurer {

    @Override
    public void configure(MapperFactory mapperFactory) {
        mapperFactory.classMap(Mock.class, com.mocknidhi.persistence.entity.Mock.class)
                .customize(new MockBidirectionalCustomMapper())
                .exclude("headers")
                .exclude("id")
                .byDefault()
                .register();
    }

    public static class MockBidirectionalCustomMapper extends CustomMapper<Mock, com.mocknidhi.persistence.entity.Mock> {

        @Override
        public void mapAtoB(Mock dto, com.mocknidhi.persistence.entity.Mock entity, MappingContext context) {
            super.mapAtoB(dto, entity, context);

            if (entity.getId() == null) {
                entity.setId(UUID.randomUUID());
            }

            entity.setHeaders(dto.getHeaders().stream()
                    .collect(Collectors
                            .toMap(x -> x.getName(), x -> x.getValue(), (k, v) -> k)));

        }

        @Override
        public void mapBtoA(com.mocknidhi.persistence.entity.Mock entity, Mock dto, MappingContext context) {
            super.mapBtoA(entity, dto, context);

            dto.setId(entity.getId().toString());
            dto.setHeaders(entity.getHeaders().entrySet()
                    .stream()
                    .map(x -> mockHeaders(x))
                    .collect(Collectors
                            .toList()));
        }

        private static MockHeaders mockHeaders(Map.Entry<String, String> kv) {
            MockHeaders headers = new MockHeaders();
            headers.setName(kv.getKey());
            headers.setValue(kv.getValue());
            return headers;
        }
    }
}
