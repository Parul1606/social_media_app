package org.example.service;

import org.example.dao.ProfileDAO;
import org.example.model.Profile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {
    @Mock private ProfileDAO profileDAO;

    @Test
    void createProfile_delegatesToDao() throws Exception {
        ProfileService svc = new ProfileService(profileDAO);
        Profile p = new Profile();
        when(profileDAO.save(p)).thenReturn(true);
        assertTrue(svc.createProfile(p));
        verify(profileDAO).save(p);
    }

    @Test
    void getProfileByUserId_returnsDao() throws Exception {
        ProfileService svc = new ProfileService(profileDAO);
        Profile p = new Profile();
        when(profileDAO.findByUserId(1)).thenReturn(p);
        assertEquals(p, svc.getProfileByUserId(1));
        verify(profileDAO).findByUserId(1);
    }

    @Test
    void updateBio_createsIfMissing() throws Exception {
        ProfileService svc = new ProfileService(profileDAO);
        when(profileDAO.findByUserId(2)).thenReturn(null);
        when(profileDAO.save(any(Profile.class))).thenReturn(true);
        assertTrue(svc.updateBio(2, "hello"));
        verify(profileDAO).save(any(Profile.class));
        verify(profileDAO, never()).updateBioByUserId(eq(2), anyString());
    }

    @Test
    void updateBio_updatesIfExists() throws Exception {
        ProfileService svc = new ProfileService(profileDAO);
        when(profileDAO.findByUserId(3)).thenReturn(new Profile());
        when(profileDAO.updateBioByUserId(3, "bio")).thenReturn(true);
        assertTrue(svc.updateBio(3, "bio"));
        verify(profileDAO).updateBioByUserId(3, "bio");
        verify(profileDAO, never()).save(any(Profile.class));
    }
}
