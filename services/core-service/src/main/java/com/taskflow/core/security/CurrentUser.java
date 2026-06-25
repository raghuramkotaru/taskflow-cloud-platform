package com.taskflow.core.security;

import com.taskflow.core.entity.User;
import com.taskflow.core.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Resolves the current user from the X-User-Id header. This is intentionally a
 * lightweight, header-based scheme (no JWT/OAuth) but it is enforced for
 * write operations via {@link #require()}.
 */
@Component
@RequestScope
public class CurrentUser {

    public static final String HEADER = "X-User-Id";

    private final UserRepository userRepository;

    public CurrentUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Returns the authenticated user or throws {@link UnauthorizedException} if
     * the header is missing or does not map to a known user.
     */
    public User require() {
        HttpServletRequest request = currentRequest();
        if (request == null) {
            throw new UnauthorizedException("No active request");
        }
        String header = request.getHeader(HEADER);
        if (header == null || header.isBlank()) {
            throw new UnauthorizedException("Missing " + HEADER + " header");
        }
        long userId;
        try {
            userId = Long.parseLong(header.trim());
        } catch (NumberFormatException e) {
            throw new UnauthorizedException("Invalid " + HEADER + " header");
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("Unknown user: " + userId));
    }

    private HttpServletRequest currentRequest() {
        RequestAttributes attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes servletAttrs) {
            return servletAttrs.getRequest();
        }
        return null;
    }
}
