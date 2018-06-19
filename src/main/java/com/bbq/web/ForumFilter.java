package com.bbq.web;

import com.bbq.domain.User;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.bbq.cons.CommonConstant.LOGIN_TO_URL;
import static com.bbq.cons.CommonConstant.USER_CONTEXT;

public class ForumFilter implements Filter {
    private static  final  String FILTERED_REQUEST = "@@session_context_filtered_request";

    private static final String[] INHERENT_ESCAPE_URIS = {
            "/index.jsp", "/index.html", "/login.jsp", "/login/doLogin.html",
            "/register.jsp", "/register.html", "/board/listBoardTopics-",
            "/board/listTopicPosts-"
    };

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        if (request != null && request.getAttribute(FILTERED_REQUEST) != null) {
            chain.doFilter(request, response);
        } else  {
            request.setAttribute(FILTERED_REQUEST, Boolean.TRUE);
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            User userContext = getSessionUser(httpServletRequest);

            if (userContext == null &&
                    !isURILogin(httpServletRequest.getRequestURI(), httpServletRequest)) {
                String toUrl = httpServletRequest.getRequestURL().toString();
                if (!StringUtils.isEmpty(httpServletRequest.getQueryString())) {
                    toUrl += "?" + httpServletRequest.getQueryString();
                }

                httpServletRequest.getSession().setAttribute(LOGIN_TO_URL, toUrl);

                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
            chain.doFilter(request, response);
        }
    }


    public void init(FilterConfig filterConfig) throws ServletException {

    }

    private boolean isURILogin(String requestURI, HttpServletRequest request) {
        if (request.getContextPath().equalsIgnoreCase(requestURI)
                || (request.getContextPath() + "/").equalsIgnoreCase(requestURI))
            return true;

        for (String uri: INHERENT_ESCAPE_URIS) {
            if (requestURI != null && requestURI.indexOf(uri) >= 0) {
                return true;
            }
        }
        return false;
    }


    protected User getSessionUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(USER_CONTEXT);
    }
    public void destroy(){}

}
