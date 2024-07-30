# F1Tracker 
This is an application made exclusively for Android phones. 
## Layout
The app consists of four different pages:
1. A calendar with all the races taking place in the current season.
1. Standings for both drivers and constructors championships.
1. An interactive map where the user can see all the different circuits in the world of the current season.
1. A diary page where the user can write down their thoughts after or while watching the race.

## Development
This app is entirely written in Java and was built using the Android Studio IDE.
Two APIs have been used in this app:
1. API-Formula-1 to retrieve information such as the ace calendar and the standings.
1. Google Maps API to display the circuits on the map.

The last page consists of a SQLite database which saves the user diary on the device's internal storage. The benefit of encapsulating each diary page as a database table is that each page can be independently modified or deleted without the fear of editing a different page or deleting the entire diary by accident.
