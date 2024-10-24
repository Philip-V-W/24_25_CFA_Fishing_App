package cfa.fishing.fishing_store_app.entity.permit;

public enum PermitType {
    DAILY("1 Day Permit"),
    WEEKLY("7 Day Permit"),
    MONTHLY("30 Day Permit"),
    ANNUAL("Annual Permit");

    private final String description;

    PermitType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}