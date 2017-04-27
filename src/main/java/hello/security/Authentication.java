package hello.security;


import hello.pojo.AuthorizationData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author tomaszrzepkowski on 16.03.2017.
 */
@Component
public class Authentication {

    private static final Logger logger = LoggerFactory.getLogger(Authentication.class);

//    private static final String USER_UUID = UUID.randomUUID().toString();
    private static final String UUID = "e550d817-534c-4140-bb76-4395f83cac08";
    private static final String USERNAME = "tomek";
    private static final String PASSWORD = "haslo";

    @Autowired
    AuthorizationData authData;

    private static Authentication ourInstance = new Authentication();

    public static Authentication getInstance() {
        return ourInstance;
    }

    private Authentication() {
    }

    public boolean authenticateUser(String login, String password) {
        if(USERNAME.equals(login)) {
            if(PASSWORD.equals(password)) {
                authData.validUser();
                return true;
            }
        }
        authData.invalidate();
        return false;
    }

    public boolean authorizeUser(String token) {
        boolean isTokenValid = UUID.equals(token);
        if(isTokenValid && isUserAuthenticated()) {
            authData.validToken();
        } else {
            authData.invalidate();
            logger.info("Token expired, please login again");
        }
        return isTokenValid;
    }

    public void checkIfTokenIsExpired() {
        if (!isTokenValid()) {
            authData.invalidate();
        }
    }

    public boolean isUserAuthenticated() {
        return authData.isUserAuthenticated();
    }

    public boolean isUserAuthorized() {
        return authData.isUserAuthorized();
    }

    public boolean isTokenValid() {
        return authData.isTokenValid();
    }
}
