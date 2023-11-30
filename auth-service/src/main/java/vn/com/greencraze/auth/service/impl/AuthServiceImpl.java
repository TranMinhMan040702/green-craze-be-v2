package vn.com.greencraze.auth.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.com.greencraze.auth.config.property.AppProperties;
import vn.com.greencraze.auth.config.security.JwtManager;
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
import vn.com.greencraze.auth.entity.Identity;
import vn.com.greencraze.auth.entity.IdentityToken;
import vn.com.greencraze.auth.entity.view.UserProfileView;
import vn.com.greencraze.auth.enumeration.IdentityStatus;
import vn.com.greencraze.auth.enumeration.RoleCode;
import vn.com.greencraze.auth.enumeration.TokenType;
import vn.com.greencraze.auth.exception.ActiveUserException;
import vn.com.greencraze.auth.exception.BlockedUserException;
import vn.com.greencraze.auth.exception.ExistedUserException;
import vn.com.greencraze.auth.exception.InactivatedUserException;
import vn.com.greencraze.auth.exception.InvalidPasswordException;
import vn.com.greencraze.auth.exception.TokenValidationException;
import vn.com.greencraze.auth.exception.UnconfirmedUserException;
import vn.com.greencraze.auth.repository.IdentityRepository;
import vn.com.greencraze.auth.repository.RoleRepository;
import vn.com.greencraze.auth.repository.view.UserProfileViewRepository;
import vn.com.greencraze.auth.service.IAuthService;
import vn.com.greencraze.auth.util.OtpHelper;
import vn.com.greencraze.auth.util.RefreshTokenHelper;
import vn.com.greencraze.commons.api.RestResponse;
import vn.com.greencraze.commons.exception.ResourceNotFoundException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final IdentityRepository identityRepository;
    private final RoleRepository roleRepository;
    private final UserProfileViewRepository userProfileViewRepository;

    private final JwtManager jwtManager;

    private final PasswordEncoder passwordEncoder;

    private final AppProperties appProperties;

    @Builder
    private record GenerateAuthCredentialDto(
            String accessToken,
            String refreshToken
    ) {}

    private GenerateAuthCredentialDto GenerateAuthCredential(Identity identity) {
        String accessToken = jwtManager.generateToken(identity);
        String refreshToken = RefreshTokenHelper.createRefreshToken();
        Instant refreshTokenExpiredTime = Instant.now().plusMillis(appProperties.refreshTokenExpirationMillis());

        IdentityToken identityToken = identity.getIdentityTokens().stream()
                .filter(token -> Objects.equals(token.getToken(), TokenType.REFRESH_TOKEN.toString()))
                .findFirst()
                .orElse(null);

        if (identityToken == null) {
            identity.getIdentityTokens().add(IdentityToken.builder()
                    .identity(identity)
                    .token(refreshToken)
                    .expiredAt(refreshTokenExpiredTime)
                    .type(TokenType.REFRESH_TOKEN)
                    .build());
        } else {
            identityToken.setToken(refreshToken);
            identityToken.setExpiredAt(refreshTokenExpiredTime);
        }

        identityRepository.save(identity);

        return GenerateAuthCredentialDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public RestResponse<AuthenticateResponse> authenticate(AuthenticateRequest request) {
        // Step 1: get user with email
        Identity identity = identityRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", request.username()));

        // Step 2: validation password
        boolean isValidatePassword = passwordEncoder.matches(request.password(), identity.getPassword());

        if (!isValidatePassword) {
            throw new InvalidPasswordException();
        }

        // Step 3: check status of user
        switch (identity.getStatus()) {
            case BLOCK -> throw new BlockedUserException();
            case INACTIVE -> throw new InactivatedUserException();
            case UNCONFIRMED -> {
                // TODO: Send OTP
                throw new UnconfirmedUserException();
            }
        }

        // Step 4: Create access token and refresh token
        GenerateAuthCredentialDto generateAuthCredentialDto = GenerateAuthCredential(identity);

        return RestResponse.ok(AuthenticateResponse.builder()
                .accessToken(generateAuthCredentialDto.accessToken)
                .refreshToken(generateAuthCredentialDto.refreshToken)
                .build());
    }

    @Override
    public RestResponse<ValidateAccessTokenResponse> validateAccessToken(ValidateAccessTokenRequest request) {
        DecodedJWT decodedJWT = jwtManager.validateToken(request.accessToken());

        String userId = decodedJWT.getClaim("userId").as(String.class);
        UserProfileView userProfileView = userProfileViewRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Identity identity = userProfileView.getIdentity();
        List<String> userAuthorities = identity.getRoles().stream().map(role -> "ROLE_" + role.getName()).toList();

        return RestResponse.ok(
                ValidateAccessTokenResponse.builder()
                        .userId(userId)
                        .userAuthorities(userAuthorities)
                        .build()
        );
    }

    @Override
    public RestResponse<GoogleAuthResponse> authenticateWithGoogle(GoogleAuthRequest googleAuthRequest)
            throws GeneralSecurityException, IOException {
        // Step 1: verify google token
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                .Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(appProperties.clientId()))
                .build();
        GoogleIdToken idToken = verifier.verify(googleAuthRequest.googleToken());
        if (idToken == null) {
            throw new TokenValidationException("Invalid id google token");
        }

        // Step 2: get payload
        var payload = idToken.getPayload();
        Optional<Identity> identityOption = identityRepository.findByUsername(payload.getEmail());

        Identity identity;

        if (identityOption.isPresent()) {
            identity = identityOption.get();
            switch (identity.getStatus()) {
                case BLOCK -> throw new BlockedUserException();
                case INACTIVE -> throw new InactivatedUserException();
            }
        } else {
            identity = Identity.builder()
                    .username(payload.getEmail())
                    .password(passwordEncoder.encode(payload.getSubject()))
                    .status(IdentityStatus.ACTIVE)
                    .roles(Set.of(roleRepository.getReferenceByCode(RoleCode.USER.toString())))
                    .build();
            // TODO: call user service create user
        }

        GenerateAuthCredentialDto generateAuthCredentialDto = GenerateAuthCredential(identity);

        return RestResponse.ok(GoogleAuthResponse.builder()
                .accessToken(generateAuthCredentialDto.accessToken)
                .refreshToken(generateAuthCredentialDto.refreshToken)
                .build());
    }

    @Override
    public RestResponse<RegisterResponse> register(RegisterRequest request) {
        // Step 1: get identity with email
        Optional<Identity> identityOption = identityRepository.findByUsername(request.email());
        Identity identity;

        // Step 2: check exist identity
        if (identityOption.isPresent()) {
            identity = identityOption.get();
            switch (identity.getStatus()) {
                case INACTIVE -> throw new InactivatedUserException();
                case UNCONFIRMED -> throw new UnconfirmedUserException();
                case ACTIVE -> throw new ExistedUserException();
                case BLOCK -> throw new BlockedUserException();
            }
        } else {
            String otp = OtpHelper.createOtp();
            identity = Identity.builder()
                    .username(request.email())
                    .password(passwordEncoder.encode(request.password()))
                    .status(IdentityStatus.UNCONFIRMED)
                    .roles(Set.of(roleRepository.getReferenceByCode(RoleCode.USER.toString())))
                    .build();
            identity.setIdentityTokens(List.of(IdentityToken.builder()
                    .identity(identity)
                    .token(otp)
                    .type(TokenType.REGISTER_OTP)
                    .expiredAt(Instant.now().plusMillis(appProperties.otpTokenExpirationMillis()))
                    .build()));
            identityRepository.save(identity);
            // TODO: call user service create user
            // TODO: Send email otp
        }

        return RestResponse.ok(RegisterResponse.builder()
                .userId(identity.getId())
                .build());
    }

    @Override
    public RestResponse<RefreshTokenResponse> refreshToken(RefreshTokenRequest request) {
        // Step 1: validate token
        DecodedJWT decodedJWT = jwtManager.validateToken(request.accessToken());
        String identityId = decodedJWT.getClaim("UserId").as(String.class);

        // Step 2: find identity with identityId in token, then get identityToken with type is REFRESH_TOKEN
        Identity identity = identityRepository.findById(identityId)
                .orElseThrow(() -> new ResourceNotFoundException("Identity", "id", identityId));

        IdentityToken identityToken = identity.getIdentityTokens().stream()
                .filter(token -> Objects.equals(token.getToken(), TokenType.REFRESH_TOKEN.toString()))
                .findFirst()
                .orElse(null);

        // Step 3: validate refresh token
        if (identityToken == null || !Objects.equals(identityToken.getToken(), request.refreshToken())) {
            throw new TokenValidationException("Invalid refresh token");
        }

        if (identityToken.getExpiredAt().toEpochMilli() <= Instant.now().toEpochMilli()) {
            throw new TokenValidationException("Expired refresh token");
        }

        // Step 4: create new access, refresh token
        String accessToken = jwtManager.generateToken(identity);
        String refreshToken = RefreshTokenHelper.createRefreshToken();
        Instant refreshTokenExpiredTime = Instant.now().plusMillis(appProperties.refreshTokenExpirationMillis());

        identityToken.setToken(refreshToken);
        identityToken.setExpiredAt(refreshTokenExpiredTime);

        identityRepository.save(identity);

        return RestResponse.ok(RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
    }

    @Override
    public RestResponse<VerifyOTPResponse> verifyOTP(VerifyOTPRequest request) {
        // Step 1: find identity with email
        Identity identity = identityRepository.findByUsername(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("Identity", "username", request.email()));

        // Step 2: get otp of identity with request otp
        IdentityToken identityToken = identity.getIdentityTokens().stream()
                .filter(token -> Objects.equals(token.getToken(), request.type().toString()))
                .findFirst()
                .orElse(null);

        // Step 3: validation otp
        if (identityToken == null || !Objects.equals(identityToken.getToken(), request.OTP())) {
            throw new TokenValidationException("Invalid OTP");
        }

        if (identityToken.getExpiredAt().toEpochMilli() <= Instant.now().toEpochMilli()) {
            throw new TokenValidationException("Expired OTP");
        }

        // Step 4: if verify with REGISTER_OTP then update identity status (ACTIVE)
        if (request.type() == TokenType.REGISTER_OTP) {
            identity.setStatus(IdentityStatus.ACTIVE);
        }

        // Step 5: set default otp
        identityToken.setToken(null);
        identityToken.setExpiredAt(null);

        identityRepository.save(identity);

        return RestResponse.ok(VerifyOTPResponse.builder()
                .isSuccess(true)
                .build());
    }

    @Override
    public RestResponse<ResendOTPResponse> resendOTP(ResendOTPRequest request) {
        // Step 1: find identity with email
        Identity identity = identityRepository.findByUsername(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("Identity", "username", request.email()));

        // Step 2: get token with type of request
        IdentityToken identityToken = identity.getIdentityTokens().stream()
                .filter(token -> Objects.equals(token.getToken(), request.type().toString()))
                .findFirst()
                .orElse(null);

        // Step 3: validate token
        if (identityToken == null) {
            throw new TokenValidationException("Otp invalid");
        }

        if (request.type() == TokenType.REGISTER_OTP && identity.getStatus() == IdentityStatus.ACTIVE) {
            throw new ActiveUserException();
        }

        // Step 4: create new token otp
        String otp = OtpHelper.createOtp();
        identityToken.setToken(otp);
        identityToken.setExpiredAt(Instant.now().plusMillis(appProperties.otpTokenExpirationMillis()));

        identityRepository.save(identity);

        // Step 5: send email for user (TODO)

        return RestResponse.ok(ResendOTPResponse.builder()
                .isSuccess(true)
                .build());
    }

    @Override
    public RestResponse<ForgotPasswordResponse> forgotPassword(ForgotPasswordRequest request) {
        // Step 1: find identity with email
        Identity identity = identityRepository.findByUsername(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("Identity", "username", request.email()));

        if (identity.getStatus() == IdentityStatus.UNCONFIRMED) {
            throw new UnconfirmedUserException();
        }

        // Step 2: get token with type FORGOT_PASSWORD_OTP
        IdentityToken identityToken = identity.getIdentityTokens().stream()
                .filter(token -> Objects.equals(token.getToken(), TokenType.FORGOT_PASSWORD_OTP.toString()))
                .findFirst()
                .orElse(null);

        String otp = OtpHelper.createOtp();
        Instant otpTokenExpiredTime = Instant.now().plusMillis(appProperties.otpTokenExpirationMillis());

        if (identityToken == null) {
            identity.setIdentityTokens(List.of(IdentityToken.builder()
                    .identity(identity)
                    .token(otp)
                    .expiredAt(otpTokenExpiredTime)
                    .type(TokenType.FORGOT_PASSWORD_OTP)
                    .build()));
        } else {
            identityToken.setToken(otp);
            identityToken.setExpiredAt(otpTokenExpiredTime);
        }

        identityRepository.save(identity);

        // Step 3: send email

        return RestResponse.ok(ForgotPasswordResponse.builder()
                .isSuccess(true)
                .build());
    }

    @Override
    public RestResponse<ResetPasswordResponse> resetPassword(ResetPasswordRequest request) {
        // Step 1: verify otp forgot password
        verifyOTP(VerifyOTPRequest.builder()
                .email(request.email())
                .OTP(request.OTP())
                .type(request.type())
                .build());

        // Step 2: find identity and set new password
        Identity identity = identityRepository.findByUsername(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("Identity", "username", request.email()));
        identity.setPassword(passwordEncoder.encode(request.password()));

        identityRepository.save(identity);

        return RestResponse.ok(ResetPasswordResponse.builder()
                .isSuccess(true)
                .build());
    }

}
