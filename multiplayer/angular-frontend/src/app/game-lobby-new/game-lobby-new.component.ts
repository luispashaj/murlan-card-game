import { Component, OnInit } from '@angular/core';
import {GameRoom} from '../game-lobby/model/GameRoom';

@Component({
  selector: 'app-game-lobby-new',
  templateUrl: './game-lobby-new.component.html',
  styleUrls: ['./game-lobby-new.component.css']
})
export class GameLobbyNewComponent implements OnInit {

  gameRooms: GameRoom[] = [new GameRoom('12345', 'Murlan', 'user123', '2/4')];

  constructor() { }

  ngOnInit(): void {
  }

}
