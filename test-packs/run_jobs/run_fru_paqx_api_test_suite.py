import os
os.system('py.test $AF_TEST_SUITE_PATH/ -m "fru_api" --html $AF_REPORTS_PATH/all/fru_paqx_parent_fru_api_test_suite_report.html --self-contained-html --json $AF_REPORTS_PATH/all/fru_paqx_parent_fru_api_test_suite_report.json --junit-xml $AF_REPORTS_PATH/all/fru_paqx_parent_fru_api_test_suite_report.xml') 


