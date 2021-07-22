import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import {FormControl, NgForm} from '@angular/forms';
import { BatchService } from 'src/app/batch.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { batchConfig } from 'src/app/batchConfig';

//@ts-ignore
import $ from "jquery";

interface Window {

  getNewCronExpression() : string;

}

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],

})
export class HomeComponent implements OnInit {
  minutesArray = new Array(60);
  hoursArray = new Array(24);
  daysArray = new Array(31);

  cronExpression="";
  chunkSize = new FormControl();
  pageSize = new FormControl();
  nbrClientsParRapport = new FormControl();

  //Liste des configuration/plannifications
  public batchConfigs : batchConfig[] | undefined;
  // @ts-ignore
  public deleteConfig: batchConfig;

  constructor(private batchService: BatchService) { }

  ngOnInit(): void {
    this.getConfigs()
  }

  //recupere toutes les configurations/plannifications depuis la backend
  public getConfigs(){
    this.batchService.getConfigs().subscribe(
      (response: batchConfig[]) => {
        this.batchConfigs = response;
        console.log(this.batchConfigs);
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public addConfig(changeConfForm: NgForm):void{
    let newConfig = new batchConfig(this.chunkSize.value, this.pageSize.value, this.nbrClientsParRapport.value, this.cronExpression);
    this.batchService.addConfig(newConfig).subscribe(
      (response: String) => {
        console.log(response);
        //Colorier les input fields et le form de selection de la cronExpression
        //en vert, pour indiquer le succes de modification de la configuration
        if (response.indexOf("SUCCES")) {
          let inputFields = $('input')
          let cronBoxes = ($('.cronbox'))
          for (let element of cronBoxes) {
            element.classList.add('success')
            setInterval(
              function () {
                element.classList.remove('success')
              }, 3000
            )
          }
          for (let element of inputFields) {
            element.classList.add('success')
            setInterval(
              function () {
                element.classList.remove('success')
              }, 3000
            )
          }
        }
        this.getConfigs()
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

  isScanEnCours() {
    status = $("#scanStatus p").text()
    if (status.indexOf("Balayage terminé")===-1 && status.indexOf("Aucun balayage")==-1){
      return true;
    }
    return false;
  }

  //Plannificateur

  getChoice(name: String) {
    let chosen = '';
    let all_selected = [];
    let options = [];
    options = $("#"+name + " option");


    for ( var index=options.length-1 ; index >= 0; --index) {
      //console.log(a.options.length);

      if(options[index].selected) {
        if( options[index].value == '*' ) {
          chosen = '*';
          return chosen;
        }
        all_selected.push(options[index].value);
      }
    }

    if(all_selected.length)
      chosen = all_selected.join(",");
    else
      chosen = '*';
    return chosen;
  }

  get_new_cronExpression() {
    var minute, hour, day, month, weekday, cmd;

    minute	= this.getChoice('minute');
    hour	= this.getChoice('hour');
    day		= this.getChoice('day');
    month	= this.getChoice('month');
    weekday	= this.getChoice('weekday');
    // @ts-ignore
    this.cronExpression = "0"+ " " +minute + " " + hour + " " + day + " " + month + " " + weekday + " ";

  }

  public onDeleteConfig(employeeId: number): void {
    this.batchService.deleteConfig(employeeId).subscribe(
      (response: String) => {
        console.log(response);
        this.getConfigs();
      },
      (error: HttpErrorResponse) => {
        alert(error.message);
      }
    );
  }

  public onOpenModal(config: batchConfig): void {
    const container = document.getElementById('main-container');
    const button = document.createElement('button');
    button.type = 'button';
    button.style.display = 'none';
    button.setAttribute('data-toggle', 'modal');
    button.setAttribute('data-target', '#deleteConfigModal');
    //Enregistrer la configuration a supprimer
    this.deleteConfig = config;

    container.appendChild(button);
    button.click();
  }

}

