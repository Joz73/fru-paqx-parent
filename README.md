[![License](https://img.shields.io/badge/License-EPL%201.0-red.svg)](https://opensource.org/licenses/EPL-1.0)
[![Build Status](https://travis-ci.org/dellemc-symphony/fru-paqx-parent.svg?branch=master)](https://travis-ci.org/dellemc-symphony/fru-paqx-parent)
# fru-paqx-parent
## Description
This repository contains the source code for the FRU PAQX service within Dell Project Symphony. 

The FRU PAQX service implements a VxRack FLEX Field Replacement Unit (FRU) workflow. The project is written in Java and is based on the Spring Boot Library.

## Documentation

You can find additional documentation for Project Symphony at [dellemc-symphony.readthedocs.io][documentation].

## API overview

The FRU PAQX service supports a REST API that performs a series of steps to execute a FRU workflow. The API uses HATEOAS (Hypermedia as the Engine of Application State) as its REST architecture. This means that the API implements a hypermedia-driven model, where each REST response contains one or more hypermedia links. These links provide the caller with information needed to perform the next step in the workflow.   

The steps in the FRU workflow must be performed in the linear sequence established by the hypermedia-driven model. This order is enforced by the workflow CLI. However, if you are executing the API calls directly, you need to ensure that the steps are performed in the correct order.

You can see the complete list of possible API calls by exploring the Swagger UI at <http://localhost:18443/fru/swagger-ui.html>.

## Before you begin

For Linux development, install the rpm-build package to enable RPM creation.
For Windows development, install cygwin or an equivalent and ensure the rpm-build package is installed.

Verify that the following tools are installed:
 
* Apache Maven 3.0.5+
* Docker 1.12+
* Docker Compose 1.8.0+
* Java Development Kit (version 8)
* RabbitMQ 3.6.6

## Building

Run the following command to build this project:
```bash
mvn clean install
```

## Deploying

If installing the RPM for the first time, run this locally:
```bash
rpm -ivh <RPM Name>
```
 
If upgrading an existing RPM, run this locally:
```bash
rpm -Uvh <RPM Name>
```

The output of running the build step is a tagged Docker image.
You can run this locally with the following command:
```bash
docker run -it --net="host" <docker_image_hash>
```
This deploys a container based on the image created in the build step that communicates with the host's RabbitMQ installation.

To run the Java application manually:

`java -jar ./fru-paqx/target/fru-paqx-1.0-SNAPSHOT.jar`

After running the Java application, you can access the REST API with the following calls:

* GET <http://localhost:18443/fru/api/about>

* GET: <http://localhost:18443/fru/api/workflow/>

* GET: <http://localhost:18443/fru/api/workflow/{jobId}>

* POST: <http://localhost:18443/fru/api/workflow/>

To start a FRU workflow for a Quanta replacement, execute the POST call shown above (POST: <http://localhost:18443/fru/api/workflow/>) with the following POST body:

```
{
"workflow":"quanta-replacement-d51b-esxi"
}

```
This call returns a JSON response that provides the next step to execute in the API call sequence:

```
{
    "id": "123abc-456def-789ghi",
   "workflow": "quanta-replacement-d51b-esxi",
    "currentStep": "Initiate Workflow",
    "nodes": null,
   "links": [{
       "rel": "step-next",
        "href": "https://localhost:18443/fru/api/workflow/123abc-456def-789ghi/rackhd-endpoint",
       "type": "application/vnd.dellemc.rackhd.endpoint+json",
       "method": "POST"
   }]
}
```
Execute the rackhd-endpoint API call (shown in the href value above) with a POST body that uses the format shown below:

```
{
  "endpointUrl": "http://<ip-address>:<port>",
  "username": "<username>",
  "password": "<password>"
}

```

For each of the remaining endpoint API steps that follow (coprhd-endpoint, vcenter-endpoint, and scaleio-endpoint), use the same POST body structure you used for rackhd-endpoint. Be sure to provide the correct endpointUrl, username, and password value required for each target endpoint.

After submitting the information required to access the four endpoints, you can continue with the steps that follow. You do not need to provide a POST body for most of the remaining steps.  

## Contributing

Project Symphony is a collection of services and libraries housed at [GitHub][github].
Contribute code and make submissions at the relevant GitHub repository level. See [our documentation][contributing] for details on how to contribute.

## Community

Reach out to us on the Slack [#symphony][slack] channel. Request an invite at [{code}Community][codecommunity].
You can also join [Google Groups][googlegroups] and start a discussion. 

[slack]: https://codecommunity.slack.com/messages/symphony
[googlegroups]: https://groups.google.com/forum/#!forum/dellemc-symphony
[codecommunity]: http://community.codedellemc.com/
[contributing]: http://dellemc-symphony.readthedocs.io/en/latest/contributingtosymphony.html
[github]: https://github.com/dellemc-symphony
[documentation]: https://dellemc-symphony.readthedocs.io/en/latest/

