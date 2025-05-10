package com.justynagajdek.healthreservationsystem;

import com.justynagajdek.healthreservationsystem.jwt.JwtTokenUtil;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenUtilTest {

    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void setUp() {
        jwtTokenUtil = new JwtTokenUtil();
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtSecret", "xiRWbQBeibMTnrNZ+untCpfL3HQFDp3XM7ZNfiC6MRo=");
        ReflectionTestUtils.setField(jwtTokenUtil, "jwtExpirationMs", 3600000L);
    }

    @Test
    public void testGenerateAndParseToken() {
        String email = "testuser@example.com";
        String token = jwtTokenUtil.generateJwtToken(email);
        assertNotNull(token);
        String extractedEmail = jwtTokenUtil.getUsernameFromJwtToken(token);
        assertEquals(email, extractedEmail);
    }

    @Test
    public void testValidateToken_ValidToken() {
        String token = jwtTokenUtil.generateJwtToken("testuser");
        assertTrue(jwtTokenUtil.validateJwtToken(token));
    }

    @Test
    public void testValidateToken_InvalidToken() {
        String invalidToken = "invalid.token.value";
        assertFalse(jwtTokenUtil.validateJwtToken(invalidToken));
    }

    @Test
    public void testGetUsernameFromJwtToken_NullToken() {
        String result = jwtTokenUtil.getUsernameFromJwtToken(null);
        assertNull(result, "Expected null when token is null");
    }

    @Test
    public void testGetUsernameFromJwtToken_EmptyToken() {
        String result = jwtTokenUtil.getUsernameFromJwtToken("");
        assertNull(result, "Expected null when token is empty");
    }
}
