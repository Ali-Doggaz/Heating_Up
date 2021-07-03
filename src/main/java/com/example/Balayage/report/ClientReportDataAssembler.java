package com.example.Balayage.report;

import com.example.Balayage.client.Client;
import com.example.Balayage.client.ClientService;
import com.example.Balayage.regles.ClientTestResult;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClientReportDataAssembler {

    @Autowired
    private static ClientService clientService;
    // Remplir les informations necessaires pour la generation du rapport
    //(titre, Nom de la banque, stats des clients testés, liste des suspects)
    public static ClientReportInput assemble(ArrayList<ClientTestResult> clientsSuspects) {
        ClientReportInput ClientReportInput = new ClientReportInput();
        ClientReportInput.setReportTitle("Rapport De Balayage - " + java.time.LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd-MMM-yy"))
        );
        ClientReportInput.setStats(ClientTestResult.getStatsReport());


        JRBeanCollectionDataSource clientDataSource = new JRBeanCollectionDataSource(clientsSuspects, false);
        ClientReportInput.setClientDataSource(clientDataSource);

        return ClientReportInput;
    }
}
