package com.example.Balayage.report;

import com.example.Balayage.regles.statsExceptions.StatsException;
import com.example.Balayage.regles.statsRegles.StatsRegle;
import net.sf.jasperreports.engine.*;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class ScanReportGenerator {
    final String templatePath = "src\\main\\resources\\clientReport.jrxml";

    private JasperReport jasperReport;
    public ScanReportGenerator(){
        try {
            //Compiler le rapport JR
            File file = ResourceUtils.getFile(templatePath);
            jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        }
        catch (JRException e1) {
            e1.printStackTrace();
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            System.out.println("Template du rapport introuvable au chemin: " + "src\\main\\resources\\clientReport.jrxml");
        }
    }

    public void generateReport( ArrayList<StatsRegle> statsRegles, ArrayList<StatsException> statsExceptions,
                               int batchNumber, Long nbrClientsAnalysed) throws IOException {
        ClientReportInput clientReportInput = ClientReportDataAssembler.assemble( statsRegles, statsExceptions, nbrClientsAnalysed);

        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                    clientReportInput.getParameters(), new JREmptyDataSource());
            //generate report
            JasperExportManager.exportReportToPdfFile(jasperPrint, "src\\main\\resources\\Final_Reports\\"+
                    clientReportInput.getReportTitle() +" batch-" +batchNumber +".pdf");
        } catch (JRException e) {
            e.printStackTrace();
        }

    }

}
