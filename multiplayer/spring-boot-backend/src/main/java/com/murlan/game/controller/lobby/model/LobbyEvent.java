package com.murlan.game.controller.lobby.model;

import com.murlan.game.shared.model.GameRoom;

import java.util.ArrayList;
import java.util.List;

public class LobbyEvent {

    private LobbyEventType eventType;
    private List<GameRoom> gameRooms;

    public LobbyEvent() {}

    public LobbyEvent(LobbyEventType eventType, List<GameRoom> gameRooms) {
        this.eventType = eventType;
        this.getGameRooms().addAll(gameRooms);
    }

    public LobbyEvent(LobbyEventType eventType, GameRoom gameRoom) {
        this.eventType = eventType;
        this.getGameRooms().add(gameRoom);
    }

    public List<GameRoom> getGameRooms() {
        if(gameRooms == null) {
            gameRooms = new ArrayList<>();
        }
        return gameRooms;
    }

    public void setGameRooms(List<GameRoom> gameRooms) {
        this.gameRooms = gameRooms;
    }

    public LobbyEventType getEventType() {
        return eventType;
    }

    public void setEventType(LobbyEventType eventType) {
        this.eventType = eventType;
    }
}
