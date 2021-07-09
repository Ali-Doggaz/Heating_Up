import { Component, OnInit } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { NgForm } from '@angular/forms';
import { BatchService } from 'src/app/batch.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],

})
export class HomeComponent implements OnInit {

  constructor(private batchService: BatchService) { }

  ngOnInit(): void {
  }

  public changeConfig(changeConfForm: NgForm):void{

  }

  public startScan(): void{
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
