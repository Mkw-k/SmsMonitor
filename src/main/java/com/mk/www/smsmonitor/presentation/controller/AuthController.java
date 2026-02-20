package com.mk.www.smsmonitor.presentation.controller;

import com.mk.www.smsmonitor.application.service.AuthService;
import com.mk.www.smsmonitor.config.JwtTokenProvider;
import com.mk.www.smsmonitor.presentation.dto.RegisterRequest;
import com.mk.www.smsmonitor.presentation.dto.ResultDTO;
import com.mk.www.smsmonitor.presentation.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "인증 및 회원 관리 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "회원가입", description = "사용자가 회원가입을 한다.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "가입 성공", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "회원가입에 적합하지 않는 회원일 경우",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(example = "{\"status\": 400, \"message\": \"회원가입에 적합하지 않는 회원입니다.\", \"data\": null}"))),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 존재하는 회원 ID가 있을 경우",
                            content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(example = "{\"status\": 409, \"message\": \"이미 존재하는 회원입니다.\", \"data\": null}")))
            })
    @PostMapping("/register")
    public ResponseEntity<ResultDTO<Void>> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(summary = "토큰 갱신", description = "쿠키의 Refresh Token을 이용해 Access Token 갱신")
    @PostMapping("/refresh")
    public ResponseEntity<ResultDTO<TokenResponse>> refresh(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.getRefreshTokenFromCookie(request);
        return ResponseEntity.ok(authService.refresh(refreshToken));
    }
}
