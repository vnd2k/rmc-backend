package rmc.backend.rmc.security;

public enum UserPermission {
    MEMBER_READ("member:read"),
    MEMBER_WRITE("member:write"),
    COMPANY_READ("company:read"),
    COMPANY_WRITE("company:write");

    private final String permission;

    UserPermission(String permission) {
        this.permission = permission;
    }
}
