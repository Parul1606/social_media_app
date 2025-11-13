package org.example.service;

import org.example.dao.ProfileDAO;
import org.example.model.Profile;

import java.sql.Timestamp;

public class ProfileService {

    private final ProfileDAO profileDAO;

    public ProfileService() {
        this.profileDAO = new ProfileDAO();
    }

    public ProfileService(ProfileDAO profileDAO) {
        this.profileDAO = profileDAO;
    }

    public boolean createProfile(Profile profile) throws Exception {
        return profileDAO.save(profile);
    }

    public Profile getProfileByUserId(int userId) throws Exception {
        return profileDAO.findByUserId(userId);
    }

    public boolean updateBio(int userId, String bio) throws Exception {
        Profile p = profileDAO.findByUserId(userId);
        if (p == null) {
            Profile np = new Profile();
            np.setUserId(userId);
            np.setBio(bio);
            np.setLocation(null);
            np.setCreatedAt(new Timestamp(System.currentTimeMillis()).toString());
            return profileDAO.save(np);
        }
        return profileDAO.updateBioByUserId(userId, bio);
    }
}
