package io.github.mapsegundo.imageliteapi.application.users;

import io.github.mapsegundo.imageliteapi.application.users.dto.CredentialsDTO;
import io.github.mapsegundo.imageliteapi.application.users.dto.UserDTO;
import io.github.mapsegundo.imageliteapi.domain.entity.User;
import io.github.mapsegundo.imageliteapi.domain.exception.DuplicatedTupleException;
import io.github.mapsegundo.imageliteapi.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controlador REST para gerenciar usuários.
 */
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Endpoint para salvar um novo usuário.
     *
     * @param dto o DTO do usuário a ser salvo
     * @return ResponseEntity com o status HTTP CREATED
     */
    @PostMapping
    public ResponseEntity save(@RequestBody UserDTO dto) {
        try {
            User user = userMapper.toEntity(dto);
            userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (DuplicatedTupleException e) {
            Map<String, String> jsonResponseBody = Map.of("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(jsonResponseBody);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Endpoint para autenticar um usuário.
     *
     * @param dto o DTO contendo as credenciais do usuário
     * @return ResponseEntity com o token de autenticação se bem-sucedido, ou status HTTP UNAUTHORIZED se falhar
     */
    @PostMapping("/auth")
    public ResponseEntity authenticate(@RequestBody CredentialsDTO dto) {
        try {
            var token = userService.authenticationToken(dto.getEmail(), dto.getPassword());
            return ResponseEntity.ok(token);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}