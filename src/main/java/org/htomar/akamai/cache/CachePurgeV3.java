package org.htomar.akamai.cache;

import org.htomar.akamai.request.PurgeRequest;
import org.htomar.akamai.request.PurgeResponse;
import org.htomar.openakamai.edge.auth.credentials.ClientCredential;
import org.htomar.openakamai.edge.auth.exception.RequestSigningException;
import org.htomar.openakamai.edge.auth.signer.EdgeGridV1Signer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

/**
 * Cache Purge utility utilizing Akamai CCU v3. CCU v3 uses the new
 * Open Akamai Authentication mechanism for authorizing purge requests.
 *
 * @author Himanshu Tomar
 * @see CachePurgeV2 for Open Akamai based CCU v2.
 */
public class CachePurgeV3 {
    /**
     * The logger used for logging.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(CachePurgeV3.class);

    /**
     * Invalidation by URL endpoint.
     */
    private static final String URL_INVALIDATE_ENDPOINT = "/ccu/v3/invalidate/url/production";

    /**
     * Invalidation by CPCode endpoint.
     */
    private static final String CPCODE_INVALIDATE_ENDPOINT = "/ccu/v3/invalidate/cpcode/production";

    /**
     * EdgeGrid V1 Signer which is used for encrypting Akamai request using Open API format.
     */
    private EdgeGridV1Signer signer = new EdgeGridV1Signer();

    /**
     * This method is used for purging URL(s) using CCU v3 api. Method accepts
     * {@link PurgeRequest} which contains the URL(s) to be purged along with
     * {@link ClientCredential} for open Akamai based authentication.
     *
     * @param purgeRequest the purge request object.
     * @param credential   credentials to be used for open Akamai authentication.
     * @param hostName     the request base host name.
     * @return an object of PurgeResponse
     * @throws RestClientException     if any exception occurs in making rest api call.
     * @throws RequestSigningException if any exception occurs in signing request.
     * @see ClientCredential
     */
    public PurgeResponse purgeByURL(final PurgeRequest<String> purgeRequest,
                                    final ClientCredential credential, String hostName)
            throws RestClientException, RequestSigningException {
        preProcessRequest(purgeRequest, hostName);
        hostName = hostName.toLowerCase();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PurgeResponse> responseEntity = restTemplate.exchange(
                hostName + URL_INVALIDATE_ENDPOINT, HttpMethod.POST,
                new HttpEntity<>(purgeRequest,
                        signer.sign(credential, hostName,
                                URL_INVALIDATE_ENDPOINT, purgeRequest)),
                PurgeResponse.class);
        LOGGER.debug(responseEntity.getBody().toString());
        return responseEntity.getBody();
    }

    /**
     * Method to pre-process purge request. It validates purge host and purge URL(s).
     *
     * @param purgeRequest the purge request.
     * @param hostName     the request base host name.
     */
    private void preProcessRequest(final PurgeRequest<String> purgeRequest,
                                   final String hostName) {
        Assert.hasText(hostName,
                "hostname cannot be null.");
        Assert.isTrue(hostName.toLowerCase().startsWith("https"),
                "hostname should start with https.");
        preProcessPurgeRequest(purgeRequest);
    }

    /**
     * Method to pre-process URL purge request and remove any invalid URL(s).
     *
     * @param purgeRequest the purge request.
     */
    private void preProcessPurgeRequest(final PurgeRequest<String> purgeRequest) {
        Assert.notEmpty(purgeRequest.getObjects(),
                "purge request object cannot be null.");
        purgeRequest.setObjects(purgeRequest.getObjects().stream()
                .filter(StringUtils::hasText)
                .map(url -> url.startsWith("/") || url.startsWith("http")
                        ? url.toLowerCase()
                        : "/" + url.toLowerCase())
                .collect(Collectors.toList()));
    }

    /**
     * This method is used for purging CPCode(s) using CCU v3 api. Method accepts
     * {@link PurgeRequest} which contains the CPCode(s) to be purged along with
     * {@link ClientCredential} for open Akamai based authentication.
     *
     * @param purgeRequest the purge request object.
     * @param credential   credentials to be used for open Akamai authentication.
     * @param hostName     the request base host name.
     * @return an object of PurgeResponse
     * @throws RestClientException     if any exception occurs in making rest api call.
     * @throws RequestSigningException if any exception occurs in signing request.
     * @see ClientCredential
     */
    public PurgeResponse purgeByCPCode(final PurgeRequest<Integer> purgeRequest,
                                       final ClientCredential credential,
                                       final String hostName)
            throws RestClientException, RequestSigningException {
        preProcessCPCodeRequest(purgeRequest, hostName);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<PurgeResponse> responseEntity = restTemplate.exchange(
                hostName + CPCODE_INVALIDATE_ENDPOINT,
                HttpMethod.POST,
                new HttpEntity<>(purgeRequest,
                        signer.sign(credential,
                                hostName,
                                CPCODE_INVALIDATE_ENDPOINT,
                                purgeRequest)),
                PurgeResponse.class);
        LOGGER.debug(responseEntity.getBody().toString());
        return responseEntity.getBody();
    }

    /**
     * Method to pre-process CPCode purge request and remove any invalid CPCode(s).
     *
     * @param purgeRequest the purge request.
     * @param hostName     the request base host name.
     */
    private void preProcessCPCodeRequest(final PurgeRequest<Integer> purgeRequest,
                                         final String hostName) {
        Assert.hasText(hostName,
                "hostname cannot be null.");
        Assert.notEmpty(purgeRequest.getObjects(),
                "purge request object cannot be null.");
        purgeRequest.setObjects(
                purgeRequest.getObjects()
                        .stream()
                        .filter(p -> p > 0)
                        .collect(Collectors.toList()));
    }
}
