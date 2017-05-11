package hello.security;

import hello.controller.AuthorizationController;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.RequestFacade;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author tomaszrzepkowski on 16.03.2017.
 */
public class AuthorizationFilter implements Filter{

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationFilter.class);

    @Autowired
    private Authentication authService;

    public AuthorizationFilter(Authentication authService) {
        super();
        if (authService == null) {
            this.authService = Authentication.getInstance();
        }
    }

    public AuthorizationFilter() {
        super();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("Content-Type", "application/json");
        String requestURI = ( (RequestFacade) servletRequest ).getRequestURI();
        String token = ( (RequestFacade) servletRequest ).getParameter("token");

        if(null == token && requestURI.equals(AuthorizationController.AUTHENTICATION_URI)
                || requestURI.contains(AuthorizationController.SWAGGER_URI)
                || requestURI.contains(AuthorizationController.V2)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else if (authService.isUserAuthenticated(token)){
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ( (HttpServletResponse) servletResponse ).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            servletResponse.getWriter().print("You are unauthorized to access the server. Try authenticating with your credentials");
        }
    }

    @Override
    public void destroy() {

    }
}
