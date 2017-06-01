#!/usr/bin/env bash
. $HOME/af_env.sh
py3clean .
export AF_TEST_SUITE_NAME='FRU PAQX Parent API'
python $AF_RUN_JOBS_PATH/run_fru_paqx_api_test_suite.py
export AF_TEST_SUITE_NAME='Test Suite Name Not Set' 
