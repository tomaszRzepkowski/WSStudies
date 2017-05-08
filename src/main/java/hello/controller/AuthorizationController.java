package hello.controller;

import hello.security.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tomaszrzepkowski on 01.05.2017.
 */
@RestController("AuthorizationController")
@RequestMapping("/auth")
public class AuthorizationController {

    public static final String AUTHENTICATION_URI = "/auth/authenticate";

    @Autowired
    private Authentication authService;

    @RequestMapping(value = "/authenticate" , method = RequestMethod.GET, produces = "application/json")
    public String authenticateUser(@RequestParam(value="login") String login,
                                 @RequestParam(value="password") String password) {
        return authService.authenticateUser(login, password);
    }

}
