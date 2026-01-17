package ma.uir.itgirlsbackend.controller;

import lombok.RequiredArgsConstructor;
import ma.uir.itgirlsbackend.domain.DonationRequest;
import ma.uir.itgirlsbackend.domain.enums.DonationStatus;
import ma.uir.itgirlsbackend.repo.DonationRequestRepository;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DonationController {

    private final DonationRequestRepository donationRepo;

    private static String s(Object v) {
        return v == null ? "" : String.valueOf(v).trim();
    }

    private static BigDecimal money(Object v) {
        try {
            String str = s(v).replace(",", ".");
            if (str.isBlank()) return null;
            return new BigDecimal(str);
        } catch (Exception e) {
            return null;
        }
    }

    // ✅ Public: création demande don
    @PostMapping("/api/public/donations")
    public Map<String, Object> createDonation(@RequestBody Map<String, Object> body) {
        String firstName = s(body.get("firstName"));
        String lastName  = s(body.get("lastName"));
        String email     = s(body.get("email")).toLowerCase();
        BigDecimal amount = money(body.get("amount"));
        String message   = s(body.get("message"));

        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("firstName/lastName/email/amount required");
        }

        DonationRequest d = DonationRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .amount(amount)
                .message(message)
                .status(DonationStatus.PENDING) // ✅ par défaut
                .createdAt(LocalDateTime.now())
                .build();

        donationRepo.save(d);

        return Map.of("ok", true, "id", d.getId());
    }

    // ✅ Admin: liste des demandes
    @GetMapping("/api/admin/donations")
    public List<DonationRequest> listDonations() {
        return donationRepo.findAll()
                .stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .toList();
    }

    // ✅ Admin: changer le status (CONFIRMED / FAILED / PENDING)
    @PatchMapping("/api/admin/donations/{id}/status")
    public DonationRequest updateStatus(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String statusStr = s(body.get("status")).toUpperCase();

        DonationStatus next;
        try {
            next = DonationStatus.valueOf(statusStr);
        } catch (Exception e) {
            throw new RuntimeException("invalid status (use PENDING/CONFIRMED/FAILED)");
        }

        DonationRequest d = donationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("donation not found"));

        d.setStatus(next);
        return donationRepo.save(d);
    }
}
