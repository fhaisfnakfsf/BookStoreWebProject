#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
mkdir -p out
javac -encoding UTF-8 -d out src/*.java tests/TestDoAn.java
java -Dsun.stdout.encoding=UTF-8 -Dfile.encoding=UTF-8 -cp out TestDoAn
