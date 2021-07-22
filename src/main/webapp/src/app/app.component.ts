import {Component, OnInit} from '@angular/core';
// @ts-ignore
import * as Stomp from 'stompjs';
// @ts-ignore
import * as SockJS from 'sockjs-client';
// @ts-ignore
import $ from 'jquery';
import {environment} from "../environments/environment";
import {batchConfig} from "./batchConfig";
import { BatchService } from 'src/app/batch.service';
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  public title = 'Balayage_UI';
  private stompClient: any;
  private serverUrl = environment.apiBaseUrl+"/socket";

  constructor(private batchService: BatchService){}

  ngOnInit() {
    this.initializeWebSocketConnection();
  }

  initializeWebSocketConnection(){
    let ws = new SockJS(this.serverUrl);
    this.stompClient = Stomp.over(ws);
    let that = this;
    this.stompClient.connect({}, function(frame: any) {
      //Modifier le status du job lorsqu'on le backend nous envoie son nouveau status
      that.stompClient.subscribe("/JobStatus", (message: any) => {
        if(message.body) {
          $("#scanStatus p").text(message.body)
        }
      });
    });
  }

}
