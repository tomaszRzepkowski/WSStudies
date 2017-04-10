package hello.pojo;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

/**
 * @author tomaszrzepkowski on 06.04.2017.
 */
@Component
@ApplicationScope
public class AuthorizationData {

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
        int TTLSeconds = 30;
        tokenValidationEnding = DateUtils.addSeconds(tokenValidationBegining, TTLSeconds);
    }

    public void invalidate() {
        tokenValidationBegining = new Date();
        tokenValidationEnding = DateUtils.addDays(tokenValidationBegining, -7);
        this.userAuthenticated = false;
    }


}
