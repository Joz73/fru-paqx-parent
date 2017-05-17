import pytest
import traceback
import requests
import af_support_tools
import file_present
import subprocess
import platform
import re

try:
    env_file = 'env.ini'
    ipaddress = af_support_tools.get_config_file_property(config_file=env_file, heading='Base_OS', property='hostname')
    user = af_support_tools.get_config_file_property(config_file=env_file, heading='Base_OS', property='username')
    password = af_support_tools.get_config_file_property(config_file=env_file, heading='Base_OS', property='password')

except Exception as e:
    print('Possible configuration error.')
    traceback.print_exc()
    raise Exception(e)


@pytest.mark.fru_paqx_parent
@pytest.mark.fru_mvp
def test_fru_cli_chk():
    """
    Description:
        The following test checks whether the workflow-cli file is present on the current system
    :return: None
    """
    # Arrange
    os_system = platform.system()
    print('\nMachine OS:' + os_system)
    print('\nSearching for Workflow-cli file....')

    if os_system == 'Windows':
        program = 'workflow-cli.exe'
    else:
        program = 'workflow-cli'

    # Act/Assert
    assert file_present.file_present(file_name=program, exe=True), 'CLI Not Found.\nPlease confirm Workflow-cli Present'
    print('\nWorkflow-cli Present.')

@pytest.mark.fru_paqx_parent
@pytest.mark.fru_mvp
def test_fru_api():
    """
    Description:
        The following test checks whether the FRU api is running by sending Rest request.
        Expected responds = 200
    :return: None
    """
    # Arrange
    url = 'https://{}:18443/fru/api/about'.format(ipaddress)

    # Act
    print('\nSending http Request....'+url)
    response = requests.get(url, verify=False)

    # Assert
    assert response.status_code == 200, 'Unexpected API Response Code'
    print('\nResponse Successful.')


@pytest.mark.fru_paqx_parent
@pytest.mark.fru_mvp
def test_fru_cli_version():
    """
    Description:
        The following test checks the version of the worflow-cli file
        Expected format = 'v[0-9].[0-9].[0-9]-[0-9][0-9]' eg.'v0.0.13-6'
    :return: None
    """
    # Arrange
    os_system = platform.system()
    print('\nMachine OS:' + os_system)
    print('\nChecking Workflow-cli Version....')

    # Act
    try:
        if os_system == 'Windows':
            program = 'workflow-cli.exe'
            command = [program, 'version']
            inshell = True
        else:
            program = 'workflow-cli'
            command = ['./'+program, 'version']
            inshell = False

        path = file_present.file_present(file_name=program, exe=True)
        response = subprocess.check_output(command, cwd=path, stderr=subprocess.STDOUT, shell=inshell)
        response = response.decode('utf-8')
        # Searches for version with pattern 'v[0-9].[0-9].[0-9]-[0-9][0-9]' eg.'v0.0.13-6'; else Returns None
        searchObj = re.search(r'v\d\.\d\.\d-\d\d', response, re.M)
        if searchObj:
            version = searchObj.group()
            print('\nCurrent Version: ' + version)

    except Exception as err:
        print('Unexpected error: ' + str(err))
        traceback.print_exc()
        raise Exception(err)

    # Assert
    assert searchObj is not None, 'Version Incorrect'
    print('\nWorkflow-cli Version Correct.')


@pytest.mark.fru_paqx_parent
@pytest.mark.fru_mvp
def test_fru_cli_target():
    """
    Description:
        The following test checks that the FRU_paqx target can be successfully set.
    :return: None
    """
    # Arrange
    os_system = platform.system()
    print('\nMachine OS:' + os_system)
    print('\nSetting target to...' + ipaddress)

    # Act
    try:
        if os_system == 'Windows':
            program = 'workflow-cli.exe'
            command = [program, 'target', 'https://{}:18443'.format(ipaddress)]
            inshell = True
        else:
            program = 'workflow-cli'
            command = ['./' + program, 'target', 'https://{}:18443'.format(ipaddress)]
            inshell = False

        path = file_present.file_present(file_name=program, exe=True)
        response = subprocess.check_output(command, cwd=path, stderr=subprocess.STDOUT, shell=inshell)
        response = response.decode('utf-8')
        print(response)

    except Exception as err:
        print('Unexpected error: ' + str(err))
        traceback.print_exc()
        raise Exception(err)

    # Assert
    assert 'Target set to https://{}:18443'.format(ipaddress) in response
    print('Target Set.')


@pytest.mark.fru_paqx_parent
@pytest.mark.fru_mvp
def test_cli_target_file():
    """
    Description:
        The following test checks whether the target file (.cli) has been created and contains the correct target.
    :return: None
    """
    # Arrange
    file = '.cli'
    os_system = platform.system()
    print('\nMachine OS:' + os_system)
    print('\nChecking .cli Target File Created....')

    # Act
    path = file_present.file_present(file_name=file)
    if os_system == 'Windows':
        dir_seperator = '\\'
    else:
        dir_seperator = '/'

    if path:
        print('\nFile Created; Checking IP....')
        with open(path+dir_seperator+file, 'r') as cli_file:
            content = cli_file.read()
    # Assert
            assert ipaddress in content, "Target IP Incorrect"
            print('\nTarget IP Correct.')
    else:
        assert path, "Target File not Created"

###### In Development ######

# @pytest.mark.fru_paqx_parent
# @pytest.mark.fru_mvp
# def test_cli_fru_start():
#     # Arrange
#     os_system = platform.system()
#     print('\nMachine OS:' + os_system)
#
#     # Act
#     try:
#         if os_system == 'Windows':
#             program = 'workflow-cli.exe'
#             command = [program, 'start']
#             inshell = True
#         else:
#             program = 'workflow-cli'
#             command = ['./' + program, 'start']
#             inshell = False
#
#         path = file_present.file_present(file_name=program, exe=True)
#         response = subprocess.check_output(command, cwd=path, stderr=subprocess.STDOUT, shell=inshell)
#         response = response.decode('utf-8')
#         print(response)
#
#     except Exception as err:
#         print('Unexpected error: ' + str(err))
#         traceback.print_exc()
#         raise Exception(err)

    # Assert
    # assert ''.format(ipaddress) in response