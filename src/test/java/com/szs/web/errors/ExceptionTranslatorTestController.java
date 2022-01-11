package com.szs.web.errors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/exception-translator-test")
public class ExceptionTranslatorTestController {

    @GetMapping("/unauthorized")
    public void unauthorized() {
        throw new UnAuthorizedException();
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
