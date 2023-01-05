package com.faithfulolaleru.IdentityAPI.config.jwt;

import com.faithfulolaleru.IdentityAPI.appUser.AppUserEntity;
import com.faithfulolaleru.IdentityAPI.appUser.AppUserService;
import com.faithfulolaleru.IdentityAPI.dto.LoginResponse;
import com.faithfulolaleru.IdentityAPI.exception.ErrorResponse;
import com.faithfulolaleru.IdentityAPI.exception.GeneralException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public record JwtService(AppUserService appUserService) {

    private static final String SECRET_KEY = "504E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public LoginResponse generateToken(UserDetails userDetails) {
        // if you wanna generate token without extra claims
        // you can also generate token for AppUserEntity instead

        return generateToken(new HashMap<>(), userDetails);
    }

    public LoginResponse generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {  // if you wanna generate token with extra claims

        String token = Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))  // 24 hrs in milliseconds
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();

        final Instant expirationDateInMillis = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24).toInstant();
        final Instant expirationDateInMillis2 = extractExpiration(token).toInstant();

        return LoginResponse.builder()
                .token(token)
                .expiresIn(expirationDateInMillis)
                .build();
    }

    public LoginResponse generateToken(Authentication authentication) {
        AppUserEntity appUser = appUserService.findUserByEmail(authentication.getName());

        if(!appUser.isActive()) {
            throw new GeneralException(HttpStatus.FORBIDDEN, ErrorResponse.ERROR_APP_USER, "App User not active");
        }

        String token = Jwts
            .builder()
            //.setClaims(extraClaims)
            .setSubject(appUser.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))  // 24 hrs in milliseconds
            .signWith(getSignInKey(), SignatureAlgorithm.HS256)
            .compact();

        final Instant expirationDateInMillis = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24).toInstant();
        final Instant expirationDateInMillis2 = extractExpiration(token).toInstant();

        return LoginResponse.builder()
                .token(token)
                .expiresIn(expirationDateInMillis)
                .tokenFor(authentication.getName())
                .build();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
