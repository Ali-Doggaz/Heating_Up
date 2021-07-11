import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import { BatchService } from 'src/app/batch.service';
//@ts-ignore
import $ from "jquery";


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],

})
export class HomeComponent implements OnInit {
  minutesArray = new Array(60);
  hoursArray = new Array(24);
  daysArray = new Array(31);

  constructor(private batchService: BatchService) { }

  ngOnInit(): void {
  }

  public changeConfig(changeConfForm: NgForm):void{
    console.log(changeConfForm.value)
    this.batchService.changeConfig(changeConfForm.value).subscribe(
      (response: String) => {
        console.log(response);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public startScan(): void{
    status = $("#scanStatus p").text();
    if (status.indexOf("Balayage terminé")==-1 && status.indexOf("Aucun balayage")==-1)
      alert("Veuillez attendre la fin du balayage en cours...")
    else {
      this.batchService.startScan().subscribe(
        (response: String) => {
          console.log(response);
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
      );
    }
  }

  public stopScan(): void {
    status = $("#scanStatus p").text();
    if (status.indexOf("Balayage en cours") == -1)
      alert("Aucun balayage n'est en cours...")
    else {
      this.batchService.stopScan().subscribe(
        (response: String) => {
          console.log(response);
        },
        (error: HttpErrorResponse) => {
          alert(error.message);
        }
      );
    }
  }

  showLog(){
    //TODO implement this
  }

  isScanEnCours() {
    status = $("#scanStatus p").text()
    if (status.indexOf("Balayage terminé")===-1 && status.indexOf("Aucun balayage")==-1){
      return true;
    }
    return false;
  }
}

