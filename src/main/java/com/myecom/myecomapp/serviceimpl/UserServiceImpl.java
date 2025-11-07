package com.myecom.myecomapp.serviceimpl;

import com.myecom.myecomapp.model.User;
import com.myecom.myecomapp.repository.UserRepository;
import com.myecom.myecomapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);

    @Override
    public User saveUser(User user) {
        user.setRole("ROLE_USER");
        String encodedPassword = encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepo.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public User getByUserId(int userId) {
        return userRepo.findById(userId).orElse(null);
    }

    @Override
    public void updateUserRole(int userId, String role) {
        User user = userRepo.findById(userId).orElse(null);
        if (user != null) {
            user.setRole(role);
            userRepo.save(user);
        }
    }

    @Override
    public Boolean deleteUserById(int id) {
        User deleteUser = userRepo.findById(id).orElse(null);

        if (deleteUser != null) {
            userRepo.deleteById(id);
            return true;
        } return false;
    }

    @Override
    public void updatePassword(String email, String password) {

        User user = userRepo.findByEmail(email);

        if (user != null) {
            user.setPassword(encoder.encode(password));
            userRepo.save(user);
        }
    }
}
