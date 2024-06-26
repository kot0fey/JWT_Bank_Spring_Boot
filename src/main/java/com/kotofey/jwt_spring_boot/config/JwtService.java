package com.kotofey.jwt_spring_boot.config;

import com.kotofey.jwt_spring_boot.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    private static final String SECRET_KEY = "1E1BF53C9601987114CF428BCBBAE1130983CFDC3A866C8FD881D9F150A38F86";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Map<String, Object> extractExtraClaims(String token) {
        Map<String, Object> allClaims = extractClaim(token, HashMap::new);
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("email", allClaims.get("email"));
        extraClaims.put("phoneNumber", allClaims.get("phoneNumber"));
        extraClaims.put("login", allClaims.get("login"));
        return extraClaims;
    }

    public String generateToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails
    ) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5))
                .signWith(getSingInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Map<String, Object> generateExtraClaims(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("login", user.getLogin());
        extraClaims.put("email", user.getEmail());
        extraClaims.put("phoneNumber", user.getPhoneNumber());
        return extraClaims;
    }

    public String generateToken(
            UserDetails userDetails
    ) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSingInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Key getSingInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsername(String jwtToken) {
        Map<String, Object> extraClaims = extractExtraClaims(jwtToken);
        if (!extraClaims.get("email").equals("")) {
            return (String) extraClaims.get("email");
        } else if (!extraClaims.get("phoneNumber").equals("")) {
            return (String) extraClaims.get("phoneNumber");
        } else if (!extraClaims.get("login").equals("")) {
            return (String) extraClaims.get("login");
        }
        return null;
    }
}
