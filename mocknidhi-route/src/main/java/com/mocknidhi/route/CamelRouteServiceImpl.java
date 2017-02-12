package com.mocknidhi.route;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mocknidhi.persistence.entity.ApiDefinition;
import com.mocknidhi.persistence.entity.OperationDefinition;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.rest.*;
import org.apache.commons.lang3.text.WordUtils;
import org.springframework.stereotype.Service;

import javax.ws.rs.HttpMethod;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by anilkumartalla on 2/8/17.
 */
@Service
public class CamelRouteServiceImpl implements RouteService {


    private CamelContext camelContext;

    @Override
    public synchronized void deploy(List<ApiDefinition> apiDefinitions) {

        try {
            if (camelContext != null) {
                tearDown();
            }

            camelContext = new DefaultCamelContext();
            camelContext.addRoutes(createRouteBuilder(apiDefinitions));
            camelContext.start();

        } catch (Exception e) {
            throw new RouteException("Exception occurred while deploying routes", e);
        }

    }

    private void tearDown() throws Exception {
        camelContext.suspend();
        camelContext.stop();
        camelContext = null;
    }

    @Override
    public synchronized void undeploy() {

        try {
            if (camelContext != null) {
                camelContext.stop();
            }
        } catch (Exception e) {
            throw new RouteException("Exception occurred while undeploying routes", e);
        }

    }

    private RouteBuilder createRouteBuilder(final List<ApiDefinition> apiDefinitions) throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                apiDefinitions.parallelStream().forEach(apiDefinition -> {
                    restConfiguration()
                            .component("netty4-http")
                            .host("localhost")
                            .port(apiDefinition.getPort())
                            .bindingMode(RestBindingMode.auto)
                            .apiContextPath("/api-doc")
                            .dataFormatProperty("prettyPrint", Boolean.TRUE.toString())
                            .apiProperty("cors", "true")
                            .apiProperty("api.title", apiDefinition.getName())
                            .apiProperty("api.version", apiDefinition.getVersion());


                    RestDefinition restDefinition = rest(apiDefinition.getContextPath())
                            .consumes("application/json")
                            .produces("application/json");

                    verbs(apiDefinition.getOperationDefinitions(), restDefinition);
                });

            }
        };
    }

    private void verbs(final List<OperationDefinition> operationDefinitions, final RestDefinition restDefinition) {

        operationDefinitions.stream().forEach(operationDefinition -> {

            addVerb(restDefinition, operationDefinition);

        });
    }

    private RestDefinition addVerb(RestDefinition restDefinition, OperationDefinition operationDefinition) {
        return restDefinition.verb(operationDefinition.getVerb().toUpperCase(), operationDefinition.getResourcePath())
                .description(operationDefinition.getDescription())
                .params(operationDefinition.getRequest()
                        .getQueryParams()
                        .stream()
                        .map(requestParam -> new RestOperationParamDefinition()
                                .name(requestParam.getName())
                                .allowableValues(requestParam.getValue()))
                        .collect(Collectors.toList()))
                //.outType(outType(operationDefinition.getResponse().getContent().getText(), operationDefinition.getDisplayName()))
                .route()
                .process(exchange -> {
                    exchange.getIn().setBody(operationDefinition.getResponse().getContent().getText());
                }).endRest();
    }

   /* private Class<?> outType(String json, String displayName) {
        Map<String, String> map = (Map<String, String>) new Gson().fromJson(json, Map.class);
        DynamicType.Builder byteBuddy = new ByteBuddy()
                .subclass(Object.class)
                .name(WordUtils.capitalize(displayName).replaceAll(" ", ""));

        map.entrySet().stream().forEach(kv -> byteBuddy.defineField(kv.getKey(), String.class));

        return byteBuddy.make().load(getClass().getClassLoader()).getLoaded();
    }*/

}
