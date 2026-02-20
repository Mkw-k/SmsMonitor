package com.mk.www.smsmonitor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mk.www.smsmonitor.presentation.dto.LoginRequest;
import com.mk.www.smsmonitor.presentation.dto.ResultDTO;
import com.mk.www.smsmonitor.presentation.dto.TokenResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        // 로그인 URL 매핑을 프로젝트 관례에 맞게 설정
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // 요청 바디에서 LoginRequest 추출
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginRequest.getLoginId(), loginRequest.getPassword());

            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        User user = (User) authResult.getPrincipal();
        String loginId = user.getUsername();
        String role = user.getAuthorities().stream().findFirst().get().getAuthority();

        // 토큰 생성
        String accessToken = jwtTokenProvider.createAccessToken(loginId, role);
        String refreshToken = jwtTokenProvider.createRefreshToken(loginId);

        // Refresh Token 쿠키 설정
        jwtTokenProvider.setRefreshTokenInCookie(response, refreshToken);

        // 공통 규격(ResultDTO)으로 응답 생성
        ResultDTO<TokenResponse> result = ResultDTO.success(new TokenResponse(accessToken), "login-success");

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        ResultDTO<Void> result = ResultDTO.error("AUTH_001", "인증 실패: " + failed.getMessage(), "login-fail");
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
