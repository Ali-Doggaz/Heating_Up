package com.example.Balayage.report;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.util.HashMap;
import java.util.Map;

public class ClientReportInput {
    private String reportTitle;
    private Long nbr_customers_analyzed;

    private JRBeanCollectionDataSource statsReglesDataSource;
    private JRBeanCollectionDataSource exceptionDataSource;

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public Long getNbr_customers_analyzed() {
        return nbr_customers_analyzed;
    }

    public void setNbr_customers_analyzed(Long nbr_customers_analyzed) {
        this.nbr_customers_analyzed = nbr_customers_analyzed;
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
        parameters.put("nbr_customers_analyzed", getNbr_customers_analyzed());
        parameters.put("exceptionDataSource", getExceptionDataSource());
        //On aura besoin de 4 parametres contenants cette meme datasource (statsRegles)
        //pour generer les graphs
        parameters.put("statsReglesDataSource", getStatsReglesDataSource());
        parameters.put("statsReglesDataSource1", getStatsReglesDataSource().cloneDataSource());
        parameters.put("statsReglesDataSource2", getStatsReglesDataSource().cloneDataSource());
        parameters.put("statsReglesDataSource3", getStatsReglesDataSource().cloneDataSource());
        return parameters;
    }
}