export class batchConfig {
  id;
  chunkSize;
  pageSize;
  nbrClientsParRapport;
  cronExpression;
  humanReadableCronExpression = "";

  static monthNames = ["*", "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
    "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
  ];

  static daysOfTheWeek = ["Dimanche", "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"];

  constructor(chunkSize: number, pageSize: number, nbrClientsParRapport: number, cronExpression: string, id: number = -1) {
    this.id = id;
    this.chunkSize = chunkSize;
    this.pageSize = pageSize;
    this.nbrClientsParRapport = nbrClientsParRapport;
    this.cronExpression = cronExpression;
    this.generateHumanReadableCronExpression();
  }

  public getMinute() : String{
    let minute = this.cronExpression.split(" ")[1];
    if (minute==="*") return "toutes";
    return minute;
  }

  public getHour() : String{
    let heure = this.cronExpression.split(" ")[2];
    if (heure==="*") return "toutes";
    return heure;
  }

  public getDay(): String{
    let days = this.cronExpression.split(" ")[3];
    if (days==="*") return "Tous";
    return days;
  }

  public getMonth(): String{
    let monthsList = new String("");
    let months = this.cronExpression.split(" ")[4].split(",");
    if (months[0]==="*") return "Tous";
    months.forEach((month, index) => {
      if (index === 0) monthsList = monthsList.concat(batchConfig.monthNames[parseInt(month, 10)])
      else monthsList = monthsList.concat(", ", batchConfig.monthNames[parseInt(month, 10)]);
    })
    return monthsList;
  }

  public getDayOfTheWeek(): String{
    let daysList = "";
    let days = this.cronExpression.split(" ")[5].split(",");
    if (days[0]==="*") return "Tous";
    days.forEach((day, index) => {
      if (index === 0) daysList = daysList.concat(batchConfig.daysOfTheWeek[parseInt(day, 10)])
      else daysList = daysList.concat(", ", batchConfig.daysOfTheWeek[parseInt(day, 10)])
    })

    return daysList;
  }

  public generateHumanReadableCronExpression(): void{
    if(this.cronExpression == "N/A") return;
    this.humanReadableCronExpression =  "Minute: " + this.getMinute() + "\nHeure: " + this.getHour() +"\nJour: " + this.getDay()+
      "\nMois: " + this.getMonth()+"\nJour de la semaine: "+this.getDayOfTheWeek();

    console.log(this.humanReadableCronExpression);

  }
}
