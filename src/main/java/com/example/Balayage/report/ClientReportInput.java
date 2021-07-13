package com.example.Balayage.report;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.HashMap;
import java.util.Map;

public class ClientReportInput {
    private String reportTitle;
    private String stats;

    private JRBeanCollectionDataSource clientDataSource;
    private JRBeanCollectionDataSource statsReglesDataSource;
    private JRBeanCollectionDataSource exceptionDataSource;

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

    public JRBeanCollectionDataSource getStatsReglesDataSource() {
        return statsReglesDataSource;
    }

    public void setStatsReglesDataSource(JRBeanCollectionDataSource statsReglesDataSource) {
        this.statsReglesDataSource = statsReglesDataSource;
    }

    public JRBeanCollectionDataSource getExceptionDataSource() {
        return exceptionDataSource;
    }

    public void setExceptionDataSource(JRBeanCollectionDataSource exceptionDataSource) {
        this.exceptionDataSource = exceptionDataSource;
    }

    public Map<String, Object> getParameters() {
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", getReportTitle());
        parameters.put("STATS", getStats());
        parameters.put("clientDataSource", getClientDataSource());
        //On aura besoin de 3 parametres contenants cette meme datasource
        //pour generer les graphs
        parameters.put("statsReglesDataSource", getStatsReglesDataSource());
        parameters.put("statsReglesDataSource1", getStatsReglesDataSource().cloneDataSource());
        parameters.put("statsReglesDataSource2", getStatsReglesDataSource().cloneDataSource());

        parameters.put("exceptionDataSource", getExceptionDataSource());
        return parameters;
    }
}