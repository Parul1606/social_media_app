package org.example.service;

import org.bson.types.ObjectId;
import org.example.dao.CommentDAO;
import org.example.model.Comment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock private CommentDAO commentDAO;

    @Test
    void addComment_delegatesToDao() {
        CommentService svc = new CommentService(commentDAO);
        Comment c = new Comment();
        svc.addComment(c);
        verify(commentDAO).save(c);
    }

    @Test
    void getComments_returnsDaoResult() {
        CommentService svc = new CommentService(commentDAO);
        ObjectId postId = new ObjectId();
        List<Comment> list = List.of(new Comment());
        when(commentDAO.findByPostId(postId)).thenReturn(list);
        assertEquals(list, svc.getComments(postId));
        verify(commentDAO).findByPostId(postId);
    }
}
