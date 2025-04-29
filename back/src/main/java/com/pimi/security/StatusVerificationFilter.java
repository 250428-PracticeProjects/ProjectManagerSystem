package com.pimi.security;

import com.pimi.exceptions.ForbiddenActionException;
import com.pimi.models.Status;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class StatusVerificationFilter extends OncePerRequestFilter {


    public StatusVerificationFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (!request.getServletPath().startsWith("/auth")) {
            Object principal = SecurityContextHolder.getContext().getAuthentication() != null
                    ? SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                    : null;

            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                CustomUserDetails customUserDetails = (CustomUserDetails) userDetails;

                if (customUserDetails.getStatus().equals(Status.INACTIVE)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setContentType("application/json");
                    String body = String.format("{\"error\": \"%s\", \"message\": \"%s\"}",
                            HttpStatus.FORBIDDEN.getReasonPhrase(),
                            "This user has been inactivated by the Admin.");
                    response.getWriter().write(body);
                    response.getWriter().flush();
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }

}
