package com.example.Balayage.report;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.HashMap;
import java.util.Map;

public class ClientReportInput {
    private String reportTitle;
    private String stats;

    private JRBeanCollectionDataSource clientDataSource;

    public String getStats() { return stats; }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public void setStats(String stats) { this.stats = stats; }

    public JRBeanCollectionDataSource getClientDataSource() {
        return clientDataSource;
    }
    public void setClientDataSource(JRBeanCollectionDataSource clientDataSource) {
        this.clientDataSource = clientDataSource;
    }
    public Map<String, Object> getParameters() {
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("P_REPORT_TITLE", getReportTitle());
        parameters.put("P_STATS", getStats());

        return parameters;
    }
    public Map<String, Object> getDataSources() {
        Map<String,Object> dataSources = new HashMap<>();
        dataSources.put("clientDataSource", clientDataSource);

        return dataSources;
    }
}