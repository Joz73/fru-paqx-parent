/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 */

package com.dell.cpsd.paqx.fru.service.integration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * Dell EMC Confidential/Proprietary Information
 * <p>
 */
@ActiveProfiles({"IntegrationTest"})
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpsConfigIT {

    @LocalServerPort
    int randomPort;

    /**RackHD Endpoint
     * Fill these up when running Integration Tests locally
     * TODO DO NOT LEAVE CREDENTIALS WHEN COMMITTING
     */
    String rackHDEndpointIP = "";
    String rackHDEndpointUrl = "https://"+rackHDEndpointIP+":8080";
    String rackHDUsername = ""; //leave empty if the RackHD does not require authentication
    String rackHDPassword = ""; //leave empty if the RackHD does not require authentication

    /**CoprHD Endpoint
     * Fill these up when running Integration Tests locally
     * TODO DO NOT LEAVE CREDENTIALS WHEN COMMITTING
     */
    String coprHDEndpointIP = "";
    String coprHDEndpointUrl = "https://"+coprHDEndpointIP+":4443";
    String coprHDUsername = "";
    String coprHDPassword = "";

    @BeforeClass
    public static void acceptAllCertificates() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllTheCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllTheCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            HostnameVerifier allValidHosts = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            HttpsURLConnection.setDefaultHostnameVerifier(allValidHosts);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAboutTest() throws JSONException
    {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = template.getForEntity("https://localhost:" + randomPort + "/fru/api/about", String.class, headers);
        assertThat(response.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void getEmptyWorkflowListOnInit() throws JSONException
    {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response = template.getForEntity("https://localhost:" + randomPort + "/fru/api/workflow", String.class, headers);
        assertThat(response.getHeaders().containsKey("Link")).isFalse();
    }

    @Test
    public void startWorkflow() throws JSONException
    {
        ResponseEntity<String> response = initiateWorkflow ();
        assertThat(response.getStatusCode().is2xxSuccessful());
    }

    /**
     * Gets the jobId from the workflow API. Currently it comes in the header.
     * @throws JSONException
     */
    @Test
    public void getJobById() throws JSONException
    {
        ResponseEntity<String> response = initiateWorkflow ();
        JSONObject jsonResponseBody = new JSONObject(response.getBody());
        String id = jsonResponseBody.get("id").toString();

        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> response2 = template.getForEntity("https://localhost:" + randomPort + "/fru/api/workflow/"+id, String.class, headers);

        assertThat(response2.getStatusCode().is2xxSuccessful());
    }

    /**
     * Test to be ignored until we figure out how to set the deployment process in a way that allows
     * for external setup of the RackHD credentials on IT running.
     *
     * @throws JSONException
     */
    @Test
    @Ignore
    public void captureRackHDEndpoint() throws JSONException
    {
        ResponseEntity<String> response = initiateWorkflow ();
        String body = buildRackHDEndpoint();
        ResponseEntity<String> response2 = doEndpointCapture(response, body);

        assertThat(response2.getStatusCode().is2xxSuccessful());
    }

    /**
     * Test to be ignored until we figure out how to set the deployment process in a way that allows
     * for external setup of the CoprHD credentials on IT running.
     *
     * @throws JSONException
     */
    @Test
    @Ignore
    public void captureCoprHDEndpoint() throws JSONException
    {
        ResponseEntity<String> response = initiateWorkflow ();
        String body = buildRackHDEndpoint();
        ResponseEntity<String> response2 = doEndpointCapture(response, body);

        String body2 = buildCoprHDEndpoint();
        ResponseEntity<String> response3 = doEndpointCapture(response2, body2);

        assertThat(response3.getStatusCode().is2xxSuccessful());
    }

    private ResponseEntity<String> doEndpointCapture(ResponseEntity<String> prevResponse, String body) throws JSONException
    {
        JSONObject jsonResponseBody = new JSONObject(prevResponse.getBody());

        JSONArray links = jsonResponseBody.getJSONArray("links");
        JSONObject linkData = links.getJSONObject(0);
        String uri = linkData.getString("href");
        String type = linkData.getString("type");

        return doPost(uri, body, type);
    }

    private String buildCoprHDEndpoint() throws JSONException
    {
        return buildEndpoint(coprHDEndpointUrl, coprHDUsername, coprHDPassword);
    }

    private String buildRackHDEndpoint() throws JSONException
    {
        return buildEndpoint(rackHDEndpointUrl, rackHDUsername, rackHDPassword);
    }

    private String buildEndpoint (String url, String username, String password) throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("endpointUrl",url);
        json.put("username",username);
        json.put("password",password);
        return json.toString();
    }

    private ResponseEntity<String> doPost(String uri, String body) throws JSONException
    {
        return doPost(uri, body, MediaType.APPLICATION_JSON.toString());
    }

    private ResponseEntity<String> doPost(String uri, String body, String contentType) throws JSONException
    {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", contentType);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setCacheControl("no-cache");

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        return template.postForEntity(uri, request, String.class);
    }

    private ResponseEntity<String> initiateWorkflow () throws JSONException
    {
        JSONObject json = new JSONObject();
        json.put("workflow","quanta-replacement-d51b-esxi");
        String body = json.toString();

        return doPost("https://localhost:" + randomPort + "/fru/api/workflow",body);
    }
}