package com.bbq.web;

import com.bbq.domain.User;
import com.bbq.exception.UserExistException;
import com.bbq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RegisterController extends BaseController{

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(HttpServletRequest request, User user) {
        ModelAndView view = new ModelAndView();
        view.setViewName("/success");

        try {
            userService.register(user);
        } catch (UserExistException e) {
            view.addObject(ERROR_MSG_KEY, "username is exists try annother username");
            view.setViewName("forward:/register.jsp");
        }

        setSessionUser(request, user);
        return view;
    }
}
