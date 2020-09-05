#!/usr/bin/env bash

source $PWD/definitions.sh

main() {
    mvn install

    sudo chmod 777 ${BIN_DIR}/run_docker.sh && \
    sudo chmod 777 ${BIN_DIR}/run_single.sh
    sudo chmod 777 ${CBIN_DIR}/run_cdocker.sh && \
    sudo chmod 777 ${CBIN_DIR}/run_client.sh

}

main