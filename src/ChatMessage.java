import java.io.Serializable;

/*
 * This class defines the different type of messages that will be exchanged between the
 * Clients and the Server. 
 * When talking from a Java Client to a Java Server a lot easier to pass Java objects, no 
 * need to count bytes or to wait for a line feed at the end of the frame
 */
public class ChatMessage implements Serializable {

    protected static final long serialVersionUID = 1112122200L;

    // The different types of message sent by the Client
    // WHOISIN to receive the list of the users connected
    // MESSAGE an ordinary message
    // LOGOUT to disconnect from the Server
    static final int WHOISIN = 0, MESSAGE = 1, LOGOUT = 2, AUTH = 3, AUTHED = 4, FETCH_CHAT = 5;
    private int type;
    private String message;
    private String username, password;

    // constructor
    ChatMessage(int type, String message) {
        this.type = type;
        this.message = message;
    }

    ChatMessage(int type, String username, String password){
        this.type = type;
        this.username = username;
        this.password = password;
    }

    ChatMessage(int type){
        this.type=type;
    }

    //message true-valid false-invalid


    // getters
    int getType() {
        return type;
    }

    String getMessage() {
        return message;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }
}

