package com.drozd.ecaps.security.filter;

import com.drozd.ecaps.security.google.identity.GoogleIdentityVerificationService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenAuthFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER_PREFIX = "Bearer ";
    private final GoogleIdentityVerificationService verificationService;
    private final Logger log = LoggerFactory.getLogger(JwtTokenAuthFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String requestHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(!StringUtils.hasLength(requestHeader) || !requestHeader.startsWith(AUTH_HEADER_PREFIX)){
            filterChain.doFilter(request, response);
            return;
        }

        final String authorizationHeader = requestHeader.substring(AUTH_HEADER_PREFIX.length());
        try {
            final GoogleIdToken idToken = verificationService.verify(authorizationHeader);
            List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            if(idToken != null){
                Authentication auth =
                        new UsernamePasswordAuthenticationToken(idToken, null, grantedAuthorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            log.info("Unauthenticated attempt to connect.");
            response.sendError(HttpStatus.UNAUTHORIZED.value(),
                    "Invalid token: " + request.getHeader(HttpHeaders.AUTHORIZATION));
            return;
        }
        filterChain.doFilter(request, response);
    }


}
