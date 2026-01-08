package com.lmkhanhs.home_net.services;

import java.text.ParseException;
import java.util.Date;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lmkhanhs.home_net.dtos.auth.requests.CreatePermissionRequest;
import com.lmkhanhs.home_net.dtos.auth.requests.LoginRequest;
import com.lmkhanhs.home_net.dtos.auth.requests.LogoutRequest;
import com.lmkhanhs.home_net.dtos.auth.responses.LoginResponse;
import com.lmkhanhs.home_net.dtos.auth.responses.LogoutResponse;
import com.lmkhanhs.home_net.dtos.auth.responses.PermissionResponse;
import com.lmkhanhs.home_net.dtos.users.requests.CreateUserRequest;
import com.lmkhanhs.home_net.dtos.users.responses.UserResponse;
import com.lmkhanhs.home_net.entities.PermissionEntity;
import com.lmkhanhs.home_net.entities.RoleEntity;
import com.lmkhanhs.home_net.entities.TenantEntity;
import com.lmkhanhs.home_net.entities.UserEntity;
import com.lmkhanhs.home_net.exceptions.AppException;
import com.lmkhanhs.home_net.exceptions.ErrorCode;
import com.lmkhanhs.home_net.mappers.AuthMapper;
import com.lmkhanhs.home_net.mappers.UserMapper;
import com.lmkhanhs.home_net.repositories.PermissionRepository;
import com.lmkhanhs.home_net.repositories.RoleRepository;
import com.lmkhanhs.home_net.repositories.TenantRepository;
import com.lmkhanhs.home_net.repositories.UserRepository;
import com.lmkhanhs.home_net.utils.BaseRedisUtils;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AuthenticationService {

    final BaseRedisUtils baseRedisUtils;
    final PermissionRepository permissionRepository;
    final AuthMapper authMapper;
    final UserRepository userRepository;
    final TenantRepository tenantRepository;
    final UserMapper userMapper;
    final RoleRepository roleRepository;
    final PasswordEncoder passwordEncoder;

    @Value("${app.security.expiresIn}")
    Integer expiresIn;

    @Value("${app.security.expiresRefresh}")
    Integer expiresRefresh;

    @Value("${app.security.secret}")
    String SECRET;

    private static final String TOKEN_PREFIX = "BLACKLIST:TOKEN:";

    public PermissionResponse handleCreatePermission(String tenantId, CreatePermissionRequest request) {

        permissionRepository.findByName(request.getName())
                .ifPresent(p -> {
                    throw new AppException(ErrorCode.PERMISSION_EXISTED);
                });

        PermissionEntity permission = authMapper.toEntity(request);
        permission.setTenantId(tenantId);

        return authMapper.toResponse(permissionRepository.save(permission));
    }

    public UserResponse handRegister(String tenantId, CreateUserRequest request) {

        if (userRepository.existsByUsernameAndTenantId(request.getUsername(), tenantId)) {
            throw new AppException(
                    ErrorCode.USERNAME_EXISTED,
                    "Please use another username!"
            );
        }

        RoleEntity userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new AppException(
                        ErrorCode.ROLE_NOT_FOUND,
                        "Role USER not found!"
                ));

        UserEntity user = userMapper.toEntity(request);
        user.setAvatarUrl("http://localhost:8088/images/default.jpg");
        user.setIsActive(true);
        user.setTenantId(tenantId);
        user.setRoles(Set.of(userRole));
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        return userMapper.toResponse(user);
    }


    public LoginResponse handleLogin(String tenantId, LoginRequest request) {

        UserEntity user = userRepository
                .findByUsernameAndTenantId(request.getUsername(), tenantId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "User not found with username: " + request.getUsername()));

        if (user.getPassword() == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = generateToken(
                tenantId,
                user.getUsername(),
                true
        );

        String refreshToken = generateToken(
                tenantId,
                user.getUsername(),
                false
        );

        return LoginResponse.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .expiresIn(expiresIn)
                .refreshToken(refreshToken)
                .build();
    }
    private String generateToken(
            String tenantId,
            String username,
            boolean isAccessToken
    ) {
        Date now = new Date();
        long durationMillis = isAccessToken
                ? expiresIn * 1000L
                : expiresRefresh * 1000L;

        Date expiration = new Date(now.getTime() + durationMillis);

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet.Builder claims = new JWTClaimsSet.Builder()
                .issuer(tenantId)
                .subject(username)
                .jwtID(UUID.randomUUID().toString())
                .issueTime(now)
                .expirationTime(expiration);

        if (isAccessToken) {
            claims.claim("token-type", "access")
                  .claim("scope", buildScope(tenantId, username));
        } else {
            claims.claim("token-type", "refresh");
        }

        SignedJWT jwt = new SignedJWT(header, claims.build());

        try {
            JWSSigner signer = new MACSigner(SECRET.getBytes());
            jwt.sign(signer);
        } catch (JOSEException e) {
            throw new RuntimeException("ERROR SIGNING JWT", e);
        }

        return jwt.serialize();
    }

    private String buildScope(String tenantId, String username) {

        UserEntity user = userRepository
                .findByUsernameAndTenantId(username, tenantId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            return "";
        }

        StringJoiner joiner = new StringJoiner(" ");

        for (RoleEntity role : user.getRoles()) {
            if (role.getPermissions() == null) continue;

            for (PermissionEntity permission : role.getPermissions()) {
                joiner.add(permission.getName());
            }
        }

        return joiner.toString();
    }
    public boolean introspectToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            boolean verified = signedJWT.verify(new MACVerifier(SECRET.getBytes()));

            JWTClaimsSet  claimsSet = signedJWT.getJWTClaimsSet();
            String jwtID = claimsSet.getJWTID();
            Date expires = claimsSet.getExpirationTime();
            Date now = new Date();
            boolean checkBlacllist = checkTokenInBlackList(jwtID);
            if (expires == null || verified == false || now.after(expires) || checkBlacllist == true) {
                return false;
            }
            return true;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean checkTokenInBlackList(String jwtID) {
        Object result = this.baseRedisUtils.getForString(this.TOKEN_PREFIX+jwtID);
        return  Boolean.TRUE.equals(result);
    }
    public void addBlacklist(String token) {
        this.baseRedisUtils.set(this.TOKEN_PREFIX + this.jwtID(token),true, this.timeToLongToken(token), TimeUnit.SECONDS);
    }
    private long timeToLongToken(String token) {
        SignedJWT signedJWT = null;
        try {
            signedJWT = SignedJWT.parse(token);
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            Date expirationTime = jwtClaimsSet.getExpirationTime();
            Date now = new Date();
            long durationInMillis = expirationTime.getTime() - now.getTime();
            return durationInMillis / 1000;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    private String jwtID (String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            return  jwtClaimsSet.getJWTID();
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public LogoutResponse handLogout(LogoutRequest logoutRequest) {
        String accessToken = logoutRequest.getAccessToken();
        String refreshToken = logoutRequest.getRefreshToken();
        this.baseRedisUtils.set(this.TOKEN_PREFIX + this.jwtID(accessToken),true, this.timeToLongToken(accessToken), TimeUnit.SECONDS);
        this.baseRedisUtils.set(this.TOKEN_PREFIX + this.jwtID(refreshToken),true, this.timeToLongToken(refreshToken), TimeUnit.SECONDS);

        return LogoutResponse.builder()
                .success(true)
                .build();
    }
}
