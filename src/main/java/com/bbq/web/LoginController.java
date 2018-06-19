package com.bbq.web;

import com.bbq.cons.CommonConstant;
import com.bbq.domain.User;
import com.bbq.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping("/doLogin")
    public ModelAndView login(HttpServletRequest request, User user){
        User dbUser = userService.getUserByUserName(user.getUserName());
        ModelAndView mav = new ModelAndView();
        mav.setViewName("forard:/login.jsp");
        if (dbUser == null) {
            mav.addObject(ERROR_MSG_KEY, "user is not exists!!");
        } else  if (!dbUser.getPassword().equals(user.getPassword())) {
            mav.addObject(ERROR_MSG_KEY, "password is not valid");
        } else if (dbUser.getLocked() == User.USER_LOCK) {
            mav.addObject(ERROR_MSG_KEY, "user is locked");
        } else {
            dbUser.setLastIp(request.getRemoteAddr());
            dbUser.setLastVisit(new Date());
            userService.loginSuccess(user);
            setSessionUser(request, user);
            String toUrl = (String)request.getSession().getAttribute(CommonConstant.LOGIN_TO_URL);
            request.getSession().removeAttribute(CommonConstant.LOGIN_TO_URL);
            if (StringUtils.isEmpty(toUrl)) {
                toUrl = "/index.html";
            }
            mav.setViewName("redirect:" + toUrl);
        }
        return mav;
    }

    @RequestMapping("/doLogout")
    public String logout(HttpSession session) {
        session.removeAttribute(CommonConstant.USER_CONTEXT);
        return "forward:/index.jsp";
    }

}
