package com.example.Balayage.Util;

import com.example.Balayage.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.Balayage.client.*;

@RestController
@CrossOrigin()
@RequestMapping("/Heating/")
public class Controller {

    @Autowired
    public ClientRepository clientRepository;

    @Autowired
    public Controller(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    //inscription client REST API
    @PostMapping("/user")
    public Client createUser (@RequestBody Client client){
        System.out.println(client.getCity());
        return clientRepository.save(client);
    }

    @GetMapping("/getNews")
    @ResponseBody
    public String getAlertsForRegion(@RequestParam String city, @RequestParam String country, @RequestParam String email){

         return "City: " + city + " Country: " + country;
    }
}
