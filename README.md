# TopToy
This project is the implementation of the TOY algorithm and its orchestrating platform, TOP, as was presented in [TOY: a Total ordering Optimistic sYstem for Permissioned Blockchains](https://arxiv.org/abs/1901.03279).

TOY is a unique frugal optimistic blockchain algorithm.

TOP runs TOYS as workers and is capable to deal with tens to hundreds of thousands of TPS while deployed on dozens to hundreds machines.

## Getting Started
TopToy is supported only on an Ubuntu linux machine requires the following

### Prerequisites
1. Install [docker](https://docs.docker.com/install/linux/docker-ce/ubuntu/)
    1. Remove an old version (if exists)
        ```
        sudo apt-get remove docker docker-engine docker.io containerd runc
        ```
    1. Set Up The Repository
        ```
        sudo apt-get update
        sudo apt-get install apt-transport-https ca-certificates curl gnupg-agent software-properties-common
        curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
        sudo apt-key fingerprint 0EBFCD88
        sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
        ```
    1. Install Docker
        ```
        sudo apt-get update
        sudo apt-get install docker-ce docker-ce-cli containerd.io
        ```
1. Install [maven](https://maven.apache.org/install.html)
    ```
    sudo apt update
    sudo apt install maven
    ```
### Installation
1. Clone this repository
    ```
    git clone https://github.com/TopToy/TopToy.git
    ```
1. Build the project
    ```
    make docker-full-build
    ```

## Benchmarks
This project has a couple of dockers based benchmarks that demonstrate its abilities<br>
The benchmarks parameters are described in the [wiki]().

### Benchmarks Description

Benchmark | Description
----------|-------------
test_correct | Test the system when no failures occur
test_benign_failure | Test the system with (i) _f_ benign failures or (ii) in an asynchronous period
test_byz_failure | Test the system with (i) _f_ Byzantine failures and (ii) ina asynchronous period
test_correct_with_clients | Test the system when no failures occur with clients running on top of docker containers

### Running A Benchmark
1. Go to `definitions.sh` and set _n_ and _f_
1. Go to `make_scripts/tests/tests_conf.txt` and mark the benchmarks to run
    * To exclude a benchmark, mark it with '_#_'
1. Build the project using `make docker-full-build`
1. Run the benchmarks using `make docker-run-tests`
1. See the results and the logs at `/tmp/dtoy/out`


