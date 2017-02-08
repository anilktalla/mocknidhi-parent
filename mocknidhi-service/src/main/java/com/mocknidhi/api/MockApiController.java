package com.mocknidhi.api;

import com.mocknidhi.persistence.repository.MockRepository;
import com.mocknidhi.protocol.mock.api.MockApi;
import com.mocknidhi.protocol.mock.model.Mock;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by anilkumartalla on 2/5/17.
 */
@RestController
public class MockApiController implements MockApi {

    private final MockRepository mockRepository;
    private final MapperFacade mapperFacade;

    @Autowired
    public MockApiController(final MockRepository mockRepository, final MapperFacade mapperFacade) {
        this.mockRepository = mockRepository;
        this.mapperFacade = mapperFacade;
    }

    public ResponseEntity<Mock> createMockEndpoint(@RequestBody Mock mock) {
        return new ResponseEntity<>(save(mock), HttpStatus.CREATED);
    }

    public ResponseEntity<Void> deleteMockEndpoint(@PathVariable("mockId") String mockId) {
        mockRepository.delete(UUID.fromString(mockId));

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<List<Mock>> getAllMockEndpoints(@RequestParam(value = "offset", required = false) Integer offset, @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit) {

        Collection<com.mocknidhi.persistence.entity.Mock> entities;

        if (offset == null || offset < 0) {
            //Get All records
            entities = mockRepository.findAll();
        } else {
            //Get by Page
            entities = mockRepository.findAll(new PageRequest((offset / limit), limit)).getContent();
        }

        if (CollectionUtils.isEmpty(entities)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(entities.stream()
                .map(item -> mapperFacade.map(item, Mock.class))
                .collect(Collectors
                        .toList()), HttpStatus.OK);
    }

    public ResponseEntity<Mock> getMockEndpointById(@PathVariable("mockId") String mockId) {
        com.mocknidhi.persistence.entity.Mock entity = mockRepository.findOne(UUID.fromString(mockId));

        if (entity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(mapperFacade.map(entity, Mock.class), HttpStatus.OK);
    }

    public ResponseEntity<Mock> updateMockEndpoint(@PathVariable("mockId") String mockId, @RequestBody Mock mock) {

        com.mocknidhi.persistence.entity.Mock entity = mockRepository.findOne(UUID.fromString(mockId));

        if (entity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        mock.setId(mockId);

        Mock savedMockInstance = save(mock);

        return new ResponseEntity<>(mapperFacade.map(savedMockInstance, Mock.class), HttpStatus.OK);
    }

    private Mock save(Mock mock) {
        com.mocknidhi.persistence.entity.Mock mockEntity = mapperFacade.map(mock, com.mocknidhi.persistence.entity.Mock.class);
        com.mocknidhi.persistence.entity.Mock savedEntity = mockRepository.save(mockEntity);
        return mapperFacade.map(savedEntity, Mock.class);
    }
}
