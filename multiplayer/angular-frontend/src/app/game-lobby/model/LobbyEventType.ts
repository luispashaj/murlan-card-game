export enum LobbyEventType {
  INITIAL = 'INITIAL',
  ROOM_OPENED = 'ROOM_OPENED',
  ROOM_CLOSED = 'ROOM_CLOSED',
  // expected after CreateRoomRequest is sent
  ROOM_CREATED = 'ROOM_CREATED'
}
