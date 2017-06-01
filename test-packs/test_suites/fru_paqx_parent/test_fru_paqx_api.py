import json
import pytest
import re
import requests
from requests.models import PreparedRequest
import requests.exceptions
import af_support_tools
import os

@pytest.fixture(scope="module", autouse=True)
def load_test_data():
    env_file = 'env.ini'
    #set variables
    global ipaddress
    ipaddress = af_support_tools.get_config_file_property(config_file=env_file, heading='Base_OS', property='hostname')
    global user
    user = af_support_tools.get_config_file_property(config_file=env_file, heading='Base_OS', property='username')
    global password
    password = af_support_tools.get_config_file_property(config_file=env_file, heading='Base_OS', property='password')
    # Update config ini files at runtime
    # This can be used to update ini files with sensitive data such as passwords an IP addresses
    my_data_file = str(os.environ.get('AF_RESOURCES_PATH')) + '/fru_paqx_parent/fru_paqx_endpoint.properties'
    af_support_tools.set_config_file_property_by_data_file(my_data_file)
    # Set config ini file name
    # These can be set inside the individual tests and do not need to be made global
    global config_file
    config_file = 'fru_paqx_parent/fru_paqx_endpoint.ini'
    #access all the endpoint URLs from the config_file.
    global rackhd_url
    rackhd_url = af_support_tools.get_config_file_property(config_file=config_file, heading='rackhd', property='url')
    global rackhd_username
    rackhd_username = af_support_tools.get_config_file_property(config_file=config_file, heading='rackhd', property='username')
    global rackhd_password
    rackhd_password = af_support_tools.get_config_file_property(config_file=config_file, heading='rackhd', property='password')
    global coprhd_url
    coprhd_url = af_support_tools.get_config_file_property(config_file=config_file, heading='coprhd', property='url')
    global coprhd_username
    coprhd_username = af_support_tools.get_config_file_property(config_file=config_file, heading='coprhd', property='username')
    global coprhd_password
    coprhd_password = af_support_tools.get_config_file_property(config_file=config_file, heading='coprhd', property='password')
    global vcenter_url
    vcenter_url = af_support_tools.get_config_file_property(config_file=config_file, heading='vcenter', property='url')
    global vcenter_username
    vcenter_username = af_support_tools.get_config_file_property(config_file=config_file, heading='vcenter', property='username')
    global vcenter_password
    vcenter_password = af_support_tools.get_config_file_property(config_file=config_file, heading='vcenter', property='password')
    global scaleio_url
    scaleio_url = af_support_tools.get_config_file_property(config_file=config_file, heading='scaleio', property='url')
    global scaleio_username
    scaleio_username = af_support_tools.get_config_file_property(config_file=config_file, heading='scaleio', property='username')
    global scaleio_password
    scaleio_password = af_support_tools.get_config_file_property(config_file=config_file, heading='scaleio', property='password')

@pytest.fixture(scope='module')
def fru_paqx_url():
    protocol = 'https://'
    host = ipaddress
    port = '18443'
    endpoint = '/fru/api/workflow/'
    URL = protocol + host + ':' + port + endpoint
    return URL

"""
This fixture updates the global dict rackHD_dict with the rackhd creadentials from config_file.
"""
@pytest.fixture(scope='module')
def get_rackHD_dict():
    rackHD_dict = {'endpointUrl': rackhd_url , 'username': rackhd_username, 'password': rackhd_password }
    return rackHD_dict

"""
This fixture updates the global dict coprHD_dict with the coprHD credentials from config_file.
"""
@pytest.fixture(scope='module')
def get_coprHD_dict():
    coprHD_dict = {'endpointUrl': coprhd_url , 'username': coprhd_username, 'password': coprhd_password }
    return coprHD_dict

"""
This fixture updates the global dict vcenter_dict with the vcenter credentials from config_file.
"""
@pytest.fixture(scope='module')
def get_vcenter_dict():
    vcenter_dict = {'endpointUrl': vcenter_url , 'username': vcenter_username , 'password': vcenter_password }
    return vcenter_dict

"""
Below fixture updates the global dict scaleIO_dict with the scaleio credentials from config_file.
"""
@pytest.fixture(scope='module')
def get_scaleIO_dict():
    scaleIO_dict = {'endpointUrl': scaleio_url , 'username': scaleio_username , 'password': scaleio_password }
    return scaleIO_dict


