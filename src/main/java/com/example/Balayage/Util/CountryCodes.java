package com.example.Balayage.Util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class CountryCodes {
    private Map<String, String> localeMap;

    private void initCountryCodeMapping() {
        String[] countries = Locale.getISOCountries();
        System.out.println(countries[0]);

        localeMap = new HashMap<String, String>(countries.length);
        for (String country : countries) {
            Locale locale = new Locale("", country);
            String country_name = locale.getDisplayCountry();
            localeMap.put(country_name, locale.getISO3Country().toUpperCase());
        }
    }

    public String getCode(String country){
        String countryFound = localeMap.get(country);
        return countryFound;
        //TODO take into account the case where the country isn't found
    }

}