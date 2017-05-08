package hello.pojo.dto;

import java.util.Date;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * @author tomaszrzepkowski on 27.04.2017.
 */
public class Credentials {

    //in seconds
    private static final int TIME_TO_EXPIRE_UUID = 120;

    private String login;
    private String password;
    private String secret;
    private String UUID = StringUtils.EMPTY;
    private Date UUIDExpirationDate = new Date();


    public Credentials() {
    }

    public Credentials(String login, String password) {
        this.login = login;
        this.password = password;
        this.secret = "o5db y2sj ozqd sezh qnmf 2cow ytmp d5l4";
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
        this.UUIDExpirationDate = DateUtils.addSeconds(new Date(), TIME_TO_EXPIRE_UUID);
        Logger.getLogger("Credentials.class").info(this.UUIDExpirationDate.toString());
    }

    public Date getUUIDExpirationDate() {
        return UUIDExpirationDate;
    }

    public boolean tokenIsValid() {
        return this.UUIDExpirationDate.compareTo(new Date()) > 0;
    }
}
