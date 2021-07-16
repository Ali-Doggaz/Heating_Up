export class batchConfig {
  chunkSize=0;
  pageSize=0;
  nbrClientsParRapport=0;
  cronExpression="";

  constructor(chunkSize: number, pageSize: number, nbrClientsParRapport: number, cronExpression: string) {
    this.chunkSize = chunkSize;
    this.pageSize = pageSize;
    this.nbrClientsParRapport = nbrClientsParRapport;
    this.cronExpression = cronExpression;
  }
}
