package com.mocknidhi.route;

import com.mocknidhi.persistence.entity.Mock;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.model.rest.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by anilkumartalla on 2/8/17.
 */
@Service
public class RouteServiceImpl implements RouteService {


    private CamelContext camelContext;

    @Override
    public synchronized void deploy(List<Mock> mockList) {

        try {
            if (camelContext != null) {
                tearDown();
            }

            camelContext = new DefaultCamelContext();
            camelContext.addRoutes(createRouteBuilder(mockList));
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

    private RouteBuilder createRouteBuilder(final List<Mock> mockList) throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                restConfiguration().component("netty4-http").host("localhost").port(8085).bindingMode(RestBindingMode.auto)
                    .apiContextPath("/api-doc")
                        .apiProperty("cors", "true");

                Map<String, List<Mock>> mockMap = mapByContext(mockList);

                mockMap.entrySet().stream().forEach(kv -> {
                    RestDefinition restDefinition = rest(kv.getKey())
                            .consumes("application/json")
                            .produces("application/json");

                    verbs(kv.getValue(), restDefinition);

                });
            }
        };
    }

    private void verbs(final List<Mock> mockList, final RestDefinition restDefinition) {

        mockList.stream().forEach(item -> {

            restDefinition.verb(item.getVerb().toLowerCase(), item.getResourcePath()).route()
                    .process(exchange -> {
                        exchange.getIn().setBody(item.getContent().getText());
                    }).endRest();

        });
    }

    private static Map<String, List<Mock>> mapByContext(List<Mock> mockList) {
        return mockList.stream().collect(Collectors.groupingBy(Mock::getContextPath));
    }
}
