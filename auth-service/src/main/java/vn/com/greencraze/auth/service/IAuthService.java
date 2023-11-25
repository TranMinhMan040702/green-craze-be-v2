package vn.com.greencraze.auth.service;

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
import vn.com.greencraze.commons.api.RestResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface IAuthService {

    RestResponse<AuthenticateResponse> authenticate(AuthenticateRequest request);

    RestResponse<GoogleAuthResponse> authenticateWithGoogle(GoogleAuthRequest googleAuthRequest) throws GeneralSecurityException, IOException;

    RestResponse<RegisterResponse> register(RegisterRequest request);

    RestResponse<RefreshTokenResponse> refreshToken(RefreshTokenRequest request);

    RestResponse<VerifyOTPResponse> verifyOTP(VerifyOTPRequest request);

    RestResponse<ResendOTPResponse> resendOTP(ResendOTPRequest request);

    RestResponse<ForgotPasswordResponse> forgotPassword(ForgotPasswordRequest request);

    RestResponse<ResetPasswordResponse> resetPassword(ResetPasswordRequest request);

}
