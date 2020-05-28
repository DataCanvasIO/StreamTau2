#!/bin/bash
set -x

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && cd .. && pwd)"
WAR_PATH=$(find "${ROOT}" -name 'streamtau-manager*.war')

echo "Start WAR from ${WAR_PATH}"

# shellcheck disable=SC2086
java ${JAVA_OPTS} -jar "${WAR_PATH}" \
    --spring.config.additional-location="file:${ROOT}/conf/" \
    --logging.config="file:${ROOT}/conf/logback.xml"
