package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FernandoController {

    @GetMapping
    public Map<String, String> getUser(){
        Map<String, String> r = new HashMap<>();
        r.put("nome", "fernando");
        return r;
    }
}
