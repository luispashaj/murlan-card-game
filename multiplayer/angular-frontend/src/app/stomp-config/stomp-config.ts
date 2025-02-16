import { InjectableRxStompConfig} from '@stomp/ng2-stompjs';

export const stompConfig: InjectableRxStompConfig = {
  brokerURL: 'ws://localhost:8080/game',

  // How often to heartbeat?
  // Interval in milliseconds, set to 0 to disable
  heartbeatIncoming: 0, // Typical value 0 - disabled
  heartbeatOutgoing: 20000, // Typical value 20000 - every 20 seconds

  // Wait in milliseconds before attempting auto reconnect
  // Set to 0 to disable
  // Typical value 500 (500 milli seconds)
  reconnectDelay: 3000
};
