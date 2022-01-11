package com.szs.web.errors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exception-translator-test")
public class ExceptionTranslatorTestController {

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

    @GetMapping("/internal-server-error")
    public void internalServerError() {
        throw new RuntimeException();
    }
}
