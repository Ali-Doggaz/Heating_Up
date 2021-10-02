import countries from "../data/countries";

export default class Countries {

    static getCountries(){
        return countries
    }

    static getStates(countryName){
        const foundOne = countries.find(country=>country.country===countryName);
        if(!foundOne) return []
        return foundOne.states;
    }

}