package com.murlan.game.connection;

import com.murlan.game.shared.model.GameRoom;
import com.murlan.game.controller.lobby.model.LobbyEvent;
import com.murlan.game.controller.lobby.model.LobbyEventType;
import com.murlan.game.shared.context.SpringContext;
import com.murlan.game.connection.model.StompPrincipal;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

public class CustomInboundChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            // validate room subscription - if channel has already 4 connected users or is in play
            // return NULL if that's the case
            StompPrincipal principal = (StompPrincipal) accessor.getHeader("simpUser");

            if(accessor.getDestination().equals("/topic/lobby")) {
                LobbyEvent event = new LobbyEvent();
                event.setEventType(LobbyEventType.INITIAL);

                GameRoom room = new GameRoom();
                room.setRoomId("as213bd");
                room.setGameType("MURLAN");
                room.setCreatedBy("Luis");
                room.setPlayersJoined("4 / 4");
                event.getGameRooms().add(room);

                GameRoom room2 = new GameRoom();
                room2.setRoomId("bds321nm");
                room2.setGameType("5 KATESH");
                room2.setCreatedBy("Jari");
                room2.setPlayersJoined("1 / 2");
                event.getGameRooms().add(room2);

                // Probably the initial lobby data needs to be sent through clientOutboundChannel, as the subscription has not happened yet when intercepting Inbound
                SimpMessagingTemplate messagingTemplate = SpringContext.getBean(SimpMessagingTemplate.class);
                messagingTemplate.convertAndSendToUser(principal.getName(),"/queue/updates", event);
            }

        }

        return message;
    }
}
