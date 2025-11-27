package com.pato.config.filter;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pato.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenValidatorTest {

    JwtUtils jwtUtils;
    JwtTokenValidator filter;

    @BeforeEach
    void setup() {
        jwtUtils = mock(JwtUtils.class);
        filter = new JwtTokenValidator(jwtUtils);

        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilter_validToken_shouldAuthenticate() throws Exception {

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Authorization", "Bearer TOKEN123");

        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        DecodedJWT decoded = mock(DecodedJWT.class);

        when(jwtUtils.validateToken("TOKEN123")).thenReturn(decoded);
        when(jwtUtils.extractUsername(decoded)).thenReturn("pato");

        Claim claimMock = mock(Claim.class);
        when(claimMock.asString()).thenReturn("ROLE_ADMIN,CAN_EDIT");

        when(jwtUtils.getSpecificClaim(decoded, "authorities")).thenReturn(claimMock);

        filter.doFilter(req, res, chain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("pato",
                SecurityContextHolder.getContext().getAuthentication().getPrincipal()
        );

        assertEquals(2,
                SecurityContextHolder.getContext().getAuthentication().getAuthorities().size()
        );
    }

    @Test
    void doFilter_invalidToken_shouldNotAuthenticate() throws Exception {

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Authorization", "Bearer INVALID");

        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        when(jwtUtils.validateToken("INVALID")).thenThrow(new RuntimeException("Token malo"));

        filter.doFilter(req, res, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilter_noToken_shouldIgnore() throws Exception {

        MockHttpServletRequest req = new MockHttpServletRequest();
        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(req, res, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtils, never()).validateToken(any());
    }

    @Test
    void doFilter_headerWithoutBearer_shouldIgnore() throws Exception {

        MockHttpServletRequest req = new MockHttpServletRequest();
        req.addHeader("Authorization", "TOKEN123");

        MockHttpServletResponse res = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(req, res, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(jwtUtils, never()).validateToken(any());
    }
}
