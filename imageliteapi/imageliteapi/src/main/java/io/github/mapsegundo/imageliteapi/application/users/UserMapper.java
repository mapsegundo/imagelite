package io.github.mapsegundo.imageliteapi.application.users;

import io.github.mapsegundo.imageliteapi.application.users.dto.UserDTO;
import io.github.mapsegundo.imageliteapi.domain.entity.User;
import org.springframework.stereotype.Component;

/**
 * Componente responsável por mapear entre a entidade User e o DTO UserDTO.
 */
@Component
public class UserMapper {

    /**
     * Converte uma entidade User para um UserDTO.
     *
     * @param user a entidade User a ser convertida
     * @return o UserDTO resultante da conversão
     */
    public UserDTO toDTO(User user) {
        return UserDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    /**
     * Converte um UserDTO para uma entidade User.
     *
     * @param dto o UserDTO a ser convertido
     * @return a entidade User resultante da conversão
     */
    public User toEntity(UserDTO dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }
}