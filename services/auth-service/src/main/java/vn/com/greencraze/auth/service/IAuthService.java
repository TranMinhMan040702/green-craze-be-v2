package vn.com.greencraze.auth.service;

import vn.com.greencraze.auth.dto.request.auth.AuthenticateRequest;
import vn.com.greencraze.auth.dto.request.auth.ForgotPasswordRequest;
import vn.com.greencraze.auth.dto.request.auth.GoogleAuthRequest;
import vn.com.greencraze.auth.dto.request.auth.RefreshTokenRequest;
import vn.com.greencraze.auth.dto.request.auth.RegisterRequest;
import vn.com.greencraze.auth.dto.request.auth.ResendOTPRequest;
import vn.com.greencraze.auth.dto.request.auth.ResetPasswordRequest;
import vn.com.greencraze.auth.dto.request.auth.ValidateAccessTokenRequest;
import vn.com.greencraze.auth.dto.request.auth.VerifyOTPRequest;
import vn.com.greencraze.auth.dto.response.auth.AuthenticateResponse;
import vn.com.greencraze.auth.dto.response.auth.ForgotPasswordResponse;
import vn.com.greencraze.auth.dto.response.auth.GoogleAuthResponse;
import vn.com.greencraze.auth.dto.response.auth.RefreshTokenResponse;
import vn.com.greencraze.auth.dto.response.auth.RegisterResponse;
import vn.com.greencraze.auth.dto.response.auth.ResendOTPResponse;
import vn.com.greencraze.auth.dto.response.auth.ResetPasswordResponse;
import vn.com.greencraze.auth.dto.response.auth.ValidateAccessTokenResponse;
import vn.com.greencraze.auth.dto.response.auth.VerifyOTPResponse;
import vn.com.greencraze.commons.api.RestResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;

public interface IAuthService {

    RestResponse<AuthenticateResponse> authenticate(AuthenticateRequest request);

    RestResponse<ValidateAccessTokenResponse> validateAccessToken(ValidateAccessTokenRequest request);

    RestResponse<GoogleAuthResponse> authenticateWithGoogle(GoogleAuthRequest googleAuthRequest) throws GeneralSecurityException, IOException;

    RestResponse<RegisterResponse> register(RegisterRequest request);

    RestResponse<RefreshTokenResponse> refreshToken(RefreshTokenRequest request);

    RestResponse<VerifyOTPResponse> verifyOTP(VerifyOTPRequest request);

    RestResponse<ResendOTPResponse> resendOTP(ResendOTPRequest request);

    RestResponse<ForgotPasswordResponse> forgotPassword(ForgotPasswordRequest request);

    RestResponse<ResetPasswordResponse> resetPassword(ResetPasswordRequest request);

}