@pytest.fixture(scope='module')
def fru_paqx_headers():
    with open(str(os.environ.get('AF_TEST_SUITE_PATH')) + '/fru_paqx_parent/fixtures/start_quanta_workflow.json') as fixture:
        headers = json.loads(fixture.read())
        return headers


@pytest.fixture(scope='module')
def get_workflow_id(fru_paqx_url, fru_paqx_headers):
    with open(str(os.environ.get('AF_TEST_SUITE_PATH'))+ '/fru_paqx_parent/fixtures/start_quanta_workflow.json') as fixture:
        requestBody = json.loads(fixture.read())
        r = requests.post(fru_paqx_url, json=requestBody, headers=fru_paqx_headers,verify=False)
        data = r.json()
        return data['id']


"""
Private helper for UUID matching
"""
def _create_uuid_pattern():
    return re.compile(
        (
            '[a-f0-9]{8}-' +
            '[a-f0-9]{4}-' +
            '[1-5]' + '[a-f0-9]{3}-' +
            '[89ab][a-f0-9]{3}-' +
            '[a-f0-9]{12}$'
        ),
        re.IGNORECASE
    )

"""
Private helper to validate URLs, lifted from Django
"""
def _check_url():
    return re.compile(
        r'^(?:http|ftp)s?://' # http:// or https://
        r'(?:(?:[A-Z0-9](?:[A-Z0-9-]{0,61}[A-Z0-9])?\.)+(?:[A-Z]{2,6}\.?|[A-Z0-9-]{2,}\.?)|' #domain...
        r'localhost|' #localhost...
        r'\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})' # ...or ip
        r'(?::\d+)?' # optional port
        r'(?:/?|[/?]\S+)$',
    re.IGNORECASE)

"""
common assertions across different apis are grouped into this private method
"""
def _assert_common_response():
    assert _create_uuid_pattern().match(data['id'])
    assert data['workflow'] == 'quanta-replacement-d51b-esxi'
    assert data['currentStepNumber'] == -1
    assert data['expectedNumberOfSteps'] == -1
    assert data['links'][0]['rel'] == 'step-next'
    assert _check_url().match(data['links'][0]['href'])



print("************************************Start of FRU workflow***********************************")

"""
API: start quanta work flow
"""
@pytest.mark.fru_api
@pytest.mark.fru_paqx_parent
@pytest.mark.fru_mvp
def test_start_quanta_workflow(fru_paqx_url, fru_paqx_headers):
    with open(str(os.environ.get('AF_TEST_SUITE_PATH'))+ '/fru_paqx_parent/fixtures/start_quanta_workflow.json') as fixture:
        # Arrange
        requestBody = json.loads(fixture.read())
        # Act
        r = requests.post(fru_paqx_url, json=requestBody, headers=fru_paqx_headers,verify=False)
        data = r.json()
        # Asserts
        assert r.status_code == 201
        assert _assert_common_response
        assert data['currentStep'] == 'workflowInitiated'
        assert data['links'][0]['rel'] == 'step-next'
        assert data['links'][0]['type'] == 'application/vnd.dellemc.rackhd.endpoint+json'
        assert data['links'][0]['method'] == 'POST'

"""
API: capture rackHD endpoint
"""
@pytest.mark.fru_api
@pytest.mark.xfail
def test_capture_rackHD_endpoint(fru_paqx_url, get_workflow_id, get_rackHD_dict):
    requestBody = get_rackHD_dict
    fru_paqx_headers = {'Content-Type':'application/vnd.dellemc.rackhd.endpoint+json'}
    workflow_id = get_workflow_id
    post_URL = fru_paqx_url + get_workflow_id+ "/rackhd-endpoint"
    #Act
    r = requests.post(post_URL, json=requestBody,headers=fru_paqx_headers,verify=False)
    data = r.json()
    #asserts start here
    assert r.status_code == 200
    assert _assert_common_response
    assert data['currentStep'] == 'captureRackHDEndpoint'
    assert data['links'][0]['type'] == 'application/vnd.dellemc.coprhd.endpoint+json'
    assert data['links'][0]['method'] == 'POST'

