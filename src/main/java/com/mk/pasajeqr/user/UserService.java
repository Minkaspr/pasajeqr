package com.mk.pasajeqr.user;

import com.mk.pasajeqr.user.request.UserRegisterRequest;

import java.util.List;

public interface UserService {
    User createUser(UserRegisterRequest request);
    User getById(Long id);
    List<User> listUsers();
    void deleteUser(Long id);
    User findByEmail(String email);
}
