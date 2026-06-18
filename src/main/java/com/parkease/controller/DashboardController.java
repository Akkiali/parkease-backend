package com.parkease.controller;

import org.springframework.web.bind.annotation.*;

import com.parkease.dto.DashboardResponse;
import com.parkease.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin("*")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService) {

        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public DashboardResponse getStats() {

        return dashboardService.getStats();
    }
}