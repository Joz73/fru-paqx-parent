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


@pytest.mark.cli_chk
def test_fru_cli_chk():
    # Arrange
    os_system = platform.system()
    print('\nMachine OS:' + os_system)

    if os_system == 'Windows':
        program = 'workflow-cli.exe'
    else:
        program = 'workflow-cli'

    # Act/Assert
    assert file_present.file_present(file_name=program, exe=True), 'CLI Not Found.\nPlease confirm Workflow-cli Present'


@pytest.mark.api
def test_fru_api():
    # Arrange
    url = 'https://{}:18443/fru/api/about'.format(ipaddress)

    # Act
    response = requests.get(url, verify=False)
    print(url)

    # Assert
    assert response.status_code == 200, 'Unexpected API Response Code'


@pytest.mark.cli_version
def test_fru_cli_version():
    # Arrange
    os_system = platform.system()
    print('\nMachine OS:' + os_system)

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
            print('Release Version: ' + version)

    except Exception as err:
        print('Unexpected error: ' + str(err))
        traceback.print_exc()
        raise Exception(err)

    # Assert
    assert searchObj is not None, 'Version Incorrect'


@pytest.mark.cli_set_target
def test_fru_cli_target():
    # Arrange
    os_system = platform.system()
    print('\nMachine OS:' + os_system)

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


@pytest.mark.cli_target_file
def test_cli_target_file():
    # Arrange
    file = ".cli"

    # Act/Assert
    print(file_present.file_present(file_name=file))
    assert file_present.file_present(file_name=file), "Target File not Created"

