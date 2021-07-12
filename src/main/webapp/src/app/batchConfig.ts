export class batchConfig {
  chunkSize=0;
  pageSize=0;
  cronExpression="";

  constructor(chunkSize: number, pageSize: number, cronExpression: string) {
    this.chunkSize = chunkSize;
    this.pageSize = pageSize;
    this.cronExpression = cronExpression;
  }
}
