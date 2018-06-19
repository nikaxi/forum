package com.bbq.web;


import com.bbq.cons.CommonConstant;
import com.bbq.dao.Page;
import com.bbq.domain.Board;
import com.bbq.domain.Post;
import com.bbq.domain.Topic;
import com.bbq.domain.User;
import com.bbq.service.ForumService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.RequestWrapper;
import java.util.Date;

public class BoardManageController extends BaseController {

    private ForumService forumService;

    public void setForumService(ForumService forumService) {
        this.forumService = forumService;
    }


    @RequestMapping(value = "/board/listBoardTopics-{boardId}", method = RequestMethod.GET)
    public ModelAndView listBoardTopics(@PathVariable Integer boardId,
                                        @RequestParam(value = "pageNo", required = false) Integer pageNo){
        ModelAndView view = new ModelAndView();
        Board board = forumService.getBoardById(boardId);
        pageNo = pageNo == null ? 1 : pageNo;
        Page pageTopic = forumService.getPageTopics(boardId, pageNo, CommonConstant.PAGE_SIZE);

        view.addObject("board", board);
        view.addObject("pagedTopic", pageTopic);
        view.setViewName("/listBoardTopics");
        return view;
    }

    @RequestMapping(value = "/board/addTopicPage-{boardId}", method = RequestMethod.GET)
    public ModelAndView addTopicPage(@PathVariable Integer boardId) {
        ModelAndView view = new ModelAndView();
        view.addObject("boardId", boardId);
        view.setViewName("/addTopic");
        return view;
    }

    @RequestMapping(value = "/board/addTopic", method = RequestMethod.POST)
    public String addTopic(HttpServletRequest request, Topic topic) {
        User user = getSessionUser(request);
        topic.setUser(user);
        Date now = new Date();
        topic.setCreateTime(now);
        topic.setLastPost(now);
        forumService.addTopic(topic);
        String targetUrl = "/board/listBoardTopics-" + topic.getBoardId()
                + ".html";
        return "redirect: " + targetUrl;
    }


    @RequestMapping(value = "/board/listTopicPosts-{topicId}", method = RequestMethod.GET)
    public ModelAndView listTopicPosts(@PathVariable Integer topicId, @RequestParam(value = "pageNo", required = false)
                                       Integer pageNo) {
        ModelAndView view = new ModelAndView();
        Topic topic = forumService.getTopicByTopicId(topicId);
        pageNo = pageNo==null? 1: pageNo;
        Page pagedPost = forumService.getPagedPosts(topicId, pageNo,
                            CommonConstant.PAGE_SIZE);

        view.addObject("topic", topic);
        view.addObject("pagedPost", pagedPost);
        view.setViewName("/listTopicPosts");
        return view;
    }

    @RequestMapping(value = "/board/addPost")
    public String addPost(HttpServletRequest request, Post post) {
        post.setCreateTime(new Date());
        post.setUser(getSessionUser(request));
        Topic topic = new Topic();
        int topicId = Integer.valueOf(request.getParameter("topicId"));
        if (topicId > 0 ) {
            topic.setTopicId(topicId);
            post.setTopic(topic);
        }

        forumService.addPost(post);
        String targetUrl = "/board/listTopicPosts-" + post.getTopic().getTopicId() + ".html";
        return "redirect:" + targetUrl;
    }


    @RequestMapping(value = "/board/removeBoard", method = RequestMethod.GET)
    public String removeBoard(@RequestParam("boardId") String boards) {
        String[] arrIds = boards.split(",");
        for (int i = 0; i < arrIds.length; i++) {
            forumService.removeBoard(new Integer(arrIds[i]));
        }

        String targetUrl = "/index.html";
        return "redirect:" + targetUrl;
    }

    @RequestMapping(value = "/board/removeTopic", method = RequestMethod.GET)
    public String removeTopic(@RequestParam("topicIds") String topicIds,
                              @RequestParam("boardId") String boardId) {

        String[] arrIds = topicIds.split(",");
        for (int i = 0; i < arrIds.length; i++) {
            forumService.removeTopic(new Integer(arrIds[i]));
        }
        String targetUrl = "/board/listBoardTopics-" + boardId + ".html";
        return "redirect:" + targetUrl;
    }


    @RequestMapping(value = "/board/makeDigestTopic", method = RequestMethod.GET)
    public String makeDigestTopic(@RequestParam("topicIds") String topicIds, @RequestParam("boardId") String boardId) {
        String[] arrIds = topicIds.split(",");
        for (int i = 0 ; i < arrIds.length; i++) {
            forumService.makeDigestTopic(new Integer(arrIds[i]));
        }
        String targetUrl = "/board/listBoardTopic-" + boardId + ".html";
        return "redirect:" + targetUrl;
    }
}

