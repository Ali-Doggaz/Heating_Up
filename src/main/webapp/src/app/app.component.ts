import { Component } from '@angular/core';
// @ts-ignore
import * as Stomp from 'stompjs';
// @ts-ignore
import * as SockJS from 'sockjs-client';
// @ts-ignore
import $ from 'jquery';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  public title = 'Balayage_UI';
  private stompClient: any;
  private serverUrl = 'http://localhost:8080/socket'
  constructor(){
    this.initializeWebSocketConnection();
  }

  initializeWebSocketConnection(){
    let ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    let that = this;
    this.stompClient.connect({}, function(frame: any) {
      that.stompClient.subscribe("/JobStatus", (message: any) => {
        if(message.body) {
          //$(".chat").append("<div class='message'>"+message.body+"</div>")
          console.log(message.body);
        }
      });
    });
  }

}
