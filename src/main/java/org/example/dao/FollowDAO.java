package org.example.dao;

import org.example.config.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class FollowDAO {
    private final Connection conn = MySQLConnection.getConnection();

    public boolean follow(int followerId, int followeeId) throws Exception {
        if (followerId == followeeId) return false;
        if (isFollowing(followerId, followeeId)) return false;
        String sql = "INSERT INTO follows (follower_id, followee_id) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, followerId);
        ps.setInt(2, followeeId);
        return ps.executeUpdate() > 0;
    }

    public boolean unfollow(int followerId, int followeeId) throws Exception {
        String sql = "DELETE FROM follows WHERE follower_id = ? AND followee_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, followerId);
        ps.setInt(2, followeeId);
        return ps.executeUpdate() > 0;
    }

    public boolean isFollowing(int followerId, int followeeId) throws Exception {
        String sql = "SELECT 1 FROM follows WHERE follower_id = ? AND followee_id = ? LIMIT 1";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, followerId);
        ps.setInt(2, followeeId);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    public int countFollowers(int userId) throws Exception {
        String sql = "SELECT COUNT(*) FROM follows WHERE followee_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
    }

    public int countFollowing(int userId) throws Exception {
        String sql = "SELECT COUNT(*) FROM follows WHERE follower_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        return rs.next() ? rs.getInt(1) : 0;
    }

    public Set<Integer> getFollowers(int userId) throws Exception {
        String sql = "SELECT follower_id FROM follows WHERE followee_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        Set<Integer> out = new HashSet<>();
        while (rs.next()) out.add(rs.getInt(1));
        return out;
    }

    public Set<Integer> getFollowing(int userId) throws Exception {
        String sql = "SELECT followee_id FROM follows WHERE follower_id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();
        Set<Integer> out = new HashSet<>();
        while (rs.next()) out.add(rs.getInt(1));
        return out;
    }
}
