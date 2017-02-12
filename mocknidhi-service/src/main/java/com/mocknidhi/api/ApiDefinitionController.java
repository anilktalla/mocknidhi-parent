package com.mocknidhi.api;

import com.mocknidhi.persistence.repository.IApiDefinitionRepository;
import com.mocknidhi.protocol.mock.api.ApiDefinitionApi;
import com.mocknidhi.protocol.mock.model.ApiDefinition;
import com.mocknidhi.protocol.mock.model.OperationDefinition;
import com.mocknidhi.route.RouteService;
import io.swagger.annotations.ApiParam;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
public class ApiDefinitionController implements ApiDefinitionApi {

    private final IApiDefinitionRepository apiDefinitionRepository;
    private final MapperFacade mapperFacade;
    private RouteService routeService;

    @Autowired
    public ApiDefinitionController(final IApiDefinitionRepository apiDefinitionRepository, final MapperFacade mapperFacade, final RouteService routeService) {
        this.apiDefinitionRepository = apiDefinitionRepository;
        this.mapperFacade = mapperFacade;
        this.routeService = routeService;
    }

    @Override
    public ResponseEntity<ApiDefinition> createApiDefinition(@ApiParam(value = "Api definition to add", required = true) @RequestBody ApiDefinition apiDefinition) {

        apiDefinition.setId(UUID.randomUUID().toString());
        return new ResponseEntity<>(save(apiDefinition), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<OperationDefinition> createOperationDefinition(@ApiParam(value = "Api Definition identifier", required = true) @PathVariable("apiDefId") String apiDefId, @ApiParam(value = "Operation definition", required = true) @RequestBody OperationDefinition operationDefinition) {

        operationDefinition.setId(UUID.randomUUID().toString());
        return new ResponseEntity<>(save(UUID.fromString(apiDefId), operationDefinition), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteApiDefinition(@ApiParam(value = "Api definition identifier", required = true) @PathVariable("apiDefId") String apiDefId) {

        apiDefinitionRepository.delete(UUID.fromString(apiDefId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> deleteOperationEndpoint(@ApiParam(value = "Api Definition identifier", required = true) @PathVariable("apiDefId") String apiDefId, @ApiParam(value = "The Operation definition identifier", required = true) @PathVariable("operationDefId") String operationDefId) {
        com.mocknidhi.persistence.entity.ApiDefinition entity = apiDefinitionRepository.findOne(UUID.fromString(apiDefId));

        if (entity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        com.mocknidhi.persistence.entity.OperationDefinition operationDefEntity = operationDefById(entity, UUID.fromString(operationDefId));

        if (operationDefEntity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        entity.getOperationDefinitions().remove(operationDefEntity);
        apiDefinitionRepository.save(entity);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<Void> deploy() {
        Collection<com.mocknidhi.persistence.entity.ApiDefinition> apiDefinitions = apiDefinitionRepository.findAll();
        routeService.deploy(apiDefinitions.stream().collect(Collectors.toList()));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiDefinition> getApiDefinition(@ApiParam(value = "Api definition identifier", required = true) @PathVariable("apiDefId") String apiDefId) {

        com.mocknidhi.persistence.entity.ApiDefinition entity = apiDefinitionRepository.findOne(UUID.fromString(apiDefId));
        return new ResponseEntity<>(mapperFacade.map(entity, ApiDefinition.class), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ApiDefinition>> getApiDefinitions(@ApiParam(value = "Offset the list of returned results by this amount. Default is zero.") @RequestParam(value = "offset", required = false) Integer offset, @ApiParam(value = "Number of items to retrieve. Default is 5, maximum is 100.") @RequestParam(value = "limit", required = false) Integer limit) {
        Collection<com.mocknidhi.persistence.entity.ApiDefinition> entities;

        if (offset == null || offset < 0) {
            //Get All records
            entities = apiDefinitionRepository.findAll();
        } else {
            //Get by Page
            entities = apiDefinitionRepository.findAll(new PageRequest((offset / limit), limit)).getContent();
        }

        if (CollectionUtils.isEmpty(entities)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(entities.stream()
                .map(item -> mapperFacade.map(item, ApiDefinition.class))
                .collect(Collectors
                        .toList()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OperationDefinition> getOperationDefinition(@ApiParam(value = "Api Definition identifier", required = true) @PathVariable("apiDefId") String apiDefId, @ApiParam(value = "Operation definition identifier", required = true) @PathVariable("operationDefId") String operationDefId) {
        com.mocknidhi.persistence.entity.ApiDefinition entity = apiDefinitionRepository.findOne(UUID.fromString(apiDefId));

        if (entity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        com.mocknidhi.persistence.entity.OperationDefinition operationDefEntity = operationDefById(entity, UUID.fromString(operationDefId));

        if (operationDefEntity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(mapperFacade.map(operationDefEntity, OperationDefinition.class), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<List<OperationDefinition>> getOperationDefinitions(@ApiParam(value = "Api Definition identifier", required = true) @PathVariable("apiDefId") String apiDefId, @ApiParam(value = "Offset the list of returned results by this amount. Default is zero.") @RequestParam(value = "offset", required = false) Integer offset, @ApiParam(value = "Number of items to retrieve. Default is 5, maximum is 100.") @RequestParam(value = "limit", required = false) Integer limit) {
        com.mocknidhi.persistence.entity.ApiDefinition entity = apiDefinitionRepository.findOne(UUID.fromString(apiDefId));

        if (entity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(entity.getOperationDefinitions()
                .stream()
                .map(item -> mapperFacade.map(item, OperationDefinition.class))
                .collect(Collectors
                        .toList()), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<ApiDefinition> updateApiDefinition(@ApiParam(value = "Api Definition identifier", required = true) @PathVariable("apiDefId") String apiDefId, @ApiParam(value = "Api Definition to update", required = true) @RequestBody ApiDefinition apiDefinition) {
        apiDefinition.setId(apiDefId);
        return new ResponseEntity<>(save(apiDefinition), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<OperationDefinition> updateOperationDefinition(@ApiParam(value = "Api Definition identifier", required = true) @PathVariable("apiDefId") String apiDefId, @ApiParam(value = "Operation definition identifier", required = true) @PathVariable("operationDefId") String operationDefId, @ApiParam(value = "Operation definition to update", required = true) @RequestBody OperationDefinition operationDefinition) {
        operationDefinition.setId(operationDefId);
        return new ResponseEntity<>(save(UUID.fromString(apiDefId), operationDefinition), HttpStatus.OK);
    }

    private ApiDefinition save(ApiDefinition apiDefinition) {
        com.mocknidhi.persistence.entity.ApiDefinition entity = mapperFacade.map(apiDefinition, com.mocknidhi.persistence.entity.ApiDefinition.class);

        com.mocknidhi.persistence.entity.ApiDefinition savedEntity = apiDefinitionRepository.save(entity);
        return mapperFacade.map(savedEntity, ApiDefinition.class);
    }

    private OperationDefinition save(UUID apiDefId, OperationDefinition operationDefinition) {

        com.mocknidhi.persistence.entity.ApiDefinition entity = apiDefinitionRepository.findOne(apiDefId);

        if (entity == null) {
            throw new NotFoundException(String.format("ApiDefinition {0} not found", apiDefId));
        }

        com.mocknidhi.persistence.entity.OperationDefinition persistedOperationDef = operationDefById(entity, UUID.fromString(operationDefinition.getId()));

        com.mocknidhi.persistence.entity.OperationDefinition operationDefEntity = mapperFacade.map(operationDefinition, com.mocknidhi.persistence.entity.OperationDefinition.class);

        if (persistedOperationDef == null) {
            //create
            entity.getOperationDefinitions().add(operationDefEntity);
        } else {
            //update
            mapperFacade.map(operationDefEntity, persistedOperationDef);
        }

        apiDefinitionRepository.save(entity);

        return operationDefinition;

    }

    private com.mocknidhi.persistence.entity.OperationDefinition operationDefById(com.mocknidhi.persistence.entity.ApiDefinition apiDefinition, UUID operationDefId) {
        return apiDefinition.getOperationDefinitions()
                .stream()
                .filter(def -> def.getId().equals(operationDefId))
                .findFirst()
                .orElse(null);
    }
}
