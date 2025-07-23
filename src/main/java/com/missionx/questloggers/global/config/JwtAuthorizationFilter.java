package com.missionx.questloggers.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.missionx.questloggers.domain.auth.exception.ExpiredTokenException;
import com.missionx.questloggers.domain.auth.exception.MalformedTokenException;
import com.missionx.questloggers.domain.auth.exception.NoneTokenException;
import com.missionx.questloggers.domain.user.repository.UserRepository;
import com.missionx.questloggers.global.config.security.LoginUser;
import com.missionx.questloggers.global.dto.ApiResponse;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import com.missionx.questloggers.domain.user.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class JwtAuthorizationFilter implements Filter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    // json으로 예외메세지 반환
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 토큰검증 뺄 api들
    private final Set<String> excludedPaths = Set.of(
            "/api/auth/login",
            "/api/auth/signup",
            "/api/auth/logout"
    );

    public JwtAuthorizationFilter(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 토큰 필요없는 url 통과
        if (isExcludedPath(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            String token = jwtTokenProvider.resolveToken(httpRequest);

            if (token != null && jwtTokenProvider.validateToken(token)) {
                Long userId = jwtTokenProvider.getUserIdFromToken(token);
                Optional<User> userOptional = userRepository.findById(userId);

                if (userOptional.isEmpty() || userOptional.get().isDeleted()) {
                    sendJsonError(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 사용자입니다.");
                    return;
                }

                setAuthentication(userOptional.get(), token);
                httpRequest.setAttribute("userId", userId);
            }

            chain.doFilter(request, response);

        } catch (NoneTokenException | ExpiredTokenException | MalformedTokenException ex) {
            sendJsonError(httpResponse, HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
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
                jwtTokenProvider.getPointFromToken(token)
        );

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                loginUser,
                null,
                loginUser.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // 예외 json형식으로 반환
    private void sendJsonError(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json; charset=UTF-8");

        ApiResponse<Void> errorResponse = new ApiResponse<>(message, null);
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}