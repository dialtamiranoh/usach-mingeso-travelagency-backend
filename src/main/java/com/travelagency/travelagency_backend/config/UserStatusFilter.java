package com.travelagency.travelagency_backend.config;

import com.travelagency.travelagency_backend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class UserStatusFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof Jwt jwt) {
            String keycloakId = jwt.getSubject();

            userRepository.findByKeycloakId(keycloakId).ifPresent(user -> {
                if (user.getStatus() != null && user.getStatus().getName().equals("INACTIVE")) {
                    try {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("Usuario inactivo");
                        response.flushBuffer();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            if (response.isCommitted()) return;
        }

        filterChain.doFilter(request, response);
    }
}