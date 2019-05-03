#!/usr/bin/env bash

configure_tx_size() {
    sed -i 's/txSize = .*/txSize = '${1}'/g' ${2}
}

configure_max_tx_in_block() {
   sed -i 's/maxTransactionInBlock = .*/maxTransactionInBlock = '${1}'/g' ${2}
}

configure_workers()  {
    sed -i 's/c =.*/c = '${1}'/g' ${2}
}

configure_tmo()  {
    sed -i 's/tmo =.*/tmo = '${1}'/g' ${2}
}

configure_testing()  {
    sed -i 's/testing =.*/testing = '${1}'/g' ${2}
}
 # ${1} before
 # ${2} test time
 # ${3} after
 # ${4} inst file
configure_inst_with_statistics() {
    echo "init" > ${4}
    echo "serve" >> ${4}
    echo "wait ${1}" >> ${4}
    echo "stStart" >> ${4}
    echo "wait ${2}" >> ${4}
    echo "stStop" >> ${4}
    echo "wait ${3}" >> ${4}
    echo "stop" >> ${4}
    echo "quit" >> ${4}
}

 # ${1} test time
 # ${2} inst file
configure_inst() {
    echo "init" > ${2}
    echo "serve" >> ${2}
    echo "wait ${2}" >> ${2}
    echo "stop" >> ${2}
    echo "quit" >> ${2}
}

copy_data_to_tmp() {
    mkdir -p ${1}.tmp
    cat ${1} > ${1}.tmp
}

restore_data_from_tmp() {
    cat ${1}.tmp > ${1}
    rm -r -f ${1}.tmp
}

# ${1} bin directory
# ${2} configuration directory
update_resources() {
    rm -r -f ${1}/src/main/resources/*
    cp -r ${2}/* ${1}/src/main/resources/
}

# ${1} public ip
# ${2} private ip
# ${3} destination
replace_ips() {
    sed -i 's/'"${1}"'/'"${2}"'/g' ${3}
}

# ${1} servers summery file
print_servers_summery_header() {
     echo "valid,readTime,id,type,workers,tmo,actTmo,maxTmo,txSize,txInBlock,txTotal,duration,tps,nob,noeb,bps,avgTxInBlock,opt,opRate,pos,posRate,neg,negRate,avgNegTime,syncs,BP2T,BP2D,HP2T,HP2D,HT2D,suspected" >> ${1}
}