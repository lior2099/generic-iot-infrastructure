/*
 FileName: Message.java
 Author: Lior Shalom
 Date: 14/09/24
 reviewer:
*/


package simple_rps;

import java.nio.channels.Channel;
import java.util.Map;

public class Message_notuse {
    private final Map<Channel,Object> connection;
    private final String body;

    public Message_notuse(Map<Channel,Object> connection, String body) {
        this.connection = connection;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public Map<Channel,Object> getType() {
        return connection;
    }
}