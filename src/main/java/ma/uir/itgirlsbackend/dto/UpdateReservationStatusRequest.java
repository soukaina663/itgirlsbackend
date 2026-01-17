package ma.uir.itgirlsbackend.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateReservationStatusRequest(
        @NotBlank String status // "Confirmé" / "En attente" / "Refusé" ou CONFIRME/EN_ATTENTE/REFUSE
) {}
