package com.murlan.game.shared.model;

public class GameRoom {
    private String roomId;
    private String gameType;
    private String createdBy;
    private String playersJoined;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getPlayersJoined() {
        return playersJoined;
    }

    public void setPlayersJoined(String playersJoined) {
        this.playersJoined = playersJoined;
    }
}
