package com.bbq.service;

import com.bbq.dao.BoardDao;
import com.bbq.dao.PostDao;
import com.bbq.dao.TopicDao;
import com.bbq.dao.UserDao;
import com.bbq.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    private TopicDao topicDao;

    private UserDao userDao;

    private BoardDao boardDao;

    private PostDao postDao;


    @Autowired
    public void setTopicDao(TopicDao topicDao) {
        this.topicDao = topicDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setBoardDao(BoardDao boardDao) {
        this.boardDao = boardDao;
    }

    @Autowired
    public void setPostDao(PostDao postDao) {
        this.postDao = postDao;
    }

    public void addTopic(Topic topic) {
        Board board = (Board) boardDao.get(topic.getBoardId());

        board.setTopicNum(board.getTopicNum()+1);
        topicDao.save(topic);

        topic.getMainPost().setTopic(topic);

        MainPost post = topic.getMainPost();
        post.setCreateTime(new Date());
        post.setUser(topic.getUser());
        post.setPostTitle(topic.getTopicTitle());
        post.setBoardId(topic.getBoardId());
        postDao.save(post);

        User user = topic.getUser();
        user.setCredit(user.getCredit() + 10);
        userDao.update(user);
    }


    public void removeTopic(int topicId) {
        Topic topic = topicDao.get(topicId);

        Board board = boardDao.get(topic.getBoardId());

        board.setTopicNum(board.getTopicNum() - 1);

        User user = topic.getUser();
        user.setCredit(user.getCredit() - 50);

        topicDao.remove(topic);
        postDao.deleteTopicPosts(topicId);

    }



    public void addPost(Post post) {
        postDao.save(post);

        User user = post.getUser();
        user.setCredit(user.getCredit()+5);
        userDao.update(user);

        Topic topic = topicDao.get(post.getTopic().getTopicId());
        topic.setReplies(topic.getReplies()+1);
        topic.setLastPost(new Date());
    }

    public void removePost(int postId) {
        Post post = postDao.get(postId);
    }
}
