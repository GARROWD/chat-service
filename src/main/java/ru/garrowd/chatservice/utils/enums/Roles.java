package ru.garrowd.chatservice.utils.enums;

import lombok.Getter;

@Getter
public enum Roles {
    ADMIN("ADMIN"),
    CURATOR("CURATOR"),
    EMPLOYEE("EMPLOYEE"),
    ENROLLEE("ENROLLEE"),
    STUDENT("STUDENT"),
    SUPERVISOR("SUPERVISOR"),
    TEACHER("TEACHER");

    private final String value;

    Roles(String value) {
        this.value = value;
    }
}

