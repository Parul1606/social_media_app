package org.example;

import org.example.model.*;
import org.example.service.*;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static final Logger log = LoggerFactory.getLogger(Main.class);

    static UserService userService = new UserService();
    static PostService postService = new PostService();
    static LikeService likeService = new LikeService();
    static CommentService commentService = new CommentService();
    static ProfileService profileService = new ProfileService();
    static FollowService followService = new FollowService();

    static User loggedInUser = null;

    public static void main(String[] args) throws Exception {

        while (true) {
            log.info("\n=== SOCIAL MEDIA APP ===");
            if (loggedInUser == null) {
                log.info("1. Register");
                log.info("2. Login");
                log.info("3. Reset Password");
                log.info("4. Exit");
                log.info("Choose: ");

                int ch = Integer.parseInt(sc.nextLine());
                switch (ch) {
                    case 1 -> register();
                    case 2 -> login();
                    case 3 -> resetPassword();
                    case 4 -> System.exit(0);
                    default -> log.info("Invalid choice.");
                }
            } else {
                log.info("1. Create Post");
                log.info("2. View Feed");
                log.info("3. Like a Post");
                log.info("4. Comment on Post");
                log.info("5. Change Password");
                log.info("6. View My Profile");
                log.info("7. Edit Profile (bio)");
                log.info("8. View My Posts");
                log.info("9. Follow a User");
                log.info("10. Logout");
                log.info("11. Exit");
                log.info("Choose: ");

                int ch = Integer.parseInt(sc.nextLine());
                switch (ch) {
                    case 1 -> createPost();
                    case 2 -> viewFeed();
                    case 3 -> likePost();
                    case 4 -> commentOnPost();
                    case 5 -> changePasswordLoggedIn();
                    case 6 -> viewMyProfile();
                    case 7 -> editProfileBio();
                    case 8 -> viewMyPosts();
                    case 9 -> followUser();
                    case 10 -> logout();
                    case 11 -> System.exit(0);
                    default -> log.info("Invalid choice.");
                }
            }
        }
    }

    static void resetPassword() {
        log.info("Enter your email: ");
        String email = sc.nextLine();
        try {
            User user = userService.getByEmail(email);
            if (user == null) {
                log.info("No user found with this email.");
                return;
            }
            log.info("Enter new password: ");
            String newPassword = sc.nextLine();
            boolean ok = userService.resetPassword(email, newPassword);
            if (ok) log.info("Password reset successful.");
            else log.info("Failed to reset password.");
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    }

    static void register() throws Exception {
        log.info("Username: ");
        String username = sc.nextLine();

        log.info("Full Name: ");
        String fullName = sc.nextLine();

        log.info("Email: ");
        String email = sc.nextLine();

        log.info("Password: ");
        String pass = sc.nextLine();

        User u = new User();
        u.setUsername(username);
        u.setFullName(fullName);
        u.setEmail(email);

        boolean ok = userService.register(u, pass);

        if (ok) log.info("Registered successfully.");
        else log.info("User already exists (email/username).");
    }

    static void login() throws Exception {
        log.info("Email: ");
        String email = sc.nextLine();

        log.info("Password: ");
        String pass = sc.nextLine();

        User u = userService.login(email, pass);

        if (u == null) {
            log.info("Invalid credentials.");
        } else {
            loggedInUser = u;
            log.info("Login successful. Welcome {}", u.getFullName());
        }
    }

    static void createPost() {
        if (loggedInUser == null) {
            log.info("Please log in first.");
            return;
        }

        log.info("Enter post content: ");
        String content = sc.nextLine();

        Post p = new Post();
        p.setId(new ObjectId());
        p.setUserId(loggedInUser.getUserId());
        p.setContent(content);
        p.setTags(List.of());
        p.setCreatedAt(System.currentTimeMillis());

        postService.createPost(p);

        log.info("Post created.");
    }

    // ------------------------------------------------------------

    static void viewFeed() {
        List<Post> posts = postService.getAllPosts();

        log.info("\n=== FEED ===");

        if (posts.isEmpty()) {
            log.info("No posts yet.");
            return;
        }

        for (Post p : posts) {

            long likeCount = likeService.countLikes(p.getId());
            List<Comment> comments = commentService.getComments(p.getId());

            log.info("\nPost ID: {}", p.getId());
            String authorName = String.valueOf(p.getUserId());
            try {
                User author = userService.getById(p.getUserId());
                if (author != null && author.getUsername() != null) {
                    authorName = author.getUsername();
                }
            } catch (Exception ignored) {}
            log.info("Posted by: {}", authorName);
            log.info("Content: {}", p.getContent());
            log.info("Likes: {}", likeCount);
            log.info("Comments:");

            for (Comment c : comments) {
                log.info(" - ({}) {}", c.getUserId(), c.getText());
            }
        }
    }

    static void likePost() {
        if (loggedInUser == null) {
            log.info("Please log in first.");
            return;
        }

        log.info("Enter Post ID to Like: ");
        String id = sc.nextLine();

        Like like = new Like();
        like.setId(new ObjectId());
        like.setPostId(new ObjectId(id));
        like.setUserId(loggedInUser.getUserId());
        like.setLikedAt(System.currentTimeMillis());

        likeService.addLike(like);

        log.info("Liked.");
    }

    static void commentOnPost() {
        if (loggedInUser == null) {
            log.info("Please log in first.");
            return;
        }

        log.info("Enter Post ID: ");
        String id = sc.nextLine();

        log.info("Comment: ");
        String text = sc.nextLine();

        Comment c = new Comment();
        c.setId(new ObjectId());
        c.setPostId(new ObjectId(id));
        c.setUserId(loggedInUser.getUserId());
        c.setText(text);
        c.setCommentedAt(System.currentTimeMillis());

        commentService.addComment(c);

        log.info("Comment added.");
    }

    static void changePasswordLoggedIn() {
        if (loggedInUser == null) {
            log.info("Please log in first.");
            return;
        }
        log.info("Current password: ");
        String current = sc.nextLine();
        log.info("New password: ");
        String next = sc.nextLine();
        try {
            boolean ok = userService.changePassword(loggedInUser.getUserId(), current, next);
            if (ok) log.info("Password changed successfully.");
            else log.info("Failed to change password (check current password).");
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    }

    static void viewMyProfile() {
        if (loggedInUser == null) {
            log.info("Please log in first.");
            return;
        }
        try {
            int uid = loggedInUser.getUserId();
            Profile p = profileService.getProfileByUserId(uid);
            int followers = followService.countFollowers(uid);
            int following = followService.countFollowing(uid);
            log.info("\n=== MY PROFILE ===");
            log.info("Username: {}", loggedInUser.getUsername());
            log.info("Full Name: {}", loggedInUser.getFullName());
            log.info("Email: {}", loggedInUser.getEmail());
            log.info("Joined: {}", loggedInUser.getCreatedAt());
            log.info("Bio: {}", (p != null ? p.getBio() : ""));
            log.info("Followers: {}, Following: {}", followers, following);
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    }

    static void editProfileBio() {
        if (loggedInUser == null) {
            log.info("Please log in first.");
            return;
        }
        log.info("Enter new bio: ");
        String bio = sc.nextLine();
        try {
            boolean ok = profileService.updateBio(loggedInUser.getUserId(), bio);
            if (ok) log.info("Bio updated.");
            else log.info("Failed to update bio.");
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    }

    static void viewMyPosts() {
        if (loggedInUser == null) {
            log.info("Please log in first.");
            return;
        }
        try {
            List<Post> mine = postService.getPostsByUserId(loggedInUser.getUserId());
            log.info("\n=== MY POSTS ===");
            if (mine.isEmpty()) {
                log.info("No posts yet.");
                return;
            }
            for (Post p : mine) {
                log.info("- {}: {}", p.getId(), p.getContent());
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    }

    static void followUser() {
        if (loggedInUser == null) {
            log.info("Please log in first.");
            return;
        }
        log.info("Enter username to follow: ");
        String uname = sc.nextLine();
        try {
            User u = userService.getByUsername(uname);
            if (u == null) {
                log.info("No such user.");
                return;
            }
            if (u.getUserId() == loggedInUser.getUserId()) {
                log.info("You cannot follow yourself.");
                return;
            }
            boolean ok = followService.follow(loggedInUser.getUserId(), u.getUserId());
            if (ok) {
                log.info("Now following {}.", uname);
            } else {
                boolean already = followService.isFollowing(loggedInUser.getUserId(), u.getUserId());
                if (already) {
                    log.info("Already following {}.", uname);
                } else {
                    log.info("Could not follow {}. Please check logs for details.", uname);
                }
            }
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
        }
    }

    static void logout() {
        loggedInUser = null;
        log.info("Logged out.");
    }
}
