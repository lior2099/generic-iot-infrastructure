/*
 FileName: Message.java
 Author: Lior Shalom
 Date: 5/09/24
 reviewer:
*/


package il.co.ilrd.chat;

import java.io.Serializable;

public class Message implements Serializable {
    private static final long serialVersionUID = 8648474831666168840L;
    private final MethodType type;
    private final String body;

    public Message(MethodType type, String body) {
        this.type = type;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public MethodType getType() {
        return type;
    }

    public enum MethodType {
        SEND_MESSAGE,
        REGISTER,
        UNREGISTER
    }
}