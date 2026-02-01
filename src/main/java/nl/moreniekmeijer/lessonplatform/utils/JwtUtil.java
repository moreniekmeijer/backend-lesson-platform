package nl.moreniekmeijer.lessonplatform.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import nl.moreniekmeijer.lessonplatform.config.CustomUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-expiration}")
    private long accessExpirationTime;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpirationTime;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String generateAccessToken(UserDetails userDetails) {
        CustomUserDetails cud = (CustomUserDetails) userDetails;

        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        claims.put("email", cud.getUsername());
        claims.put("roles", cud.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        return createToken(claims, cud.getId().toString(), accessExpirationTime);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        CustomUserDetails cud = (CustomUserDetails) userDetails;

        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");

        return createToken(
                claims,
                cud.getId().toString(),
                refreshExpirationTime
        );
    }

    private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey() , SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateAccessToken(String token, UserDetails userDetails) {
        final String subject = extractSubject(token);

        if (!(userDetails instanceof CustomUserDetails cud)) {
            return false;
        }

        Claims claims = extractAllClaims(token);

        return subject.equals(cud.getId().toString())
                && "access".equals(claims.get("type"))
                && !claims.getExpiration().before(new Date());
    }

    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            if (!"refresh".equals(claims.get("type"))) {
                return false;
            }

            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}