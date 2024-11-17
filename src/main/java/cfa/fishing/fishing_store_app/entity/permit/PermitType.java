package cfa.fishing.fishing_store_app.entity.permit;

import lombok.Getter;

@Getter
public enum PermitType {
    DAILY("1 Day Permit"),
    WEEKLY("7 Day Permit"),
    MONTHLY("30 Day Permit"),
    ANNUAL("Annual Permit"),
    LIFETIME("Lifetime Permit");

    private final String description;

    PermitType(String description) {
        this.description = description;
    }

}