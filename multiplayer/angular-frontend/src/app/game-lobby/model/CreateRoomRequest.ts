export class CreateRoomRequest {
  requestId: string;
  gameType: string;

  constructor(requestId: string, gameType: string) {
    this.gameType = gameType;
  }
}
