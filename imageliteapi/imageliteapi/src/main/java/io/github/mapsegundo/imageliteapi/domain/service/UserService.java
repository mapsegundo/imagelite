package io.github.mapsegundo.imageliteapi.domain.service;

import io.github.mapsegundo.imageliteapi.domain.AccessToken;
import io.github.mapsegundo.imageliteapi.domain.entity.User;

public interface UserService {
    User getByEmail(String email);
    User save(User user);
    AccessToken authenticationToken(String email, String password);

}
