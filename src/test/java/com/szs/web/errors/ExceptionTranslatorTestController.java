package com.szs.web.errors;

import com.szs.service.ScrapSaveFailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/exception-translator-test")
public class ExceptionTranslatorTestController {
    @PostMapping("/method-argument")
    public void methodArgument(@Valid @RequestBody TestDTO testDTO) {}

    public static class TestDTO {

        @NotNull
        private String test;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }
    }

    @GetMapping("/access-denied")
    public void accessdenied() {
        throw new AccessDeniedException("test access denied!");
    }

    @GetMapping("/unauthorized")
    public void unauthorized() {
        throw new BadCredentialsException("test authentication failed!");
    }

    @GetMapping("/user-info-not-found")
    public void userInfoNotFoundException() {
        throw new UserInfoNotFoundException();
    }

    @GetMapping("/scrap-not-found")
    public void scrapNotFoundException() {
        throw new ScrapNotFoundException();
    }

    @GetMapping("/scrap-save-fail")
    public void scrapSaveFailException() {
        throw new ScrapSaveFailException();
    }

    @GetMapping("/login-already-used")
    public void loginAlreadyUsed() {
        throw new LoginAlreadyUsedException();
    }

    @GetMapping("/internal-server-error")
    public void internalServerError() {
        throw new RuntimeException();
    }
}
