import {LobbyEventType} from './LobbyEventType';
import {GameRoom} from './GameRoom';

export class LobbyEvent {
  eventType: LobbyEventType;
  gameRooms: GameRoom[];
}
