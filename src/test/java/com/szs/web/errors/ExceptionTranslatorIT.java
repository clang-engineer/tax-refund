package com.szs.web.errors;

import com.szs.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
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
    void testAccessDenied() throws Exception {
        mockMvc
                .perform(get("/api/exception-translator-test/access-denied"))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Access Denied"))
                .andExpect(jsonPath("$.description").value("access denied"))
                .andExpect(jsonPath("$.status").value(HttpStatus.FORBIDDEN.value()));
    }

    @Test
    void testUnauthorized() throws Exception {
        mockMvc
                .perform(get("/api/exception-translator-test/unauthorized"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Not Authorized"))
                .andExpect(jsonPath("$.description").value("authorized user session not found"))
                .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void testUserNotFound() throws Exception {
        mockMvc
                .perform(get("/api/exception-translator-test/user-info-not-found"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Not Authorized"))
                .andExpect(jsonPath("$.description").value("authorized user session not found"))
                .andExpect(jsonPath("$.status").value(HttpStatus.UNAUTHORIZED.value()));
    }

    @Test
    void testScrapNotFound() throws Exception {
        mockMvc
                .perform(get("/api/exception-translator-test/scrap-not-found"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Scrap Not Found"))
                .andExpect(jsonPath("$.description").value("can't find scrap info"))
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    void testScrapSaveFail() throws Exception {
        mockMvc
                .perform(get("/api/exception-translator-test/scrap-save-fail"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Scrap Save Fail"))
                .andExpect(jsonPath("$.description").value("can't save scrap info"))
                .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @Test
    void testInternalServerError() throws Exception {
        mockMvc
                .perform(get("/api/exception-translator-test/internal-server-error"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Internal Server Error"))
                .andExpect(jsonPath("$.description").value(""))
                .andExpect(jsonPath("$.status").value(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }


}