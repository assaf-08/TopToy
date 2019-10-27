#!/usr/bin/env bash

out_path_t=/home/yon/toy/${2}
#readarray -t gate < ./data/gateway.txt

gate=${1}
mkdir -p ${out_path_t}/summeries
while sleep 5; do
    echo "getting files from ${gate}..."
    rsync -au ${gate}:./toy/out/summeries ${out_path_t}
done


