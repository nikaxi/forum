package com.bbq.service;

import com.bbq.dao.*;
import com.bbq.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ForumService {

    private TopicDao topicDao;
    private UserDao userDao;
    private BoardDao boardDao;
    private PostDao postDao;

    @Autowired
    public void setTopicDao(TopicDao topicDao) {
        this.topicDao = topicDao;
    }

    @Autowired
    public void setBoardDao(BoardDao boardDao) {
        this.boardDao = boardDao;
    }


    @Autowired
    public void setPostDao(PostDao postDao) {
        this.postDao = postDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void addTopic(Topic topic){
        Board board = boardDao.get(topic.getBoardId());
        board.setTopicNum(board.getTopicNum()+1);
        topicDao.save(topic);

        topic.getMainPost().setTopic(topic);
        MainPost post = topic.getMainPost();
        post.setCreateTime(new Date());
        post.setUser(topic.getUser());
        post.setPostTitle(topic.getTopicTitle());
        postDao.save(topic.getMainPost());

        User user = topic.getUser();
        user.setCredit(user.getCredit()+10);
        userDao.update(user);
    }

    public void removeTopic(int topicId) {
        Topic topic = topicDao.get(topicId);

        Board board = boardDao.get(topic.getBoardId());
        board.setTopicNum(board.getTopicNum()-1);

        User user = topic.getUser();
        user.setCredit(user.getCredit()-10);

        topicDao.remove(topic);
        postDao.deleteTopicPosts(topicId);
    }


    public void addPost(Post post){
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
        postDao.remove(post);

        Topic topic = topicDao.get(post.getTopic().getTopicId());
        topic.setReplies(topic.getReplies()-1);

        User user = post.getUser();
        user.setCredit(user.getCredit()-20);
    }

    public void addBoard(Board board) {
        boardDao.save(board);
    }

    public void removeBoard(int boardId) {
        Board board = boardDao.get(boardId);
        boardDao.remove(board);

    }

    public void makeDigestTopic(int topicId) {
        Topic topic = topicDao.get(topicId);
        topic.setDigest(Topic.DIGEST_TOPIC);
        User user = topic.getUser();
        user.setCredit(user.getCredit()+ 100);
        // topic 处于hibernate受管状态，自动更新
    }

    public List<Board> getAllBoards(){
        return boardDao.loadAll();
    }

    public Page getPageTopics(int boardId, int pageNo, int pageSize){
        return topicDao.getPagedTopics(boardId, pageNo, pageSize);
    }

    public Page getPagedPosts(int topicId, int pageNo, int pageSize) {
        return postDao.getPagePosts(topicId, pageNo, pageSize);
    }


    public Page queryTopicByTitle(String title, int pageNo, int pageSize){
        return topicDao.queryTopicByTitle(title, pageNo, pageSize);
    }

    public Board getBoardById(int boardId) {
        return boardDao.get(boardId);
    }

    public Topic getTopicByTopicId(int topicId) {
        return topicDao.get(topicId);
    }

    public Post getPostByPostId(int postId) {
        return postDao.get(postId);
    }

    public void addBoardManager(int boardId, String userName) {
        User user = userDao.getUserByUserName(userName);
        if (user == null) {
            throw new RuntimeException("用户:" + userName + "不存在");
        } else {
            Board board = boardDao.get(boardId);
            user.getManBoards().add(board);
            userDao.update(user);
        }
    }


    public void updateTopic(Topic topic) {
        topicDao.update(topic);
    }


    public void updatePost(Post post) {
        postDao.update(post);
    }


}
