package com.pato.utils;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();

        ReflectionTestUtils.setField(jwtUtils, "privateKey", "test-private-key");
        ReflectionTestUtils.setField(jwtUtils, "userGenerator", "test-issuer");
    }

    @Test
    void createToken_ok() {
        var auth = new UsernamePasswordAuthenticationToken(
                "pato",
                "123",
                java.util.List.of(() -> "ROLE_ADMIN", () -> "CAN_EDIT")
        );

        String token = jwtUtils.createToken(auth);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void validateToken_ok() {
        var auth = new UsernamePasswordAuthenticationToken(
                "pato",
                "123",
                java.util.List.of(() -> "ROLE_ADMIN")
        );

        String token = jwtUtils.createToken(auth);

        DecodedJWT decoded = jwtUtils.validateToken(token);

        assertNotNull(decoded);
        assertEquals("pato", decoded.getSubject());
        assertEquals("test-issuer", decoded.getIssuer());
    }

    @Test
    void validateToken_invalido_lanzaExcepcion() {
        String tokenInvalido = "123.456.789";

        assertThrows(JWTVerificationException.class,
                () -> jwtUtils.validateToken(tokenInvalido)
        );
    }

    @Test
    void extractUsername_y_claims_ok() {
        var auth = new UsernamePasswordAuthenticationToken(
                "pato",
                "123",
                java.util.List.of(() -> "ROLE_ADMIN", () -> "CAN_EDIT")
        );

        String token = jwtUtils.createToken(auth);
        DecodedJWT decoded = jwtUtils.validateToken(token);

        String username = jwtUtils.extractUsername(decoded);
        assertEquals("pato", username);

        Map<String, Claim> claims = jwtUtils.returnAllClaims(decoded);

        assertTrue(claims.containsKey("authorities"));

        String authorities = claims.get("authorities").asString();

        assertEquals("ROLE_ADMIN,CAN_EDIT", authorities);

    }
}
