#!/bin/bash

if [[ $1 == "-b" ]] || [[ ! -d "./target" ]]; then
    mvn compile
    mvn install
fi
java -jar ./target/JToy-1.0-jar-with-dependencies.jar