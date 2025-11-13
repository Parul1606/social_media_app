package org.example.model;

import java.util.Date;

public class PasswordReset {
    private String id;
    private String email;
    private String token;
    private Date requestedAt;
    private boolean used;

    public PasswordReset() {
    }

    public PasswordReset(String id, String email, String token, Date requestedAt, boolean used) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.requestedAt = requestedAt;
        this.used = used;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(Date requestedAt) {
        this.requestedAt = requestedAt;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
