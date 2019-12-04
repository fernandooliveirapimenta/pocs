package com.example.demo;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FernandoService {

    @HystrixCommand( commandKey = "getUserFromDb" )
    public Map<String, String> getUser(){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Map<String, String> r = new HashMap<>();
        r.put("nome", "fernando");
        return r;
    }
}

