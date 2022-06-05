package rmc.backend.rmc.security;

import com.google.common.collect.Sets;

import java.util.Set;

import static rmc.backend.rmc.security.UserPermission.*;

public enum UserRole {
    MEMBER(),

    COMPANY(),

    ADMIN();
}
