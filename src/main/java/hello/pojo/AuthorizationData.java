package hello.pojo;

import hello.pojo.dto.Credentials;
import hello.pojo.dto.Person;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.annotation.SessionScope;

/**
 * @author tomaszrzepkowski on 06.04.2017.
 */
@Component
@ApplicationScope
public class AuthorizationData {

    private Map<String,Credentials> userList;

    public AuthorizationData() {
        initUsers();
    }

    //Create some users, this map acts like a DB
    public void initUsers() {
        userList = new HashMap<>();

        Credentials c1 = new Credentials("tomek", "haslo");
        Credentials c2 = new Credentials("aaa", "aaa");
        Credentials c3 = new Credentials("bbb", "bbb");
        Credentials c4 = new Credentials("admin", "admin");

        userList.put(c1.getLogin(), c1);
        userList.put(c2.getLogin(), c2);
        userList.put(c3.getLogin(), c3);
        userList.put(c4.getLogin(), c4);
    }

    public Map<String, Credentials> getUserList() {
        return userList;
    }
}
