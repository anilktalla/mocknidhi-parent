package com.mocknidhi.mapper;

import com.mocknidhi.persistence.entity.ApiDefinition;
import com.mocknidhi.protocol.mock.model.OperationDefinition;
import ma.glasnost.orika.Converter;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
import org.springframework.stereotype.Component;

import java.util.UUID;


/**
 * Created by anilkumartalla on 2/5/17.
 */
@Component
public class MocknidhiMapper implements OrikaMapperFactoryConfigurer {

    @Override
    public void configure(MapperFactory mapperFactory) {
        mapperFactory.classMap(ApiDefinition.class, com.mocknidhi.persistence.entity.ApiDefinition.class)
                .byDefault()
                .register();

        mapperFactory.classMap(OperationDefinition.class, com.mocknidhi.persistence.entity.OperationDefinition.class)
                .byDefault()
                .register();

        mapperFactory.getConverterFactory().registerConverter(new UUIDToStringBidirectionalConverter());
    }

    private static class UUIDToStringBidirectionalConverter extends BidirectionalConverter<UUID, String> {

        @Override
        public String convertTo(UUID source, Type<String> destinationType, MappingContext mappingContext) {
            return source.toString();
        }

        @Override
        public UUID convertFrom(String source, Type<UUID> destinationType, MappingContext mappingContext) {
            return UUID.fromString(source);
        }
    }


}
