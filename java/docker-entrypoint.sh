#!/bin/bash
set -eu

# you can do arbitrary things here in the startup script
java -version

# let's see who the container is running as
id

# but make sure you exec the final run command
exec java -cp /apps/my-app-1.0-SNAPSHOT.jar com.mycompany.app.App
