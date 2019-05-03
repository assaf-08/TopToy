#!/usr/bin/env bash

source /home/yoni/github.com/JToy/make_scripts/tests/utiles.sh
source /home/yoni/github.com/JToy/definitions.sh
# ${1} - start worker
# ${2} - end worker
# ${3} - interval
# ${4} - tx_size
# ${5} - tmo
# ${6} - block_size
# ${7} - test_time
run_channels() {

    configure_inst ${7} ${cdest}
    configure_tx_size ${4} ${cdest}
    configure_tmo ${5} ${cdest}
    configure_max_tx_in_block ${6} ${cdest}
    configure_testing "false" ${cdest}

    configure_tx_size ${4} ${cldest}
    configure_tmo ${5} ${cldest}
    configure_max_tx_in_block ${6} ${cldest}
    configure_testing "false" ${cldest}

    for i in `seq ${1} ${3} ${2}`; do
        configure_channels ${i} ${cdest}
        configure_channels ${i} ${cldest}
#        run_dockers ${compose_file_correct} &
#        sleep 10
        run_dockers ${compose_file_correct_with_clients} &
        wait
    done
}

run_channels 1 1 1 512 10 10 60