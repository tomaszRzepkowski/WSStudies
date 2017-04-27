package hello.pojo;

import hello.pojo.dto.Credentials;
import hello.pojo.dto.Person;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

/**
 * @author tomaszrzepkowski on 06.04.2017.
 */
@Component
@ApplicationScope
public class AuthorizationData {

    public static final int TIME_TO_EXPIRE_UUID = 120;
    private Map<String,Credentials> userList;
    private boolean userAuthenticated = false;
    private Date tokenValidationBegining = new Date();
    private Date tokenValidationEnding = DateUtils.addDays(tokenValidationBegining, -7);

    public boolean isUserAuthenticated() {
        return userAuthenticated;
    }

    public boolean isUserAuthorized() {
        return userAuthenticated && isTokenValid();
    }

    public boolean isTokenValid() {
        return tokenValidationEnding.compareTo(new Date()) > 0;
    }

    public void validUser() {
        this.userAuthenticated = true;
    }

    public void validToken() {
        tokenValidationBegining = new Date();
        tokenValidationEnding = DateUtils.addSeconds(tokenValidationBegining, TIME_TO_EXPIRE_UUID);
    }

    public void invalidate() {
        tokenValidationBegining = new Date();
        tokenValidationEnding = DateUtils.addDays(tokenValidationBegining, -7);
        this.userAuthenticated = false;
    }

    public void initUsers() {
        userList = new HashMap<>();

        Credentials c1 = new Credentials();
        c1.setLogin("tomek");
        c1.setPassword("haslo");
        c1.setUUID("e550d817-534c-4140-bb76-4395f83cac08");

        Credentials c2 = new Credentials();
        c1.setLogin("aaa");
        c1.setPassword("aaa");
        c1.setUUID("e550d817-534c-4140-bb76-4395f83cac08");

        Credentials c3 = new Credentials();
        c1.setLogin("bbb");
        c1.setPassword("bbb");
        c1.setUUID("e550d817-534c-4140-bb76-4395f83cac08");

        Credentials c4 = new Credentials();
        c1.setLogin("admin");
        c1.setPassword("admin");
        c1.setUUID("e550d817-534c-4140-bb76-4395f83cac08");

        userList.put(c1.getLogin(), c1);
        userList.put(c2.getLogin(), c2);
        userList.put(c3.getLogin(), c3);
        userList.put(c4.getLogin(), c4);
    }

}
