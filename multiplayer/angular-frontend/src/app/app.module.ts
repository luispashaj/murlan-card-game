import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';

import { InjectableRxStompConfig, RxStompService, rxStompServiceFactory } from '@stomp/ng2-stompjs';

import { AppComponent } from './app.component';
import { GameLobbyComponent } from './game-lobby/game-lobby.component';
import { GameRoomComponent } from './game-room/game-room.component';
import { CardComponent } from './card/card.component';
import { stompConfig } from './stomp-config/stomp-config';
import { GameLobbyNewComponent } from './game-lobby-new/game-lobby-new.component';
import { ConnectedPlayersComponent } from './game-lobby-new/connected-players/connected-players.component';
import { ChatComponent } from './game-lobby-new/chat/chat.component';
import { CurrentGamesComponent } from './current-games/current-games.component';

@NgModule({
  declarations: [
    AppComponent,
    GameRoomComponent,
    CardComponent,
    GameLobbyComponent,
    GameLobbyNewComponent,
    ConnectedPlayersComponent,
    ChatComponent,
    CurrentGamesComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forRoot([
      {
        path: '',
        component: GameLobbyNewComponent
      },
      {
        path: 'room',
        component: GameRoomComponent
      }
    ]),
    BrowserAnimationsModule
  ],
  providers: [
    {
      provide: InjectableRxStompConfig,
      useValue: stompConfig
    },
    {
      provide: RxStompService,
      useFactory: rxStompServiceFactory,
      deps: [InjectableRxStompConfig]
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
