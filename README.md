# Loyola ETL Scalatra Hello App #

Example involving Swagger and deployment via foreperson.

## Build & Run ##

```sh
$ cd sensorproxy-scalatra-scala
$ sbt
> container:start
> ~ ;copy-resources;aux-compile
> browse
```

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.

## Deployment ##

Deployable to A/PaaS providers that support foreperson or war files.