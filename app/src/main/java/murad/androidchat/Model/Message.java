package murad.androidchat.Model;

/**
 * Created by Administrator on 17.07.2016.
 */
public class Message {
    String message, createdAt;
    User user;

    public Message() {
    }

    public Message( String message, String createdAt, User user) {
        this.message = message;
        this.createdAt = createdAt;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
