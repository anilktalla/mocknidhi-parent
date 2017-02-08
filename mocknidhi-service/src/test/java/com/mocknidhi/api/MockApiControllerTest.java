package com.mocknidhi.api;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.google.gson.Gson;
import com.mocknidhi.persistence.repository.MockRepository;
import com.mocknidhi.protocol.mock.model.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
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
import java.util.regex.Matcher;

/**
 * Created by anilkumartalla on 2/6/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MocknidhiSpringBoot.class)
@WebAppConfiguration
public class MockApiControllerTest {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockRepository mockRepository;

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
    public void testCreateMockEndpointSuccess() throws Exception {

        //Then
        createMock();

    }

    @Test
    public void testUpdateMockEndpointSuccess() throws Exception {

        //Given
        String response = createMock();

        String id = new Gson().fromJson(response, Mock.class).getId();

        //Then
        mockMvc.perform(put("/api/v1/mock/" + id)
                .content(json(mock()))
                .contentType(contentType))
                .andExpect(status().isOk());

    }

    @Test
    public void testGetMockEndpointSuccess() throws Exception {

        //Given
        String response = createMock();

        String id = new Gson().fromJson(response, Mock.class).getId();

        //Then
        mockMvc.perform(get("/api/v1/mock/" + id)
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id").value(id));

    }

    @Test
    public void testGetAllMockEndpointSuccess() throws Exception {

        //Given
        String response = createMock();

        String id = new Gson().fromJson(response, Mock.class).getId();

        //Then
        mockMvc.perform(get("/api/v1/mock")
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
    public void testDeleteMockEndpointSuccess() throws Exception {

        //Given
        String response = createMock();

        String id = new Gson().fromJson(response, Mock.class).getId();

        //Then
        mockMvc.perform(delete("/api/v1/mock/" + id)
                .contentType(contentType))
                .andExpect(status().isNoContent());

    }

    private String createMock() throws Exception {

        return mockMvc.perform(post("/api/v1/mock")
                .content(json(mock()))
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

    private Mock mock() {
        return Faker.fakeA(Mock.class);
    }

}
