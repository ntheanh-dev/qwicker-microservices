package com.nta.identity.service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nta.identity.dto.request.AuthenticationRequest;
import com.nta.identity.dto.request.IntrospectRequest;
import com.nta.identity.dto.request.LogoutRequest;
import com.nta.identity.dto.request.RefreshRequest;
import com.nta.identity.dto.response.AuthenticationResponse;
import com.nta.identity.dto.response.IntrospectResponse;
import com.nta.identity.entity.InvalidatedToken;
import com.nta.identity.entity.User;
import com.nta.identity.exception.AppException;
import com.nta.identity.exception.ErrorCode;
import com.nta.identity.repository.InvalidatedTokenRepository;
import com.nta.identity.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.SIGNER_KEY}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    public IntrospectResponse introspect(final IntrospectRequest request) throws JOSEException, ParseException {
        final var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    public AuthenticationResponse authenticate(final AuthenticationRequest request) {
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        final var user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        final boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

        final var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token.token)
                .expiryTime(token.expiryDate)
                .build();
    }

    public void logout(final LogoutRequest request) throws ParseException, JOSEException {
        try {
            final var signToken = verifyToken(
                    request.getToken(),
                    true); // cần check thời gian theo refresh token đảm bảo token luôn được persit xuống database tránh
            // trường hợp token được logout nhưng vẫn refresh token mới được
            final String jit = signToken.getJWTClaimsSet().getJWTID();
            final Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            final InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException e) {
            log.info("Token already expired");
        }
    }

    public AuthenticationResponse refreshToken(final RefreshRequest request) throws ParseException, JOSEException {
        final var signedJWT = verifyToken(request.getToken(), true);

        final var jit = signedJWT.getJWTClaimsSet().getJWTID();
        final var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        final InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        final var username = signedJWT.getJWTClaimsSet().getSubject();

        final var user =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        final var token = generateToken(user);

        return AuthenticationResponse.builder()
                .token(token.token)
                .expiryTime(token.expiryDate)
                .build();
    }

    private TokenInfo generateToken(final User user) {
        final JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        final Date expiryTime = new Date(Instant.ofEpochMilli(Instant.now().getEpochSecond() + VALID_DURATION)
                .plus(VALID_DURATION, ChronoUnit.DAYS)
                .toEpochMilli());
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("nta.com")
                .issueTime(new Date())
                .expirationTime(expiryTime)
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .claim("userId", user.getId())
                .build();

        final Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        final JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return new TokenInfo(jwsObject.serialize(), expiryTime);
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
    }

    private SignedJWT verifyToken(final String token, final boolean isRefresh) throws JOSEException, ParseException {
        final JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        final SignedJWT signedJWT = SignedJWT.parse(token);

        final Date expiryTime = isRefresh
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(REFRESHABLE_DURATION, ChronoUnit.DAYS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        final var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    private String buildScope(final User user) {
        final StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }

    private record TokenInfo(String token, Date expiryDate) {}
}
