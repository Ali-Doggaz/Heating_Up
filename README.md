# Heating Up [![Greetings](https://github.com/larkinds/EthicalTargeting/actions/workflows/greetings.yml/badge.svg)](https://github.com/larkinds/EthicalTargeting/actions/workflows/greetings.yml)

## Inspiration
Climate change disasters are responsible for the death of around 5 million people per year. Almost 10% of global deaths can be attributed to abnormally hot or cold temperatures, according to new research linking extreme weather to mortality.

A large disaster is sometimes not predicted in advance which causes many deaths as people find themselves  unprepared. In order to counter this problem, we thought about an application that uses data from different sources and keeps the user safe by notifying him/her if there's a high risk of an incident occurring in the city he lives in.

## What it does
Our app is a tool that will keep you constantly updated about climate change consequences in your area. 

Our backend fetches multiple APIs and public datasets (see **Source** category below) to obtain climatic variables (temperature, humidity, wind direction and speed, water level, air quality index, etc...) specific to your region. 

It then passes these variables to a battery of tests, that look for all kind of climate/ecological problems such as air pollution, wildifres near you, brutal increases of temperatures in the next few days, (etc ...) and sends you an alert for every problem we find!

This way, people can **prepare themselves to big heat waves, stay informed of local wildfires, sea level increase, and more generally, they can be more aware of how climate is changing in their area.**


### App Demo
[![Watch the video](https://img.youtube.com/vi/PdGPmb-Gd00/maxresdefault.jpg)](https://youtu.be/PdGPmb-Gd00)

## Development

**Technologies used**  
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Maven](https://maven.apache.org/)
- [Tomcat](https://tomcat.apache.org/)
- [elenctronJs](https://www.electronjs.org/)
- [React Js](https://fr.reactjs.org/)
- [npm](https://www.npmjs.com/)


## Challenges we ran into
* **First time creating a desktop app:** It was difficult to learn so many new technologies in just 24 hours and to develop a fully functional project, but we had fun and learned many new things! 
* **Finding relevant datasets and API:** We had an entire subteam of 3 members dedicated to searching for relevant datasets. Many data found online was inaccurate or didn't provide the exact informations we needed. Therefore, we had to spend a good amount of time looking things up and figuring out which APIs and datasets to use

## Accomplishments that we're proud of
- Learning how to develop a desktop app in such a short time span
- Using the java built-in JSON serializer was tricky.
- The app works very smoothly (no lag at all), and the backend is doing it's job! We didn't expect it to work that well to be honest.

## What we learned
- Spring 
- React
- Electron.js
- Desktop app development

## What's next for Ethical Targeting
- Finalizing this prototype version
- Add advanced search for other cities/countries, and correcting some bugs (+- 1month)
- Release the beta version of our app and invite a limited number of beta testers
- Collect their feedback and implement them in our app, while enhancing the UI and the server's performance
- Release our app and start working on a website and a mobile app as well

## Contributors
* Salsabil Houij
* Dali Sahnoun
* Aicha Khalfaoui
* Yasine Ben Slimene
* Ali Doggaz

## Sources
- https://docs.ambeedata.com/#aq-intro
- https://droughtmonitor.unl.edu/
- http://www.airqualityontario.com/science/aqi_description.php
- https://www.airnow.gov/aqi/aqi-basics/
- https://www.cdc.gov/pictureofamerica/pdfs/picture_of_america_heat-related_illness.pdf
- https://www.lagons-plages.com/humidite-tropiques.php
- https://m3o.com/weather/api#Now

