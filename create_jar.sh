#!/usr/bin/env bash
sbt compile
sbt assembly
mv target/scala-2.12/encryption4s.jar ./
