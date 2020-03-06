#!/usr/bin/env bash
#!/usr/bin/env bash

source $PWD/utils/aws_utils.sh
source $PWD/utils/config_utils.sh
source $PWD/utils/utils.sh
source $PWD/definitions.sh

# ${1} tx size
# ${2} max tx/block
# #{3} start worker
# ${4} end worker
# ${5} interval
# ${6} tmo
# ${7} test time
# ${8} f
test_byz_tps_servers_over_workers() {

    echo "running test_byz_tps_servers_over_workers"

    configure_servers "$@"

    for i in `seq ${3} ${5} ${4}`; do

        echo "run [[txSize:${1}] [txInBlock:${2}] [workers:${i}]]"
        local w=${i}
        configure_servers_workers ${w}

        ${utils_dir}/watchdog.sh 120 &
        local wdipd=$!

        run_servers $7 $8

        kill -9 $wdipd
        sleep 10
        collect_res_from_servers

    done

}

configure_servers() {

    configure_tx_size ${1} ${config_toml}
    configure_max_tx_in_block ${2} ${config_toml}
    configure_tmo ${6} ${config_toml}


    for i in `seq 0 $((${#servers[@]} - 1))`; do

        if (( ${i} < ${8} )); then
            configure_byz_inst_with_statistics 30 ${7} 1 ${inst}
        else
            configure_inst_with_statistics 30 ${7} 1 ${inst}
        fi

        copy_data_to_tmp ${config_rb}
        local public_ip=`echo "${servers[${i}]}" | sed 's/'${user}'\@//g'`
        local private_ip=${pips[${i}]}
        replace_ips ${public_ip} ${private_ip} ${config_rb}

        update_resources_and_load ${servers[${i}]} ${sbin} ${conf}

        restore_data_from_tmp ${config_rb}

    done
}

run_servers() {

    local pids=[]
    for i in `seq 0 $((${#servers[@]} - 1))`; do
        local id=${i}
        if (( $i < ${2} )); then
            run_remote_server ${servers[${id}]} ${id} "b"
        else
            run_remote_server ${servers[${id}]} ${id} "r"
        fi

        pids[${id}]=$!
    done
    sleep 30
    t=$((${1} - 30))
    echo "waits for more ${t} s"
    progress-bar ${t}

    for pid in "${pids[@]}"; do
        wait ${pid}
    done

}

configure_servers_workers() {

    for s in "${servers[@]}"; do
        configure_workers ${1} ${config_toml}
        sr=${s}
        update_config_toml ${sr} ${sbin} ${config_toml}
    done

}
start_aws_instances

#test_byz_tps_servers_over_workers 512 10 3 3 2 100 60 1

#######################512x10###########################
test_byz_tps_servers_over_workers 512 10 1 5 2 100 60 3
#test_byz_tps_servers_over_workers 512 10 7 10 3 100 120 2

########################512x100###########################
test_byz_tps_servers_over_workers 512 100 1 5 2 100 60 3
#test_byz_tps_servers_over_workers 512 100 10 10 1 100 120 1
#
########################512x1000###########################
test_byz_tps_servers_over_workers 512 1000 1 5 2 100 60 3
#test_byz_tps_servers_over_workers 512 1000 10 10 1 100 120 1



stop_aws_instances
