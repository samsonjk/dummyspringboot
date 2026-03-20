package com.axxohub.dummyspringboot.controller;

import com.axxohub.dummyspringboot.service.DataResetService;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final DataResetService dataResetService;

    public TestController(DataResetService dataResetService) {
        this.dataResetService = dataResetService;
    }

    @PostMapping("/reset")
    public Map<String, String> reset() {
        dataResetService.reset();
        return Map.of("message", "Database reset complete succeessful");
    }
}
