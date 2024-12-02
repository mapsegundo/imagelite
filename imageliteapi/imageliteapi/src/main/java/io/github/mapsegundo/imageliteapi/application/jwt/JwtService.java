package io.github.mapsegundo.imageliteapi.application.jwt;

import io.github.mapsegundo.imageliteapi.domain.AccessToken;
import io.github.mapsegundo.imageliteapi.domain.entity.User;
import io.github.mapsegundo.imageliteapi.domain.exception.InvalidTokenException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A classe `JwtService` é responsável por gerar tokens JWT (JSON Web Tokens) para autenticação de usuários.
 */
@Service
@RequiredArgsConstructor
public class JwtService {

    private final SecretKeyGenerator keyGenerator;

    /**
     * Gera um token JWT para o usuário fornecido.
     *
     * @param user O usuário para o qual o token será gerado.
     * @return Um objeto `AccessToken` contendo o token gerado.
     */
    public AccessToken generateToken(User user) {

        var key = keyGenerator.getKey();
        var expirationTime = getExpirationTime();
        var claims = getClaims(user);

        String token = Jwts.builder()
                .subject(user.getEmail())
                .expiration(expirationTime)
                .claims(claims)
                .signWith(key)
                .compact();
        return new AccessToken(token);
    }

    /**
     * Calcula o tempo de expiração do token.
     *
     * @return A data de expiração do token.
     */
    private Date getExpirationTime() {
        // 60 minutos de duração
        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(60);
        return Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Cria um mapa de claims (informações) para o token JWT com base no usuário fornecido.
     *
     * @param user O usuário para o qual os claims serão gerados.
     * @return Um mapa contendo os claims do token.
     */
    private Map<String, Object> getClaims(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", user.getName());
        return claims;
    }

    public String getEmailFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(keyGenerator.getKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (JwtException e) {
            throw new InvalidTokenException(e.getMessage());
        }
    }
}