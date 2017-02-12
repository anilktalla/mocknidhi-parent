package com.mocknidhi.api;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.google.gson.Gson;
import com.mocknidhi.persistence.repository.IApiDefinitionRepository;
import com.mocknidhi.protocol.mock.model.ApiDefinition;
import com.mocknidhi.protocol.mock.model.OperationDefinition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import com.mocknidhi.MocknidhiSpringBoot;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import com.oneeyedmen.fakir.Faker;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by anilkumartalla on 2/6/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MocknidhiSpringBoot.class)
@WebAppConfiguration
public class ApiDefinitionControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private IApiDefinitionRepository apiDefinitionRepository;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertThat(this.mappingJackson2HttpMessageConverter).isNotNull();
    }

    @Before
    public void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testCreateApiDefinitionSuccess() throws Exception {

        //Then
        assertThat(createApiDefinition()).isNotEmpty();

    }

    @Test
    public void testUpdateApiDefinitionSuccess() throws Exception {

        //Given
        String response = createApiDefinition();

        String id = new Gson().fromJson(response, ApiDefinition.class).getId();

        //Then
        assertThat(id).isNotEmpty();
        mockMvc.perform(put("/api/v1/apiDefinition/" + id)
                .content(json(apiDefinition()))
                .contentType(contentType))
                .andExpect(status().isOk());

    }

    @Test
    public void testGetApiDefinitionSuccess() throws Exception {

        //Given
        String response = createApiDefinition();

        String id = new Gson().fromJson(response, ApiDefinition.class).getId();

        //Then
        mockMvc.perform(get("/api/v1/apiDefinition/" + id)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(id));

    }

    @Test
    public void testGetAllApiDefinitionSuccess() throws Exception {

        //Given
        String response = createApiDefinition();

        String id = new Gson().fromJson(response, ApiDefinition.class).getId();

        //Then
        mockMvc.perform(get("/api/v1/apiDefinition")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.[?(@.id)]").exists())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains(id);

    }

    @Test
    public void testDeleteApiDefinitionSuccess() throws Exception {

        //Given
        String response = createApiDefinition();

        String id = new Gson().fromJson(response, ApiDefinition.class).getId();

        //Then
        mockMvc.perform(delete("/api/v1/apiDefinition/" + id)
                .contentType(contentType))
                .andExpect(status().isNoContent());

    }

    @Test
    public void testCreateOperationDefinitionSuccess() throws Exception {

        //Given
        String response = createApiDefinition();
        String apiDefId = new Gson().fromJson(response, ApiDefinition.class).getId();

        //Then
        assertThat(createOperationDefinition(apiDefId)).isNotEmpty();

    }

    @Test
    public void testUpdateOperationDefinitionSuccess() throws Exception {

        //Given
        String apiDefResponse = createApiDefinition();
        String apiDefId = new Gson().fromJson(apiDefResponse, ApiDefinition.class).getId();
        String operationResponse = createOperationDefinition(apiDefId);
        String operationDefId = new Gson().fromJson(operationResponse, OperationDefinition.class).getId();


        //Then
        assertThat(operationDefId).isNotEmpty();
        mockMvc.perform(put("/api/v1/apiDefinition/".concat(apiDefId).concat("/operationDefinition/").concat(operationDefId))
                .content(json(operationDefinition()))
                .contentType(contentType))
                .andExpect(status().isOk());

    }

    @Test
    public void testGetOperationDefinitionSuccess() throws Exception {

        //Given
        String apiDefResponse = createApiDefinition();
        String apiDefId = new Gson().fromJson(apiDefResponse, ApiDefinition.class).getId();
        String operationResponse = createOperationDefinition(apiDefId);
        String operationDefId = new Gson().fromJson(operationResponse, OperationDefinition.class).getId();

        //Then
        mockMvc.perform(get("/api/v1/apiDefinition/".concat(apiDefId).concat("/operationDefinition/").concat(operationDefId))
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(operationDefId));

    }

    @Test
    public void testGetAllOperationDefinitionSuccess() throws Exception {

        //Given
        String apiDefResponse = createApiDefinition();
        String apiDefId = new Gson().fromJson(apiDefResponse, ApiDefinition.class).getId();
        String operationResponse = createOperationDefinition(apiDefId);
        String operationDefId = new Gson().fromJson(operationResponse, OperationDefinition.class).getId();

        //Then
        mockMvc.perform(get("/api/v1/apiDefinition/".concat(apiDefId).concat("/operationDefinition"))
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.[?(@.id)]").exists())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains(operationDefId);

    }

    @Test
    public void testDeleteOperationDefinitionSuccess() throws Exception {

        //Given
        String apiDefResponse = createApiDefinition();
        String apiDefId = new Gson().fromJson(apiDefResponse, ApiDefinition.class).getId();
        String operationResponse = createOperationDefinition(apiDefId);
        String operationDefId = new Gson().fromJson(operationResponse, OperationDefinition.class).getId();

        //Then
        mockMvc.perform(delete("/api/v1/apiDefinition/".concat(apiDefId).concat("/operationDefinition/").concat(operationDefId))
                .contentType(contentType))
                .andExpect(status().isNoContent());

    }

    private String createApiDefinition() throws Exception {

        return mockMvc.perform(post("/api/v1/apiDefinition")
                .content(json(apiDefinition()))
                .contentType(contentType))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
    }

    private String createOperationDefinition(String apiDefId) throws Exception {
        return mockMvc.perform(post("/api/v1/apiDefinition/".concat(apiDefId).concat("/operationDefinition"))
                .content(json(operationDefinition()))
                .contentType(contentType))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
    }

    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

    private ApiDefinition apiDefinition() {
        return Faker.fakeA(ApiDefinition.class);
    }

    private OperationDefinition operationDefinition() {
        return Faker.fakeA(OperationDefinition.class);
    }

}
