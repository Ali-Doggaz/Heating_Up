import {Component, OnInit} from '@angular/core';
// @ts-ignore
import * as Stomp from 'stompjs';
// @ts-ignore
import * as SockJS from 'sockjs-client';
// @ts-ignore
import $ from 'jquery';
import {environment} from "../environments/environment";
import {BatchService} from 'src/app/batch.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  public title = 'Balayage_UI';
  private stompClient: any;
  private serverUrl = environment.apiBaseUrl+"/socket";
  private ws: SockJS;
  private recInterval: any;
  constructor(private batchService: BatchService){}

  ngOnInit() {
    this.connectWebSocket();
  }

  connectWebSocket(){
    this.ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(this.ws);
    let that = this;
    this.stompClient.connect({}, function(frame: any) {
      //Modifier le status du job lorsqu'on le backend nous envoie son nouveau status
      that.stompClient.subscribe("/JobStatus", (message: any) => {
        if(message.body) {
          $("#scanStatus p").text(message.body)
        }
      });
    });
    clearInterval(this.recInterval);
    let this_outer = this;
    this.ws.onclose = function() {
      console.log("Tentative de reconnexion en cours...")
      this_outer.ws = null;
      this_outer.recInterval = setInterval(function() {
        this_outer.connectWebSocket();
      }, 2000);
    };

  }


}
