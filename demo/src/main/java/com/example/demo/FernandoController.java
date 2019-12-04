package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FernandoController {

    private final FernandoService fernandoService;

    public FernandoController(FernandoService fernandoService) {
        this.fernandoService = fernandoService;
    }

    @GetMapping
    public Map<String, String> getUser(){
        return fernandoService.getUser();
    }
}

