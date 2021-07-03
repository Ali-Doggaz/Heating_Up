package com.example.Balayage.report;

import com.example.Balayage.regles.ClientTestResult;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapArrayDataSource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ScanReportGenerator {
    public void generateReport(ArrayList<ClientTestResult> clientSuspects) throws IOException {
        ClientReportInput clientReportInput = ClientReportDataAssembler.assemble(clientSuspects);

        try {
            //TODO remove
            //JRMapArrayDataSource dataSource = new JRMapArrayDataSource(new Object[]{clientReportInput.getDataSource()});

            //load file and compile it
            //TODO update path
            File file = ResourceUtils.getFile("D:\\MyDesktop\\Algebre\\Vneuron-Balayage_Regles_Metier\\src\\main\\resources\\clientReport.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                    clientReportInput.getParameters(), new JREmptyDataSource());
            //generate report
            JasperExportManager.exportReportToPdfFile(jasperPrint, "src\\main\\resources\\Final_Reports\\"+ clientReportInput.getReportTitle() + ".pdf");
        } catch (JRException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
