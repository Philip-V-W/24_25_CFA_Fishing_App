package cfa.fishing.fishing_store_app.entity.user;

public enum Role {
    ADMIN,
    CUSTOMER;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}