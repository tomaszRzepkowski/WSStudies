package hello.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.RequestFacade;
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
        String token = ( (RequestFacade) servletRequest ).getHeader("token");
        String login = ( (RequestFacade) servletRequest ).getHeader("login");
        String password = ( (RequestFacade) servletRequest ).getHeader("password");
        if(!authService.isUserAuthenticated()) {
            authService.authenticateUser(login, password);
        }
        if(authService.isUserAuthenticated()) {
            authService.checkIfTokenIsExpired();
            authService.authorizeUser(token);
        }
        if(authService.isUserAuthorized()) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            logger.info("User with credentials: " + login + " " +  password + "is not a valid user");
        }
    }

    @Override
    public void destroy() {

    }
}
