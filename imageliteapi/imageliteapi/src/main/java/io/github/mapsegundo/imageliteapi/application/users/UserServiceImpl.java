package io.github.mapsegundo.imageliteapi.application.users;

import io.github.mapsegundo.imageliteapi.application.jwt.JwtService;
import io.github.mapsegundo.imageliteapi.domain.AccessToken;
import io.github.mapsegundo.imageliteapi.domain.entity.User;
import io.github.mapsegundo.imageliteapi.domain.exception.DuplicatedTupleException;
import io.github.mapsegundo.imageliteapi.domain.service.UserService;
import io.github.mapsegundo.imageliteapi.infra.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementação do serviço de usuários.
 * Esta classe fornece métodos para gerenciar usuários, incluindo salvar, buscar por email e autenticar.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Busca um usuário pelo email.
     *
     * @param email o email do usuário a ser buscado
     * @return o usuário encontrado ou null se não encontrado
     */
    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Salva um novo usuário no repositório.
     *
     * @param user o usuário a ser salvo
     * @return o usuário salvo
     * @throws IllegalArgumentException se o usuário ou seus campos obrigatórios forem nulos ou vazios
     * @throws DuplicatedTupleException se um usuário com o mesmo email já existir
     */
    @Override
    @Transactional
    public User save(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("User email cannot be null or empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("User password cannot be null or empty");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new DuplicatedTupleException("O usuário já existe");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Gera um token de autenticação para o usuário.
     *
     * @param email    o email do usuário
     * @param password a senha do usuário
     * @return um token de acesso se a autenticação for bem-sucedida, ou null se falhar
     */
    @Override
    public AccessToken authenticationToken(String email, String password) {
        var user = getByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return jwtService.generateToken(user);
    }

}