package org.example.service;

import org.example.dao.UserDAO;
import org.example.model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.example.model.Profile;

import java.sql.Timestamp;

public class UserService {

    private final UserDAO userDAO;
    private final ProfileService profileService;

    public UserService() {
        this.userDAO = new UserDAO();
        this.profileService = new ProfileService();
    }

    public UserService(UserDAO userDAO, ProfileService profileService) {
        this.userDAO = userDAO;
        this.profileService = profileService;
    }

    public boolean register(User user, String plainPassword) throws Exception {

        if (userDAO.findByEmail(user.getEmail()) != null) return false;
        if (userDAO.findByUsername(user.getUsername()) != null) return false;

        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        user.setPasswordHash(hashed);

        boolean ok = userDAO.save(user);
        if (!ok) return false;
        User saved = userDAO.findByEmail(user.getEmail());
        if (saved != null) {
            Profile p = new Profile();
            p.setUserId(saved.getUserId());
            p.setBio("");
            p.setLocation(null);
            p.setCreatedAt(new Timestamp(System.currentTimeMillis()).toString());
            try {
                profileService.createProfile(p);
            } catch (Exception ignored) {
            }
        }
        return true;
    }

    public User login(String email, String password) throws Exception {
        User user = userDAO.findByEmail(email);
        if (user == null) return null;

        if (BCrypt.checkpw(password, user.getPasswordHash())) {
            return user;
        }
        return null;
    }

    public User getById(int id) throws Exception {
        return userDAO.findById(id);
    }

    public User getByEmail(String email) throws Exception {
        return userDAO.findByEmail(email);
    }

    public User getByUsername(String username) throws Exception {
        return userDAO.findByUsername(username);
    }

    public boolean resetPassword(String email, String newPassword) throws Exception {
        User user = userDAO.findByEmail(email);
        if (user == null) return false;
        String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        user.setPasswordHash(hashed);
        return userDAO.updatePasswordHashByEmail(email, hashed);
    }

    public boolean changePassword(int userId, String currentPassword, String newPassword) throws Exception {
        User user = userDAO.findById(userId);
        if (user == null) return false;
        if (!BCrypt.checkpw(currentPassword, user.getPasswordHash())) return false;
        String hashed = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        user.setPasswordHash(hashed);
        return userDAO.updatePasswordHashByEmail(user.getEmail(), hashed);
    }
}
