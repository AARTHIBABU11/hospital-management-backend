package com.hospital.controller;

import com.hospital.dto.ApiResponse;
import com.hospital.dto.DashboardStatsDto;
import com.hospital.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/stats")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getAdminStats() {
        return ResponseEntity.ok(ApiResponse.ok("Stats fetched", dashboardService.getAdminDashboardStats()));
    }

    @GetMapping("/public")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPublicStats() {
        return ResponseEntity.ok(ApiResponse.ok("Public stats fetched", dashboardService.getPublicStats()));
    }
}
