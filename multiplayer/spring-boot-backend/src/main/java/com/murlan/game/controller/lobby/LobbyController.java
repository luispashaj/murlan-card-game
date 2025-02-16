package com.murlan.game.controller.lobby;

import com.murlan.game.controller.lobby.model.CreateRoomRequest;
import com.murlan.game.shared.model.GameRoom;
import com.murlan.game.controller.lobby.model.LobbyEvent;
import com.murlan.game.controller.lobby.model.LobbyEventType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@EnableScheduling
@Controller
public class LobbyController {

    private final SimpMessagingTemplate messagingTemplate;

    private int counter = 0;

    public LobbyController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/lobby/createRoom")
    public void createRoom(Principal principal, CreateRoomRequest request) {

        GameRoom room = createRoom(request, principal.getName());

        // notify game creator (requester) that room was created
        LobbyEvent userResponse = new LobbyEvent(LobbyEventType.ROOM_CREATED, room);
        messagingTemplate.convertAndSendToUser(principal.getName(), "/queue/updates", userResponse);

        // notify lobby that a room was opened
        LobbyEvent event = new LobbyEvent(LobbyEventType.ROOM_OPENED, room);
        messagingTemplate.convertAndSend("/topic/lobby", event);

    }

    private GameRoom createRoom(final CreateRoomRequest request, final String username) {
        GameRoom gameRoom = new GameRoom();

        gameRoom.setGameType(request.getGameType());
        gameRoom.setCreatedBy(username);
        gameRoom.setRoomId(UUID.randomUUID().toString());
        gameRoom.setPlayersJoined("1/4");

        return gameRoom;
    }


//    @Scheduled(cron = "*/3 * * * * *")
//    public void createAndSendRoomPeriodically() {
//
//        GameRoom createdRoom = new GameRoom();
//        createdRoom.setRoomId(Integer.toString(counter++));
//        createdRoom.setPlayersJoined("3/4");
//        createdRoom.setGameType("MURLAN");
//        createdRoom.setCreatedBy("Loni");
//        LobbyEvent event = new LobbyEvent(LobbyEventType.ROOM_OPENED, createdRoom);
//
//        messagingTemplate.convertAndSend("/topic/lobby", event);
//    }
}
