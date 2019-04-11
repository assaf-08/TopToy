#!/usr/bin/env bash

echo "title = \"configuration\"
[system]
    n = ${2}
    f = ${3}
    c = 1
    testing = true
    txSize = 0

[setting]
    tmo = 1000
    tmoInterval = 100
    ABConfigPath = \"src/main/resources/ABConfig\"
    maxTransactionInBlock = 1000
    caRootPath = \"\"
    fastMode = true

[server]
    privateKey = \"\"\"MIGNAgEAMBAGByqGSM49AgEGBSuBBAAKBHYwdAIBAQQg/ngTdAL+eZOyh4lilm6djqsl
                    RDHT5C60eLxRcEoNjAGgBwYFK4EEAAqhRANCAASeFQqtyOwJcJtYceofW2TeNg7rJBlW
                    L28GZn+tk32fz95JqVS3+iF6JdoM1clkRFLliyXSxEnS1iO4wzRKGQwm\"\"\"

    TlsPrivKeyPath = \"src/main/resources/sslConfig/server.pem\"
    TlsCertPath = \"src/main/resources/sslConfig/server.crt\"


[[cluster]]" > config.txt

inputIpsPath=${1}
i=0
while read line
do
echo "      [cluster.s${i}]
            id = ${i}
            ip = \"${line}\"
            wrbPort = 30000
            commPort = 30010
            obbcPort = 30020
            publicKey =\"\"\"MFYwEAYHKoZIzj0CAQYFK4EEAAoDQgAEnhUKrcjsCXCbWHHqH1tk3jYO6yQZVi9vBmZ/rZ
                           N9n8/eSalUt/oheiXaDNXJZERS5Ysl0sRJ0tYjuMM0ShkMJg==\"\"\"
" >> config.txt
i=$((i+1))
done < ${inputIpsPath}