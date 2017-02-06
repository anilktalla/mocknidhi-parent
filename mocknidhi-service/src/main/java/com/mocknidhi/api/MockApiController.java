package com.mocknidhi.api;

import com.mocknidhi.persistence.repository.MockRepository;
import com.mocknidhi.protocol.mock.api.MockApi;
import com.mocknidhi.protocol.mock.model.Mock;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Created by anilkumartalla on 2/5/17.
 */
@RestController
public class MockApiController implements MockApi {

    @Autowired
    private MockRepository mockRepository;

    @Autowired
    private MapperFacade mapper;

    public Mock createMockEndpoint(@RequestBody Mock mock) {
        return save(mock);
    }

    public void deleteMockEndpoint(String mockId) {
        mockRepository.delete(UUID.fromString(mockId));
    }

    public List<Mock> getAllMockEndpoints(Integer offset, Integer limit) {
        return null;
    }

    public Mock getMockEndpointById(String mockId) {
        com.mocknidhi.persistence.entity.Mock entity = mockRepository.findOne(UUID.fromString(mockId));

        return mapper.map(entity, Mock.class);
    }

    public Mock updateMockEndpoint(String mockId, Mock mock) {

        mock.setId(mockId);

        return save(mock);
    }

    private Mock save(Mock mock) {
        com.mocknidhi.persistence.entity.Mock mockEntity = mapper.map(mock, com.mocknidhi.persistence.entity.Mock.class);
        com.mocknidhi.persistence.entity.Mock savedEntity = mockRepository.insert(mockEntity);
        return mapper.map(savedEntity, Mock.class);
    }
}
