package vn.com.greencraze.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import vn.com.greencraze.auth.dto.request.identity.AuthenticateRequest;
import vn.com.greencraze.auth.dto.request.identity.ForgotPasswordRequest;
import vn.com.greencraze.auth.dto.request.identity.GoogleAuthRequest;
import vn.com.greencraze.auth.dto.request.identity.RefreshTokenRequest;
import vn.com.greencraze.auth.dto.request.identity.RegisterRequest;
import vn.com.greencraze.auth.dto.request.identity.ResendOTPRequest;
import vn.com.greencraze.auth.dto.request.identity.ResetPasswordRequest;
import vn.com.greencraze.auth.dto.request.identity.VerifyOTPRequest;
import vn.com.greencraze.auth.dto.response.identity.AuthenticateResponse;
import vn.com.greencraze.auth.dto.response.identity.ForgotPasswordResponse;
import vn.com.greencraze.auth.dto.response.identity.GoogleAuthResponse;
import vn.com.greencraze.auth.dto.response.identity.RefreshTokenResponse;
import vn.com.greencraze.auth.dto.response.identity.RegisterResponse;
import vn.com.greencraze.auth.dto.response.identity.ResendOTPResponse;
import vn.com.greencraze.auth.dto.response.identity.ResetPasswordResponse;
import vn.com.greencraze.auth.dto.response.identity.VerifyOTPResponse;
import vn.com.greencraze.auth.service.IAuthService;
import vn.com.greencraze.commons.api.RestResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/auths")
@Tag(name = "auth :: Auth")
@RequiredArgsConstructor
public class AuthController {

    private final IAuthService authService;

    @PostMapping(
            value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login with google")
    public ResponseEntity<RestResponse<GoogleAuthResponse>> authenticateWithGoogle(
            @RequestBody @Valid GoogleAuthRequest request
    ) throws GeneralSecurityException, IOException {
        return ResponseEntity.ok(authService.authenticateWithGoogle(request));
    }

    @PostMapping(
            value = "/google-login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Login")
    public ResponseEntity<RestResponse<AuthenticateResponse>> authenticate(
            @RequestBody @Valid AuthenticateRequest request
    ) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping(
            value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Register")
    public ResponseEntity<RestResponse<RegisterResponse>> register(@RequestBody @Valid RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PutMapping(
            value = "/register/verify",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Verify otp register")
    public ResponseEntity<RestResponse<VerifyOTPResponse>> verifyOTP(@RequestBody @Valid VerifyOTPRequest request) {
        return ResponseEntity.ok(authService.verifyOTP(request));
    }

    @PutMapping(
            value = "/register/resend"
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Resend otp register")
    public ResponseEntity<RestResponse<ResendOTPResponse>> resendRegisterOTP(
            @RequestBody @Valid ResendOTPRequest request
    ) {
        return ResponseEntity.ok(authService.resendOTP(request));
    }

    @PostMapping(
            value = "/refresh-token",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Refresh token")
    public ResponseEntity<RestResponse<RefreshTokenResponse>> refreshToken(
            @RequestBody @Valid RefreshTokenRequest request
    ) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PutMapping(
            value = "/forgot-password"
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Forgot password")
    public ResponseEntity<RestResponse<ForgotPasswordResponse>> forgotPassword(
            @RequestBody @Valid ForgotPasswordRequest request
    ) {
        return ResponseEntity.ok(authService.forgotPassword(request));
    }

    @PutMapping(
            value = "/reset-password"
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Reset password")
    public ResponseEntity<RestResponse<ResetPasswordResponse>> resetPassword(
            @RequestBody @Valid ResetPasswordRequest request
    ) {
        return ResponseEntity.ok(authService.resetPassword(request));
    }

    @PutMapping(
            value = "/forgot-password/resend"
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Resend otp forgot password")
    public ResponseEntity<RestResponse<ResendOTPResponse>> resendForgotPasswordOTP(
            @RequestBody @Valid ResendOTPRequest request
    ) {
        return ResponseEntity.ok(authService.resendOTP(request));
    }

}
