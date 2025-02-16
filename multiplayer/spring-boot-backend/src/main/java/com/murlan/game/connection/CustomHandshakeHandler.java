package com.murlan.game.connection;

import com.murlan.game.connection.model.StompPrincipal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {
    // Custom class for storing principal
    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        if (request.getPrincipal() != null && StringUtils.isEmpty(request.getPrincipal().getName())) {
            return request.getPrincipal();
        }
        // Generate principal with UUID as name
        String uuid = UUID.randomUUID().toString();
        return new StompPrincipal(uuid);
//        Faker faker = new Faker();
//        return new StompPrincipal(faker.name().fullName());
    }
}
