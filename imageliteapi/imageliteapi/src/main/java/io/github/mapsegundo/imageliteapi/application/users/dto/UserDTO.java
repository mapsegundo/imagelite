package io.github.mapsegundo.imageliteapi.application.users.dto;

import lombok.Builder;
import lombok.Data;

/**
 * Classe DTO para transferência de dados do usuário.
 * Utiliza Lombok para gerar automaticamente getters, setters, e métodos de construção.
 */
@Data
@Builder
public class UserDTO {

    /**
     * Nome do usuário.
     */
    private String name;

    /**
     * Email do usuário.
     */
    private String email;

    /**
     * Senha do usuário.
     */
    private String password;

}