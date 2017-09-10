package org.htomar.akamai.cache;

import org.htomar.akamai.headers.CustomHeaders;
import org.htomar.akamai.request.PurgeRequest;
import org.htomar.akamai.request.PurgeResponse;
import org.htomar.openakamai.edge.auth.credentials.BasicCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Cache Purge utility utilizing Akamai CCU v2. This class uses Basic Auth
 * mechanism for request authorizations.
 *
 * @author Himanshu Tomar
 * @see CachePurgeV3 for Open Akamai based CCU v3.
 */
public class CachePurgeV2 {
    /**
     * The logger used for logging.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(CachePurgeV2.class);

    /**
     * Default invalidation endpoint to be used. This URL is always same irrespective of client.
     */
    private static final String DEFAULT_INVALIDATE_ENDPOINT = "https://api.ccu.akamai.com/ccu/v2/queues/default";

    /**
     * This method is used for purging URL(s) using CCU v2 api. Method accepts
     * {@link PurgeRequest} which contains the URL(s) to be purged along with
     * {@link BasicCredential} for basic auth.
     *
     * @param purgeRequest purge request.
     * @param basicAuth    credentials to be used for basic auth.
     * @return an object of PurgeResponse
     * @throws RestClientException if any exception occurs in making rest api call.
     */
    public PurgeResponse purgeByURL(final PurgeRequest purgeRequest,
                                    final BasicCredential basicAuth) throws RestClientException {
        RestTemplate restTemplate = new RestTemplate();
        LOGGER.debug(purgeRequest.toString());
        ResponseEntity<PurgeResponse> responseEntity = restTemplate.exchange(
                DEFAULT_INVALIDATE_ENDPOINT, HttpMethod.POST,
                new HttpEntity<>(purgeRequest, new CustomHeaders(basicAuth)),
                PurgeResponse.class);
        LOGGER.info(responseEntity.getBody().toString());
        return responseEntity.getBody();
    }

    /**
     * This method is used for purging CPCode(s) using CCU v2 api. Method accepts
     * {@link PurgeRequest} which contains the CPCode(s) to be purged along with
     * {@link BasicCredential} for basic auth.
     *
     * @param purgeRequest purge request.
     * @param basicAuth    credentials to be used for basic auth.
     * @return an object of PurgeResponse
     * @throws RestClientException if any exception occurs in making rest api call.
     */
    public PurgeResponse purgeByCPCode(final PurgeRequest purgeRequest,
                                       final BasicCredential basicAuth) throws RestClientException {
        RestTemplate restTemplate = new RestTemplate();
        LOGGER.debug(purgeRequest.toString());
        purgeRequest.setType("cpcode");
        ResponseEntity<PurgeResponse> responseEntity = restTemplate.exchange(
                DEFAULT_INVALIDATE_ENDPOINT, HttpMethod.POST,
                new HttpEntity<>(purgeRequest, new CustomHeaders(basicAuth)),
                PurgeResponse.class);
        LOGGER.info(responseEntity.getBody().toString());
        return responseEntity.getBody();
    }
}
