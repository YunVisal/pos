package com.sokhamart.template.currency.api;

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
class CurrencyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldSaveSuccessAndReturnUSD() throws Exception {
        mockMvc.perform(post("/api/v1/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"code\":\"USD\",\"name\":\"US Dollar\", \"symbol\": \"$\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("USD"));
    }

    @Test
    void shouldContainKHROnGetAll() throws Exception {
        mockMvc.perform(post("/api/v1/currencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"code\":\"KHR\",\"name\":\"Khmer Riel\", \"symbol\": \"R\"}"))
                .andExpect(status().isCreated());

        String jsonResponse = mockMvc.perform(get("/api/v1/currencies"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<String> codes = JsonPath.read(jsonResponse, "$[*].code");
        assertThat(codes).contains("KHR");
    }
}
