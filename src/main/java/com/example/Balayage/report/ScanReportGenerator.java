package com.example.Balayage.report;

import com.example.Balayage.regles.statsExceptions.StatsException;
import com.example.Balayage.regles.statsRegles.StatsRegle;
import net.sf.jasperreports.engine.*;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ScanReportGenerator {
    public void generateReport( ArrayList<StatsRegle> statsRegles, ArrayList<StatsException> statsExceptions,
                               int batchNumber, Long nbrClientsAnalysed) throws IOException {
        ClientReportInput clientReportInput = ClientReportDataAssembler.assemble( statsRegles, statsExceptions, nbrClientsAnalysed);

        try {

            //load file and compile it
            File file = ResourceUtils.getFile("src\\main\\resources\\clientReport.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                    clientReportInput.getParameters(), new JREmptyDataSource());
            //generate report
            JasperExportManager.exportReportToPdfFile(jasperPrint, "src\\main\\resources\\Final_Reports\\"+
                    clientReportInput.getReportTitle() +" batch-" +batchNumber +".pdf");
        } catch (JRException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
