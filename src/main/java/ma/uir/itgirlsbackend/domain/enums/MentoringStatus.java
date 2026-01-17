package ma.uir.itgirlsbackend.domain.enums;

public enum MentoringStatus {
    EN_ATTENTE("En attente"),
    CONFIRME("Confirmé"),
    REFUSE("Refusé");

    private final String label;

    MentoringStatus(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
