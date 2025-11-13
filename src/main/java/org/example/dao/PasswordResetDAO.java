package org.example.dao;

import org.example.model.PasswordReset;

import java.util.*;

public class PasswordResetDAO {
    private static final Map<String, PasswordReset> resets = new HashMap<>();

    public void save(PasswordReset reset) {
        resets.put(reset.getId(), reset);
    }

    public PasswordReset findByToken(String token) {
        return resets.values().stream()
                .filter(r -> r.getToken().equals(token) && !r.isUsed())
                .findFirst().orElse(null);
    }

    public List<PasswordReset> findByEmail(String email) {
        List<PasswordReset> list = new ArrayList<>();
        for (PasswordReset r : resets.values()) {
            if (r.getEmail().equals(email)) list.add(r);
        }
        return list;
    }
}
