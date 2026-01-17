package ma.uir.itgirlsbackend.controller;

import ma.uir.itgirlsbackend.dto.DashboardResponse;
import ma.uir.itgirlsbackend.security.AuthUser;
import ma.uir.itgirlsbackend.service.DashboardService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/expert/dashboard")
public class ExpertDashboardController {

    private final DashboardService dashboardService;

    public ExpertDashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public DashboardResponse get(@AuthenticationPrincipal AuthUser me) {
        return dashboardService.getDashboard(me.id());
    }
}
