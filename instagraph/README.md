# Instagraph

To get started, start the API and the SPA.

To start the API, do:

```bash
./gradlew :api:bootRun
```

To start the SPA, do:

```bash
cd spa
npm install
ng serve
```

Then navigate to http://localhost:4200 and login as "candice@example.org/password".

Note that this state of the application is without the "Bonus" module.
Because of that, for the moment you must log in again on each restart and each time the Angular app is updated.
This is because the JWT is held in-memory.
This would be removed in the event that I add an Authorization Server.