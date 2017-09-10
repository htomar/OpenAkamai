package org.htomar.akamai.service;

import org.htomar.akamai.cache.CachePurgeV2;
import org.htomar.akamai.cache.CachePurgeV3;
import org.htomar.akamai.request.PurgeRequest;
import org.htomar.akamai.request.PurgeResponse;
import org.htomar.openakamai.edge.auth.credentials.BasicCredential;
import org.htomar.openakamai.edge.auth.credentials.ClientCredential;
import org.htomar.openakamai.edge.auth.exception.RequestSigningException;
import org.springframework.web.client.RestClientException;

public class AkamaiService {
    /**
     * Akamai purge service using Akamai's new Open API mechanism.
     * This uses {@link ClientCredential} to authenticate request.
     * This method accepts URLs for purge request.
     *
     * @param purgeRequest the purge request object
     * @param credential   Open API credentials
     * @return purge response
     * @throws RestClientException     if request fails
     * @throws RequestSigningException if request signing fails
     */
    public PurgeResponse purgeByURL(final PurgeRequest purgeRequest,
                                    final ClientCredential credential)
            throws RestClientException, RequestSigningException {
        CachePurgeV3 cachePurgeV3 = new CachePurgeV3();
        return cachePurgeV3.purgeByURL(purgeRequest,
                credential,
                credential.getBaseURL());
    }

    /**
     * Akamai purge service using Basic Auth (username:password) mechanism.
     * This method accepts URLs for purge request.
     *
     * @param purgeRequest    the purge request object
     * @param basicCredential basic auth
     * @return purge response
     * @throws RestClientException     if request fails
     * @throws RequestSigningException if request signing fails
     */
    public PurgeResponse purgeByURL(final PurgeRequest purgeRequest,
                                    final BasicCredential basicCredential)
            throws RestClientException, RequestSigningException {
        CachePurgeV2 cachePurgeV2 = new CachePurgeV2();
        return cachePurgeV2.purgeByURL(purgeRequest, basicCredential);
    }

    /**
     * Akamai purge service using Akamai's new Open API mechanism.
     * This uses {@link ClientCredential} to authenticate request.
     * This method accepts CPCode's for purge request.
     *
     * @param purgeRequest the purge request object
     * @param credential   Open API credentials
     * @return purge response
     * @throws RestClientException     if request fails
     * @throws RequestSigningException if request signing fails
     */
    public PurgeResponse purgeByCPCode(final PurgeRequest purgeRequest,
                                       final ClientCredential credential)
            throws RestClientException, RequestSigningException {
        CachePurgeV3 cachePurgeV3 = new CachePurgeV3();
        return cachePurgeV3.purgeByCPCode(purgeRequest,
                credential,
                credential.getBaseURL());
    }
}
