import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GameLobbyNewComponent } from './game-lobby-new.component';

describe('GameLobbyNewComponent', () => {
  let component: GameLobbyNewComponent;
  let fixture: ComponentFixture<GameLobbyNewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GameLobbyNewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GameLobbyNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
