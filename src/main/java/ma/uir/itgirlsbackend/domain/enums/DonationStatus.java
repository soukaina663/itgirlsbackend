package ma.uir.itgirlsbackend.domain.enums;

public enum DonationStatus {
    PENDING,     // demande reçue (par défaut)
    CONFIRMED,   // don confirmé (admin)
    FAILED       // don échoué / annulé (admin)
}
