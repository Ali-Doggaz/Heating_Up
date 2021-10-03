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


## Try it out
**Chrome extension**  
1. [Download the extension](https://github.com/larkinds/EthicalTargeting/releases)
1. Unzip it into a folder you won't delete until you uninstall the extension
1. Open `chrome://extensions` and switch on developer mode
1. Drag the folder into the Chrome extensions tab
1. Enjoy!

Ad management UI: <https://glz5x-riaaa-aaaab-aa6bq-cai.ic0.app>

## Development

**Prerequisites**  
- [Yarn](https://yarnpkg.com/)
- [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/install-win10) if building on Windows, due to a Bash build script

Building is straightforward:

```sh
git clone https://github.com/larkinds/EthicalTargeting.git
cd EthicalTargeting
yarn install
yarn build
```

## How we built it
The backend service runs on the [DFINITY Internet Computer](https://dfinity.org/), a decentralized network of independent data centers enabling a public ad network that is:

* **Transparent:** anyone can see what ads are in the ledger (past/present/future) and who funded them  
* **Private:** nobody will know who clicked on an ad or what their interests/demographics are  
* **Targeted:** actually relevant to the user, making them more useful to the user and effective for the advertiser  
* **Truly open:** not stored in any one data center and controlled by community governance in the future

Browsers running our extension pull the entire ledger of ads from the backend service, and filter them through the user's current profile to find ads that are relevant and useful to that specific person.

The extension is built with React, Tailwind CSS, HTML, and vanilla JavaScript.

## Challenges we ran into
* **Motoko:** The first-party language of the Internet Computer, at just 17 months old, is missing a lot of standard library functions and features common in older languages. Learning a new language from incomplete documentation was a welcome challenge.
* **Chrome extension documentation:** Not to be outdone by Motoko, Chrome's extension API has incomplete and inconsistent documentation. Shout-out to Stack Overflow and our fellow pod members for helping us figure out the API!
* **Webpage categorization:** Big Tech has put millions of dollars into how they infer our interests, and attempting to recreate that in just 3 weeks was a daunting task. We're proud of how well our demo interests generator works.
* **Extension bundling:** Chrome extensions have additional restrictions on what libraries and npm packages can be used, resulting in confusing errors and convoluted bundling setups.

## Accomplishments that we're proud of
- Managing to finish such a large and complex project in just 3 weeks
- Writing a JSON serializer from scratch in Motoko, without type introspection
- The extension is able to infer user interests a lot better than expected
- Learning Chrome's Extension API and the Internet Computer environment
- As a team, we met every day and organized several pair programming sessions

## What we learned
- Motoko (and the Internet Computer)
- React
- Chrome's Extension API
- Webpack (though we ended up not using it)

## What's next for Ethical Targeting
- Finalize multi-profile support
- Add a way to disable tracking for a certain period of time, instead of having to reactivate it each time
- Add a blocklist of websites where your activity won't get tracked
- Add monetization
- Make the weight of the interests appear in the user's profile
- Finish developing the ad management console (including login system)

## Contributors
* [Larkin Smith](https://github.com/larkinds)
* [Ali Doggaz](https://github.com/Ali-Doggaz)
* [Prabal Chhatkuli](https://github.com/prabalchhatkuli)
* [James Martindale](http://github.com)

### Ethical Targeting backend service
[@jkmartindale/ethical_targeting-backend](https://github.com/jkmartindale/ethical_targeting-backend)
