package org.example.service;

import org.example.dao.FollowDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class FollowService {
    private final FollowDAO followDAO;
    private static final Logger log = LoggerFactory.getLogger(FollowService.class);

    public FollowService() {
        this.followDAO = new FollowDAO();
    }

    public FollowService(FollowDAO followDAO) {
        this.followDAO = followDAO;
    }

    public boolean follow(int followerId, int followeeId) {
        try {
            return followDAO.follow(followerId, followeeId);
        } catch (Exception e) {
            log.error("follow({}, {}) failed: {}", followerId, followeeId, e.getMessage());
            return false;
        }
    }

    public boolean unfollow(int followerId, int followeeId) {
        try {
            return followDAO.unfollow(followerId, followeeId);
        } catch (Exception e) {
            log.error("unfollow({}, {}) failed: {}", followerId, followeeId, e.getMessage());
            return false;
        }
    }

    public boolean isFollowing(int followerId, int followeeId) {
        try {
            return followDAO.isFollowing(followerId, followeeId);
        } catch (Exception e) {
            log.error("isFollowing({}, {}) failed: {}", followerId, followeeId, e.getMessage());
            return false;
        }
    }

    public int countFollowers(int userId) {
        try {
            return followDAO.countFollowers(userId);
        } catch (Exception e) {
            log.error("countFollowers({}) failed: {}", userId, e.getMessage());
            return 0;
        }
    }

    public int countFollowing(int userId) {
        try {
            return followDAO.countFollowing(userId);
        } catch (Exception e) {
            log.error("countFollowing({}) failed: {}", userId, e.getMessage());
            return 0;
        }
    }

    public Set<Integer> getFollowers(int userId) {
        try {
            return followDAO.getFollowers(userId);
        } catch (Exception e) {
            log.error("getFollowers({}) failed: {}", userId, e.getMessage());
            return Set.of();
        }
    }

    public Set<Integer> getFollowing(int userId) {
        try {
            return followDAO.getFollowing(userId);
        } catch (Exception e) {
            log.error("getFollowing({}) failed: {}", userId, e.getMessage());
            return Set.of();
        }
    }
}
