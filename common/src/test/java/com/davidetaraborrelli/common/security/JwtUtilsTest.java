package com.davidetaraborrelli.common.security;

import com.davidetaraborrelli.common.dto.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils(
                "test-secret-key-che-deve-essere-lunga-almeno-256-bit-ok",
                86400000L
        );
    }

    @Test
    void generateToken_returnsNonNullString() {
        String token = jwtUtils.generateToken(1L, "testuser", "test@test.com");

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void validateAndExtract_returnsCorrectUserInfo() {
        String token = jwtUtils.generateToken(1L, "testuser", "test@test.com");

        UserInfo userInfo = jwtUtils.validateAndExtract(token);

        assertEquals(1L, userInfo.id());
        assertEquals("testuser", userInfo.username());
        assertEquals("test@test.com", userInfo.email());
    }

    @Test
    void validateAndExtract_invalidToken_throws() {
        assertThrows(Exception.class, () ->
                jwtUtils.validateAndExtract("invalid.token.string")
        );
    }

    @Test
    void validateAndExtract_expiredToken_throws() {
        JwtUtils expiredJwtUtils = new JwtUtils(
                "test-secret-key-che-deve-essere-lunga-almeno-256-bit-ok",
                0L
        );

        String token = expiredJwtUtils.generateToken(1L, "testuser", "test@test.com");

        assertThrows(Exception.class, () ->
                jwtUtils.validateAndExtract(token)
        );
    }
}
