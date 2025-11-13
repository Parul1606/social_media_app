package org.example.service;

import org.example.dao.PasswordResetDAO;
import org.example.model.PasswordReset;

import java.util.*;

public class PasswordResetService {
    private final PasswordResetDAO dao;

    public PasswordResetService() {
        this.dao = new PasswordResetDAO();
    }

    public PasswordResetService(PasswordResetDAO dao) {
        this.dao = dao;
    }

    public void createReset(String email, String token) {
        PasswordReset reset = new PasswordReset(UUID.randomUUID().toString(), email, token, new Date(), false);
        dao.save(reset);
    }

    public PasswordReset getByToken(String token) {
        return dao.findByToken(token);
    }

    public List<PasswordReset> getByEmail(String email) {
        return dao.findByEmail(email);
    }

    public void markUsed(PasswordReset reset) {
        reset.setUsed(true);
        dao.save(reset);
    }
}
