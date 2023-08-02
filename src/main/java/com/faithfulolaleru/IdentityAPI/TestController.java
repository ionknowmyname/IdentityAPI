package com.faithfulolaleru.IdentityAPI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/test")
public class TestController {


    @GetMapping("/")
    public ResponseEntity<String> testing() {

        return ResponseEntity.ok("Please return Testing");
    }
}
