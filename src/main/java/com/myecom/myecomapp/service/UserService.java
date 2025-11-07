package com.myecom.myecomapp.service;

import com.myecom.myecomapp.model.User;

import java.util.List;

public interface UserService {

    public User saveUser(User user);

    public User getUserByEmail(String email);

    public List<User> getAllUsers();

    public User getByUserId(int userId);

    public void updateUserRole(int userId, String role);

    public Boolean deleteUserById(int id);

    public void updatePassword(String email, String password);
}
