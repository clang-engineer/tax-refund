package com.szs.web.errors;

import com.szs.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser
@AutoConfigureMockMvc
@IntegrationTest
class ExceptionTranslatorIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testUnauthorized() throws Exception {
        mockMvc
                .perform(get("/api/exception-translator-test/unauthorized"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Not Authorized"))
                .andExpect(jsonPath("$.description").value("authorized user session not found"))
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void testScrapNotFound() throws Exception {
        mockMvc
                .perform(get("/api/exception-translator-test/scrap-not-found"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Scrap Not Found"))
                .andExpect(jsonPath("$.description").value("can't find scrap info"))
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void testInternalServerError() throws Exception {
        mockMvc
                .perform(get("/api/exception-translator-test/internal-server-error"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Internal Server Error"))
                .andExpect(jsonPath("$.description").value(""))
                .andExpect(jsonPath("$.status").value(500));
    }


}