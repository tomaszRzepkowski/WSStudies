package hello.pojo.dto;

/**
 * @author tomaszrzepkowski on 27.04.2017.
 */
public class Credentials {

    private String login;
    private String password;
    private String UUID;

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
    }
}