"""
API: capture coprHD endpoint
"""
@pytest.mark.fru_api
#@pytest.mark.xfail
def test_capture_coprHD_endpoint(fru_paqx_url, get_workflow_id, get_coprHD_dict):
    requestBody = get_coprHD_dict
    workflow_id = get_workflow_id
    post_URL = fru_paqx_url + get_workflow_id+ "/coprhd-endpoint"
    fru_paqx_headers = {'Content-Type':'application/vnd.dellemc.coprhd.endpoint+json'}
    #Ack
    r = requests.post(post_URL, json=requestBody, headers=fru_paqx_headers,verify=False)
    data = r.json()
    #asserts start here
    assert r.status_code == 200
    assert _assert_common_response
    assert data['currentStep'] == 'captureCoprHDEndpoint'
    assert data['links'][0]['type'] == 'application/vnd.dellemc.vcenter.endpoint+json'
    assert data['links'][0]['method'] == 'POST'

"""
API: capture Vcenter endpoint
"""
@pytest.mark.fru_api
@pytest.mark.xfail
def test_capture_vcenter_endpoint(fru_paqx_url, get_workflow_id, get_vcenter_dict):
    requestBody = get_vcenter_dict
    fru_paqx_headers = {'Content-Type':'application/vnd.dellemc.vcenter.endpoint+json'}
    workflow_id = get_workflow_id
    post_URL = fru_paqx_url + get_workflow_id+ "/vcenter-endpoint"
    #Act
    r = requests.post(post_URL, json=requestBody, headers=fru_paqx_headers, verify=False)
    data = r.json()
    #asserts start here
    assert r.status_code == 200
    assert _assert_common_response
    assert data['currentStep'] == 'capturevCenterEndpoint'
    assert data['links'][0]['type'] == 'application/vnd.dellemc.scaleio.endpoint+json'
    assert data['links'][0]['method'] == 'POST'

"""
API: capture scaleio endpoint
"""
@pytest.mark.fru_api
@pytest.mark.xfail
def test_capture_scaleIO_endpoint(fru_paqx_url, get_workflow_id,get_scaleIO_dict):
    requestBody = get_scaleIO_dict
    fru_paqx_headers = {'Content-Type':'application/vnd.dellemc.scaleio.endpoint+json'}
    workflow_id = get_workflow_id
    post_URL = fru_paqx_url + get_workflow_id+ "/scaleio-endpoint"
    #Act
    r = requests.post(post_URL, json=requestBody, headers=fru_paqx_headers, verify=False)
    data = r.json()
    #asserts start here
    assert r.status_code == 200
    assert _assert_common_response
    assert data['currentStep'] == 'captureScaleIOEndpoint'
    assert data['links'][0]['type'] == 'application/json'
    assert data['links'][0]['method'] == 'POST'

"""
API: Discover ScaleIO.
"""
@pytest.mark.xfail
@pytest.mark.fru_api
def test_discover_scaleIO(fru_paqx_url, get_workflow_id, fru_paqx_headers):
    workflow_id = get_workflow_id
    post_URL = fru_paqx_url + get_workflow_id+ "/start-scaleio-data-collection"
    #Act
    r = requests.post(post_URL,headers=fru_paqx_headers,verify=False)
    data = r.json()
    #asserts start here
    assert r.status_code == 200
    assert _assert_common_response
    assert data['currentStep'] == 'startScaleIODataCollection'
    assert data['links'][0]['type'] == 'application/json'
    assert data['links'][0]['method'] == 'POST'

"""
API: Discover vCenter
"""
@pytest.mark.fru_api
#@pytest.mark.xfail
def test_discover_vCenter(fru_paqx_url, get_workflow_id, fru_paqx_headers):
    workflow_id = get_workflow_id
    post_URL = fru_paqx_url + get_workflow_id+ "/start-vcenter-data-collection"
    #Act
    r = requests.post(post_URL,headers=fru_paqx_headers,verify=False)
    data = r.json()
    #asserts start here
    assert r.status_code == 200
    assert _assert_common_response
    assert data['currentStep'] == 'startvCenterDataCollection'
    assert data['links'][0]['type'] == 'application/json'
    assert data['links'][0]['method'] == 'POST'

"""
API: List active jobs
"""
@pytest.mark.fru_api
@pytest.mark.fru_paqx_parent
def test_list_active_jobs(fru_paqx_url):
    headers = {'Content-Type': 'application/json'}
    r = requests.get(fru_paqx_url, headers=headers, verify=False)


"""
Validate that:
  -  id passed in URL matches the id returned in body
"""
@pytest.mark.fru_api
@pytest.mark.fru_paqx_parent
def test_get_job_details(fru_paqx_url, get_workflow_id, fru_paqx_headers):
    r = requests.get(fru_paqx_url + get_workflow_id, headers=fru_paqx_headers, verify=False)
    data = r.json()
    print(data)
    assert data['id'] == get_workflow_id
