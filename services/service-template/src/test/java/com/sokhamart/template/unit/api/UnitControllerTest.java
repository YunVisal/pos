package com.sokhamart.template.unit.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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

        @Test
        void shouldReturn409ProblemDetailWhenCodeIsDuplicate() throws Exception {
                mockMvc.perform(post("/api/v1/units")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"code\":\"LB\",\"name\":\"pounds\"}"))
                                .andExpect(status().isCreated());

                mockMvc.perform(post("/api/v1/units")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"code\":\"LB\",\"name\":\"pounds\"}"))
                                .andExpect(status().isConflict())
                                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                                .andExpect(jsonPath("$.status").value("409"))
                                .andExpect(jsonPath("$.detail").value("Unit code already exists: LB"))
                                .andDo(print());
        }

        @Test
        void shouldReturn400ProblemDetailWhenCodeAndNameAreBlank() throws Exception {
                mockMvc.perform(post("/api/v1/units")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"code\":\"\",\"name\":\"\"}"))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                                .andExpect(jsonPath("$.status").value("400"))
                                .andExpect(jsonPath("$.errors.length()").value(2))
                                .andDo(print());
        }

        @Test
        void shouldReturn400ProblemDetailWhenCodeIsLowercase() throws Exception {
                mockMvc.perform(post("/api/v1/units")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"code\":\"kg\",\"name\":\"kilogram\"}"))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                                .andExpect(jsonPath("$.status").value("400"))
                                .andExpect(jsonPath("$.errors.[0].field").value("code"))
                                .andDo(print());
        }

        @Test
        void shouldReturn201WhenCodeAndNameIsValid() throws Exception {
                mockMvc.perform(post("/api/v1/units")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"code\":\"BOX12\",\"name\":\"Box 12\"}"))
                                .andExpect(status().isCreated())
                                .andDo(print());
        }
}
