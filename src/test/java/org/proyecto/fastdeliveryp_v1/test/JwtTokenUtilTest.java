package org.proyecto.fastdeliveryp_v1.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.proyecto.fastdeliveryp_v1.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@TestPropertySource(properties = {
        "jwt.secret=your-256-bit-secret-which-needs-to-be-at-least-64-characters-long-and-secure",
        "jwt.expiration=3600"
})
public class JwtTokenUtilTest {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @BeforeEach
    public void setUp() {
        jwtTokenUtil.init();
    }

    @Test
    public void testCreateToken() {
        String token = jwtTokenUtil.createToken("test@example.com", "ROLE_USER");
        assertNotNull(token, "The token should not be null");
    }

    @Test
    public void testValidateToken() {
        String token = jwtTokenUtil.createToken("test@example.com", "ROLE_USER");
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                "test@example.com", "", AuthorityUtils.createAuthorityList("ROLE_USER"));
        assertTrue(jwtTokenUtil.validateToken(token, userDetails), "The token should be valid");
    }

    @Test
    public void testIsTokenExpired() {
        String token = jwtTokenUtil.createToken("test@example.com", "ROLE_USER");
        assertFalse(jwtTokenUtil.isTokenExpired(token), "The token should not be expired");
    }
}
