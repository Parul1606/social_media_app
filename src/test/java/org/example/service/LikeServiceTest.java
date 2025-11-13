package org.example.service;

import org.bson.types.ObjectId;
import org.example.dao.LikeDAO;
import org.example.model.Like;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {
    @Mock private LikeDAO likeDAO;

    @Test
    void addLike_delegatesToDao() {
        LikeService svc = new LikeService(likeDAO);
        Like l = new Like();
        svc.addLike(l);
        verify(likeDAO).save(l);
    }

    @Test
    void countLikes_returnsDaoValue() {
        LikeService svc = new LikeService(likeDAO);
        ObjectId postId = new ObjectId();
        when(likeDAO.countLikes(postId)).thenReturn(5L);
        assertEquals(5L, svc.countLikes(postId));
        verify(likeDAO).countLikes(postId);
    }
}
