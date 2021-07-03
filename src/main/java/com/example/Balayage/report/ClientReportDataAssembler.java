package com.example.Balayage.report;


import com.example.Balayage.regles.ClientTestResult;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class ClientReportDataAssembler {

    // Remplir les informations necessaires pour la generation du rapport
    //(titre, Nom de la banque, stats des clients testés, liste des suspects)
    public static ClientReportInput assemble(ArrayList<ClientTestResult> clientsSuspects) {
        ClientReportInput ClientReportInput = new ClientReportInput();
        ClientReportInput.setReportTitle("Rapport De Balayage Du " +
                (java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy")))
        );
        ClientReportInput.setStats(ClientTestResult.getStatsReport());

        JRBeanCollectionDataSource clientDataSource = new JRBeanCollectionDataSource(clientsSuspects);
        ClientReportInput.setClientDataSource(clientDataSource);

        return ClientReportInput;
    }
}
