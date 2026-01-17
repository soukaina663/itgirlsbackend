package ma.uir.itgirlsbackend.controller;

import lombok.RequiredArgsConstructor;
import ma.uir.itgirlsbackend.dto.GirlDashboardResponse;
import ma.uir.itgirlsbackend.security.AuthUser;
import ma.uir.itgirlsbackend.service.GirlDashboardService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/girl/dashboard")
@RequiredArgsConstructor
public class GirlDashboardController {

    private final GirlDashboardService girlDashboardService;

    @GetMapping
    public GirlDashboardResponse get(@AuthenticationPrincipal AuthUser me) {
        return girlDashboardService.getDashboard(me.id());
    }
}
