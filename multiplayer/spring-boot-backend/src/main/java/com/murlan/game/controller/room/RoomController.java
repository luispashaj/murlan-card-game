package com.murlan.game.controller.room;

import com.murlan.game.shared.model.GameRoom;
import com.murlan.game.controller.lobby.model.LobbyEvent;
import com.murlan.game.controller.lobby.model.LobbyEventType;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class RoomController {

    @MessageMapping("/room/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public void makePlay(@DestinationVariable String roomId) {

    }

    @SubscribeMapping("/room")
    public LobbyEvent subscribe() {
        return new LobbyEvent(LobbyEventType.INITIAL, new GameRoom());
    }
}
