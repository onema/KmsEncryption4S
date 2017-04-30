#!/usr/bin/env bash
COMMAND=$1
echo "Executing command $COMMAND"
JAR_NAME='encryption4s.jar'

if [ "$COMMAND" ==  "" ]; then
    echo """usage: build.sh [COMMAND]
        jar                 Compiles and creates a .jar file for the project.
        executable          Creates an executable file that can be used globally.
        install             Install the crypto application in the  '/usr/local/bin/' directory.
        uninstall           Uninstall the crypto application from the '/usr/local/bin/' directory.
        cleanup             Removes jar and crypto file from the current directory.
    """
    exit 0
fi

function create_jar() {
    echo "Compiling using SBT"
    sbt compile
    sbt assembly
    mv target/scala-2.12/$JAR_NAME ./
}

function create_executable() {
    echo "Creating executable"
    MYSELF=`which "$JAR_NAME" 2>/dev/null`
    [ $? -gt 0 -a -f "$JAR_NAME" ] && MYSELF="./$JAR_NAME"
    java=java
    if test -n "$JAVA_HOME"; then
        java="$JAVA_HOME/bin/java"
    fi
    exec "$java" $java_args -jar $MYSELF "$@"
    exit 1
}

function cleanup() {
    echo "Removing ./crypto"
    rm ./crypto
    echo "Removing /encryption4s.jar"
    rm ./encryption4s.jar
}

function create_executable() {
    cat create_executable.sh "$JAR_NAME" > crypto && chmod +x crypto
}
if [ "$COMMAND" == "jar" ]; then
    create_jar
fi

if [ "$COMMAND" == "executable" ]; then
    create_jar
    create_executable
fi

if [ "$COMMAND" == "install" ]; then
    create_jar
    create_executable
    echo "Installing crypto in /usr/local/bin/crypto"
    mv crypto /usr/local/bin/
    cleanup
fi

if [ "$COMMAND" == "uninstall" ]; then
    echo removing "/usr/local/bin/crypto"
    rm /usr/local/bin/crypto
fi

if [ "$COMMAND" == "cleanup" ]; then
    cleanup
fi
