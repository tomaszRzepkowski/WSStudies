package hello.security;


import hello.pojo.AuthorizationData;
import hello.pojo.dto.Credentials;

import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author tomaszrzepkowski on 16.03.2017.
 */
@Component
public class Authentication {

    @Autowired
    AuthorizationData authData;

    private static Authentication ourInstance = new Authentication();

    public static Authentication getInstance() {
        return ourInstance;
    }

    private Authentication() {
        authData = new AuthorizationData();
    }

    public String authenticateUser(String login, String password) {
        Map<String, Credentials> userList = authData.getUserList();
        boolean userExists = userList.containsKey(login);
        if(userExists) {
            Credentials credentials = userList.get(login);
            if(credentials.getPassword().equals(password)) {
                String newUUID = UUID.randomUUID().toString();
                credentials.setUUID(newUUID);
                return newUUID;
            }
        }
        return StringUtils.EMPTY;
    }

    public boolean isUserAuthenticated(String token) {
        Map<String, Credentials> userList = authData.getUserList();
        for (Credentials user : userList.values()) {
            if(user.getUUID().equals(token) && user.tokenIsValid()) {
                return true;
            }
        }
        return false;
    }


    public String getSecretForUserUUID(String UUID) {
        Map<String, Credentials> userList = authData.getUserList();
        for(Credentials user : userList.values()) {
            if(user.getUUID().equals(UUID)) {
                return user.getSecret();
            }
        }
        return StringUtils.EMPTY;
    }
}
