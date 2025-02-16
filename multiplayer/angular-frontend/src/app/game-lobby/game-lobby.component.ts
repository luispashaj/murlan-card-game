import {Component, OnDestroy, OnInit} from '@angular/core';
import {GameRoom} from './model/GameRoom';
import {RxStompService} from '@stomp/ng2-stompjs';
import {RxStompState} from '@stomp/rx-stomp';
import {LobbyEvent} from './model/LobbyEvent';
import {Subscription} from 'rxjs/internal/Subscription';
import {Message} from '@stomp/stompjs';
import {LobbyEventType} from './model/LobbyEventType';
import {CreateRoomRequest} from './model/CreateRoomRequest';
import {Router} from '@angular/router';

@Component({
  selector: 'app-game-lobby',
  templateUrl: './game-lobby.component.html',
  styleUrls: ['./game-lobby.component.css']
})
export class GameLobbyComponent implements OnInit, OnDestroy {
  // list of opened game rooms
  gameRooms: GameRoom[] = []; // = [new GameRoom('12345', 'Murlan', 'user123', '2/4')];

  // lobby topic subscription
  lobbySubscription: Subscription;

  // user queue subscription
  userQueueSubscription: Subscription;
  // connection status
  connectionStatus: Subscription;

  // request id counter
  requestCounter: number;

  constructor(private stompService: RxStompService,
              private router: Router) {
  }

  ngOnInit(): void {
    // init request counter
    this.requestCounter = 0;

    // HANDLE CONNECTION ERRORS HERE
    this.connectionStatus = this.stompService.connectionState$.subscribe((state: RxStompState) => {
      switch (state) {
        case RxStompState.CONNECTING: {
          break
        }
        default: {
          break;
        }
      }
      console.log(state);
    });

    this.userQueueSubscription = this.stompService.watch('/user/queue/updates').subscribe((message: Message) => {
      const lobbyEvent: LobbyEvent = JSON.parse(message.body);
      this.handleLobbyEvent(lobbyEvent);
    })

    //
    this.lobbySubscription = this.stompService.watch('/topic/lobby').subscribe((message: Message) => {
      const lobbyEvent: LobbyEvent = JSON.parse(message.body);
      this.handleLobbyEvent(lobbyEvent);
    })
  }

  ngOnDestroy(): void {
    this.lobbySubscription.unsubscribe();
  }

  navigate() {
    this.router.navigate(['/room']);
    // this.lobbySubscription = this.stompService.watch('/topic/lobby').subscribe((message: Message) => {
    //   console.log('subscribed to /lobby')
    // })
  }

  createRoom() {

    const request: CreateRoomRequest = new CreateRoomRequest(this.requestCounter.toString(), 'Murlan');
    // increase counter
    this.requestCounter++;

    // this.stompService.publish({destination: '/app/lobby/createRoom', body: JSON.stringify(request)});
    // test
    this.stompService.publish({destination: '/topic/lobby', body: JSON.stringify('')})
  }

  private handleLobbyEvent(event: LobbyEvent) {
    switch (event.eventType) {
      case LobbyEventType.INITIAL: {
        this.gameRooms.push(...event.gameRooms);
        break;
      }
      case LobbyEventType.ROOM_OPENED: {
        this.gameRooms.push(event.gameRooms[0]);
        break;
      }
      case LobbyEventType.ROOM_CLOSED: {
        const toRemoveIndex: number = this.gameRooms.findIndex(gameRoom => gameRoom.roomId === event.gameRooms[0].roomId);
        if (toRemoveIndex > -1) {
          this.gameRooms.splice(toRemoveIndex, 1);
        }
        break;
      }
      case LobbyEventType.ROOM_CREATED: {
        const data = {
          roomId: event.gameRooms[0].roomId
        }

        // pass roomId to subscribe
        this.router.navigate(['/room', JSON.stringify(data)]);
        break;
      }
      default: {
        break;
      }
    }
  }
}
