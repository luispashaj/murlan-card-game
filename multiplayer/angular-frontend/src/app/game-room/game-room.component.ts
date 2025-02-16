import { Component, OnInit } from '@angular/core';
import { GameRoomService } from './game-room.service';
import { Card } from '../card/model/Card';
import {RxStompService} from '@stomp/ng2-stompjs';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-game-room',
  templateUrl: './game-room.component.html',
  styleUrls: ['./game-room.component.css'],
  providers: [GameRoomService]
})
export class GameRoomComponent implements OnInit {

  public listPlayerCards: Array<Card>;
  public southPlayerCard: Card;
  public readonly northPlayerCard: Card;
  public readonly eastPlayerCard: Card;
  public readonly westPlayerCard: Card;

  constructor(private gameRoomService: GameRoomService,
              private rxStompService: RxStompService,
              private router: Router,
              private route: ActivatedRoute) { }

  ngOnInit() {
    // this.listPlayerCards = this.gameRoomService.getPlayerCards();

    const roomId = this.route.snapshot.params['roomId'];


  }

  clickedCard(card: Card) {
    this.gameRoomService.clickedCard(card);
    this.southPlayerCard = card;
    this.listPlayerCards.splice(this.listPlayerCards.indexOf(card), 1);
  }

}
