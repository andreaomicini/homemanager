#!/bin/sh
# Compiles the Java parts of Home Manager as they were left in 2018, with a
# Java 8 compiler, as their Eclipse and NetBeans projects did. Each part is
# compiled from its own src/ against the jars it carries; nothing is fetched.
#
#   ./build.sh                 every part
#   ./build.sh SmartOven …     only the parts named
#
# Classes go to _build/<part>/, outside the historical trees. The Android
# app (MyAppHomeManager) is not built here: see README.md.
set -eu

PARTS="HomeManager SmartComponentInfrastructure SmartFridge SmartPantry SmartMixer SmartOven SmartWardrobe"
[ $# -gt 0 ] && PARTS="$*"

cd "$(dirname "$0")"
JAVAC="${JAVA_HOME:+$JAVA_HOME/bin/}javac"
"$JAVAC" -version 2>&1 | grep -q '1\.8\.' || {
    echo "build.sh: needs a Java 8 javac (set JAVA_HOME)" >&2; exit 2; }

for part in $PARTS; do
    out="_build/$part"
    rm -rf "$out"; mkdir -p "$out"
    # the part's own jars, without the -sources/-javadoc ones and the
    # copies NetBeans left in its build/ directory
    cp=$(find "$part" -name '*.jar' ! -name '*-sources.jar' ! -name '*-javadoc.jar' \
            ! -path "$part/build/*" | sort | tr '\n' ':')
    find "$part/src" -name '*.java' | sort > "$out.sources"
    "$JAVAC" -nowarn -encoding UTF-8 -cp "$cp" -d "$out" @"$out.sources"
    echo "$part: $(wc -l < "$out.sources" | tr -d ' ') sources, $(find "$out" -name '*.class' | wc -l | tr -d ' ') classes"
done
