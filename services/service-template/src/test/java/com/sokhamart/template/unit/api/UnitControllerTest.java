package com.sokhamart.template.unit.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class UnitControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldSaveSuccessAndReturnPCS() throws Exception {
        mockMvc.perform(post("/api/v1/units")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"code\":\"PCS\",\"name\":\"Piece\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("PCS"));
    }

    @Test
    void shouldContainKGOnGetAll() throws Exception {
        mockMvc.perform(post("/api/v1/units")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"code\":\"KG\",\"name\":\"Kilogram\"}"))
                .andExpect(status().isCreated());

        String jsonResponse = mockMvc.perform(get("/api/v1/units"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<String> codes = JsonPath.read(jsonResponse, "$[*].code");
        assertThat(codes).contains("KG");
    }
}
