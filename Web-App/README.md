Web Demo
===============
This demo uses Dorset's web api to create a web application. It uses an embedded Jetty web server to serve the pages to your web browser.

Build
-----------
A deployable and executable war is built with:

```
mvn clean package
```

Run
----------
Launch the web application by running:

```
./run.sh
```

and then visiting http://localhost:8888/ with your web browser. Optionally, you can specify the port with

```
./run.sh [port]
```

