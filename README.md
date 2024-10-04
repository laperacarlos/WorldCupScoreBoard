## Running

* Build the project (incl. running all unit tests) using command:
    ```
    mvn clean install 
    ```
  Project will be packed to single .jar file which can be found in ..\target directory.

* It is possible to run unit tests separately using command:
    ```
    mvn test 
    ```
## Assumptions

* Project offers implementation to process World Cup game data. Incoming data should be mapped to domain class Game equipped in necessary fields to handle required operations. 
  Class Game has implemented no arguments constructor and getters & setters that it can be easily mapped from database entity using ORM (e.g. JPA, Hibernate) after entry update or after small adjustments
  it can easily be used to map values from different formats (e.g. JSON, XML).
* To start game names of home and away teams are required, as well as flag isLive set to true.
* To update game home and away scores are required, as well as flag isLive set to true.
* To end game final scores are required, as well as flag isLive set to false. When game ended, moved to finished games.
  
