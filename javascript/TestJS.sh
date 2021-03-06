#!/bin/bash

if [[ -z "$1" ]] ; then
    echo Usage: $(basename "$0") \<variant\>
    exit 1
fi

mkdir -p _out

JS="$(dirname "$0")"

javac -d "_out" "--class-path=$JS/../javascript:$JS/../java" "$JS/jstest/prefix/PrefixSumAvgTest.java" \
    && java \
        -ea \
        "--module-path=$JS/graal" \
        "--class-path=_out" jstest.prefix.PrefixSumAvgTest "$1"
