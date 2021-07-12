package com.example.Balayage.batch;

public class BatchConfigParams {
    private int chunkSize;
    private int pageSize;
    private String cronExpression;

    public int getChunkSize() {
        return chunkSize;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getCronExpression() {
        return cronExpression;
    }
}
