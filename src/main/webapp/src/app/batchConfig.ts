export class batchConfig {
  id;
  chunkSize;
  pageSize;
  nbrClientsParRapport;
  cronExpression;


  constructor(chunkSize: number, pageSize: number, nbrClientsParRapport: number, cronExpression: string, id: number = -1) {
    this.id = id;
    this.chunkSize = chunkSize;
    this.pageSize = pageSize;
    this.nbrClientsParRapport = nbrClientsParRapport;
    this.cronExpression = cronExpression;
  }
}
