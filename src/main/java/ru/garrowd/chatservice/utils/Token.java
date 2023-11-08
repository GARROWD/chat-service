package ru.garrowd.chatservice.utils;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import ru.garrowd.chatservice.utils.enums.JwtClaims;

public class Token {
    public static String get(JwtAuthenticationToken token, JwtClaims attribute){
        return (String) token.getTokenAttributes().get(attribute.getValue());
    }
}
