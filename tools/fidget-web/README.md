Fidget Web App
==============
This is a Web GUI for File IDentification GEnerator and Tester.  The aim is
to provide a site to facilitate the development and testing of new file
format signatures.

The project is aimed at anyone who's interested in developing file format
signatures.

Status
------
This project is currently in development, and there's no supported release.

Running fidget-web
------------------
The fidget-web project comes bundled with it's own Jetty server, in fact it
uses http://dropwizard.codahale.com under the hood.  The project is also maven
based so can be built and run as so:
```
mvn package

java -jar fidget-web.jar
```
Then open http://localhost:8080/fidget.....

ToDo
----
- [ ] Improve build and run instructions
- [ ] Provide example
