package org.example.service;

import org.example.dao.UserDAO;
import org.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserDAO userDAO;
    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1);
        user.setUsername("testuser");
        user.setFullName("Test User");
        user.setEmail("test@example.com");
        user.setPasswordHash("hashed");
    }

    @Test
    void register_shouldReturnFalseIfEmailExists() throws Exception {
        when(userDAO.findByEmail(user.getEmail())).thenReturn(user);
        boolean result = userService.register(user, "password");
        assertFalse(result);
        verify(userDAO, never()).save(any());
    }

    @Test
    void register_shouldReturnFalseIfUsernameExists() throws Exception {
        when(userDAO.findByEmail(user.getEmail())).thenReturn(null);
        when(userDAO.findByUsername(user.getUsername())).thenReturn(user);
        boolean result = userService.register(user, "password");
        assertFalse(result);
        verify(userDAO, never()).save(any());
    }

    @Test
    void register_shouldHashPasswordAndSaveUser() throws Exception {
        when(userDAO.findByEmail(user.getEmail())).thenReturn(null);
        when(userDAO.findByUsername(user.getUsername())).thenReturn(null);
        when(userDAO.save(any(User.class))).thenReturn(true);
        boolean result = userService.register(user, "password");
        assertTrue(result);
        verify(userDAO).save(any(User.class));
    }

    @Test
    void login_shouldReturnNullIfUserNotFound() throws Exception {
        when(userDAO.findByEmail(user.getEmail())).thenReturn(null);
        User result = userService.login(user.getEmail(), "password");
        assertNull(result);
    }

    @Test
    void login_shouldReturnUserIfPasswordMatches() throws Exception {
        String plainPassword = "password";
        String hashed = org.mindrot.jbcrypt.BCrypt.hashpw(plainPassword, org.mindrot.jbcrypt.BCrypt.gensalt());
        user.setPasswordHash(hashed);
        when(userDAO.findByEmail(user.getEmail())).thenReturn(user);
        User result = userService.login(user.getEmail(), plainPassword);
        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void login_shouldReturnNullIfPasswordDoesNotMatch() throws Exception {
        String hashed = org.mindrot.jbcrypt.BCrypt.hashpw("other", org.mindrot.jbcrypt.BCrypt.gensalt());
        user.setPasswordHash(hashed);
        when(userDAO.findByEmail(user.getEmail())).thenReturn(user);
        User result = userService.login(user.getEmail(), "password");
        assertNull(result);
    }

    @Test
    void getById_shouldReturnUserFromDAO() throws Exception {
        when(userDAO.findById(1)).thenReturn(user);
        User result = userService.getById(1);
        assertEquals(user, result);
    }
}
