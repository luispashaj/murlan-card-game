export class GameRoom {
  public roomId: string;
  public gameType: string;
  createdBy: string;
  playersJoined: string;

  constructor(roomId: string, gameType: string, createdBy: string, playersJoined: string) {
    this.roomId = roomId;
    this.gameType = gameType;
    this.createdBy = createdBy;
    this.playersJoined = playersJoined;
  }
}
