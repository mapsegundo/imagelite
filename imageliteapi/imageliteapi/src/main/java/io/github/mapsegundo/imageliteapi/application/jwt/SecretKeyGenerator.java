package io.github.mapsegundo.imageliteapi.application.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

/**
 * A classe `SecretKeyGenerator` é um componente do Spring responsável por gerar e fornecer uma chave secreta (`SecretKey`)
 * para uso em operações criptográficas, como a geração de tokens JWT.
 */
@Component
public class SecretKeyGenerator {

    /**
     * Armazena a chave secreta gerada.
     */
    private SecretKey key;

    /**
     * Retorna a chave secreta. Se a chave ainda não tiver sido gerada (ou seja, se `key` for `null`), o método a gera
     * utilizando a biblioteca `Jwts` e o algoritmo HS256.
     *
     * @return a chave secreta gerada ou existente.
     */
    public SecretKey getKey() {
        if(key == null) {
            key = Jwts.SIG.HS256.key().build();
        }
        return key;
    }
}