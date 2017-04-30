#!/usr/bin/env bash
./create_jar.sh
echo "Installing crypto in /usr/local/bin/crypto"
cat create_executable.sh encryption4s.jar > crypto && chmod +x crypto && mv crypto /usr/local/bin/
rm encryption4s.jar