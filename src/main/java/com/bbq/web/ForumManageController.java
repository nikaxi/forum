package com.bbq.web;

import com.bbq.domain.Board;
import com.bbq.domain.User;
import com.bbq.service.ForumService;
import com.bbq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ForumManageController extends BaseController{
    private ForumService forumService;

    private UserService userService;

    @Autowired
    public void setForumService(ForumService forumService) {
        this.forumService = forumService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ModelAndView listAllBoards() {
        ModelAndView view = new ModelAndView();
        List<Board> boards = forumService.getAllBoards();
        view.addObject("boards", boards);
        view.setViewName("/listAllBoards");
        return view;
    }

    @RequestMapping(value = "/forum/addBoardPage", method = RequestMethod.GET)
    public String addBoardPage(){
        return "/addBoard";
    }

    @RequestMapping(value = "/forum/addBoard", method = RequestMethod.POST)
    public String addBoard(){
        return "/addBoardSuccess";
    }

    @RequestMapping(value = "/forum/setBoardManagerPage", method = RequestMethod.POST)
    public ModelAndView setBoardManager(){
        ModelAndView view = new ModelAndView();
        List<Board> boards = forumService.getAllBoards();
        List<User> users = userService.getAllUser();
        view.addObject("boards", boards);
        view.addObject("users", users);
        view.setViewName("/setBoardManager");
        return view;
    }

    @RequestMapping(value = "/forum/setBoardManager", method = RequestMethod.POST)
    public ModelAndView setBoardManager(@RequestParam("userName") String userName,
                                        @RequestParam("boardId") Integer boardId) {

        ModelAndView view = new ModelAndView();
        User user = userService.getUserByUserName(userName);
        if (user == null) {
            view.addObject(ERROR_MSG_KEY, "user:" + userName + " not exists");
            view.setViewName("/fail");
        } else {
            Board board = forumService.getBoardById(boardId);
            user.getManBoards().add(board);
            userService.update(user);
            view.setViewName("/success");
        }
        return view;
    }

    @RequestMapping(value = "/forum/userLockManage", method = RequestMethod.POST)
    public ModelAndView userLockManage(@RequestParam("userName") String userName,
                                       @RequestParam("locked") String locked) {
        ModelAndView view = new ModelAndView();
        User user = userService.getUserByUserName(userName);
        if (user == null) {
            view.addObject(ERROR_MSG_KEY, "user:" + userName + "not exists");
            view.setViewName("/fail");
        } else  {
            user.setLocked(Integer.parseInt(locked));
            userService.update(user);
            view.setViewName("/success");
        }
        return view;
    }
}
