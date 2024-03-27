# M2_PFE_E-Livret

> Project made by :
>
> - Adam FARESSE
> - Mickaël LASCOUTOUNAS
> - Gaëtan TRINCA-PUPET

## Launching the app

> To launch the web application you must :
>
> - Either have docker
> - Or Maven and NPM

### With Docker

> Fetch of build the image for the front and for the back, and start the image in containers.
>
> It is imporant to set a link for the ports of the back and of the front (8080 and 8081).
>
> Run these commands
>
> - `docker run -d -p 8081:8081 --name=”elivret-back” elivret-back`
> - `docker run -d -p 8080:8080 –-name=”elivret-front” elivret-front`

> If you want to build the images, follow these step :
>
> - For the Back, go in the folder "spring-back" and run :
> - - `docker build -t elivret-back:latest .`
>
> - For the Front, go in the folder "vue-front" and run :
> - - `docker build -t elivret-front:latest .`

### With Maven and NPM

> For the back, go in the folder "spring-back" and follow these steps
>
> - `java -classpath hsqldb/lib/hsqldb.jar org.hsqldb.server.Server`
> - Open another console
> - `mvn clean package spring-boot:repackage -DskipTests -Dspring.profiles.active=prod -Dspring.datasource.url=jdbc:hsqldb:hsql://localhost/db`
> - `java -jar -Dspring.profiles.active=prod -Dspring.datasource.url=jdbc:hsqldb:hsql:localhost/db target/M2_PFE-E_LIVRET-1.0-SNAPSHOT.war`

> For the front, go in the folder "vue-front" and follow these steps
>
> - `npm run serve`

## How to access the application

> Simply connect to "localhost://8080"
