# Video Streaming API
This API is the backend we use for our video streaming apps at Whitespell.

##Documentation
All documentation is available on:

http://whitespell.github.io/slate/


##Instructions
In order to get started, import the DDL (ddl/peak.sql) in your database and run the server by running:

```./run.sh bin```

Build by using:

```./build.sh bin```

Also edit the values in tests.prop (test properties), config.prop (production properties) and config-dev.prop (dev properties) with your database details and other configs in order to get your service running.

In order to make it automatically spin up Google Cloud nodes with the video converter service, you will need to install the google cloud CLI, log in, and create a disk with the video converter service that it automatically spins up.


##API Video
To see the app this API featured, see:

https://drive.google.com/file/d/0BwsMM5hElkXESW0xMHFvMG9Ic1U/view

Credits to Cory McAn & Pim de Witte for building this. 