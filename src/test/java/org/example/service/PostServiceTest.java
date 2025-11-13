package org.example.service;

import org.bson.types.ObjectId;
import org.example.dao.PostDAO;
import org.example.model.Post;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock private PostDAO postDAO;

    @Test
    void createPost_delegatesToDao() {
        PostService postService = new PostService(postDAO);
        Post p = new Post();
        ObjectId id = new ObjectId();
        p.setId(id);
        when(postDAO.save(p)).thenReturn(id);
        ObjectId out = postService.createPost(p);
        assertEquals(id, out);
        verify(postDAO).save(p);
    }

    @Test
    void getAllPosts_returnsDaoResult() {
        PostService postService = new PostService(postDAO);
        List<Post> posts = List.of(new Post());
        when(postDAO.findAll()).thenReturn(posts);
        assertEquals(posts, postService.getAllPosts());
        verify(postDAO).findAll();
    }

    @Test
    void getPost_returnsFromDao() {
        PostService postService = new PostService(postDAO);
        ObjectId id = new ObjectId();
        Post p = new Post();
        when(postDAO.findById(id)).thenReturn(p);
        assertEquals(p, postService.getPost(id));
        verify(postDAO).findById(id);
    }
}
