package io.github.mapsegundo.imageliteapi.application.users.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Classe DTO para transferência de dados de credenciais do usuário.
 * Utiliza Lombok para gerar automaticamente getters, setters, e métodos de construção.
 */
@Data
@Builder
public class CredentialsDTO {

    /**
     * Email do usuário.
     */
    private String email;

    /**
     * Senha do usuário.
     */
    private String password;

}