package com.missionx.questloggers.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.missionx.questloggers.domain.user.entity.User;
import com.missionx.questloggers.domain.user.repository.UserRepository;
import com.missionx.questloggers.global.config.security.LoginUser;
import com.missionx.questloggers.global.dto.ApiResponse;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

public class JwtAuthorizationFilter implements Filter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Set<String> excludedPaths = Set.of(
            "/api/auth/login",
            "/api/auth/signup",
            "/api/auth/logout",
            "/api/auth/test",
            "/api/test/apply-party",
            "/api/auth/test/posts"
    );

    public JwtAuthorizationFilter(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public void doFilter(ServletRequest servletrequest, ServletResponse servletresponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpservletRequest = (HttpServletRequest) servletrequest;
        HttpServletResponse httpservletResponse = (HttpServletResponse) servletresponse;

        if (isExcludedPath(httpservletRequest)) {
            chain.doFilter(servletrequest, servletresponse);
            return;
        }

        try {
            String token = jwtTokenProvider.resolveToken(httpservletRequest);

            if (token != null && jwtTokenProvider.validateToken(token)) {
                Long userId = jwtTokenProvider.getUserIdFromToken(token);
                Optional<User> userOptional = userRepository.findById(userId);

                if (userOptional.isEmpty() || userOptional.get().getDeletedAt() != null) {
                    sendJsonError(httpservletResponse, HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 사용자입니다.");
                    return;
                }

                setAuthentication(userOptional.get(), token);
                httpservletRequest.setAttribute("userId", userId);
            }

            chain.doFilter(servletrequest, httpservletResponse);

        } catch (Exception ex) {
            sendJsonError(httpservletResponse, HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
        }
    }

    private boolean isExcludedPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return excludedPaths.stream().anyMatch(uri::startsWith);
    }

    private void setAuthentication(User user, String token) {
        LoginUser loginUser = new LoginUser(
                user.getId(),
                user.getEmail(),
                jwtTokenProvider.getRoleFromToken(token),
                jwtTokenProvider.getApiKeyFromToken(token),
                jwtTokenProvider.getPointFromToken(token),
                jwtTokenProvider.getOwnerCharIdFromToken(token),
                jwtTokenProvider.getOwnerCharNameFromToken(token)
        );

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                loginUser,
                null,
                loginUser.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void sendJsonError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json; charset=UTF-8");

        ApiResponse<Void> errorResponse = new ApiResponse<>(message, null);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
