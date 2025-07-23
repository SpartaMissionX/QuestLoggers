package com.missionx.questloggers.global.config;

import com.missionx.questloggers.domain.user.repository.UserRepository;
import com.missionx.questloggers.global.config.security.LoginUser;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import com.missionx.questloggers.domain.user.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class JwtAuthorizationFilter implements Filter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public JwtAuthorizationFilter(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = jwtTokenProvider.resolveToken(httpRequest);

        if (token != null && jwtTokenProvider.validateToken(token)) {
            Long userId = jwtTokenProvider.getUserIdFromToken(token);

            Optional<User> userOptional = userRepository.findById(userId);
            if (userOptional.isEmpty() || userOptional.get().isDeleted()) {
                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 사용자입니다.");
                return;
            }

            String email = jwtTokenProvider.getEmailFromToken(token);
            String role = jwtTokenProvider.getRoleFromToken(token);
            String apiKey = jwtTokenProvider.getApiKeyFromToken(token);
            Integer point = jwtTokenProvider.getPointFromToken(token);

            LoginUser loginUser = new LoginUser(userId, email, role, apiKey, point);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            loginUser,
                            null,
                            loginUser.getAuthorities()  // ROLE_USER 포함
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            httpRequest.setAttribute("userId", userId);  // 선택 사항
        }

        chain.doFilter(request, response);
    }
}