import os
os.system('py.test $AF_TEST_SUITE_PATH/ -m "fru_paqx_parent" --html $AF_REPORTS_PATH/all/fru_paqx_parent_test_suite_report.html --self-contained-html --json $AF_REPORTS_PATH/all/fru_paqx_parent_test_suite_report.json --junit-xml $AF_REPORTS_PATH/all/fru_paqx_parent_test_suite_report.xml')