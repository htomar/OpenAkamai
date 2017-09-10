package org.htomar.openakamai.edge.auth.signer;

import org.htomar.akamai.request.PurgeRequest;
import org.htomar.openakamai.edge.auth.credentials.ClientCredential;
import org.htomar.openakamai.edge.auth.exception.RequestSigningException;
import org.springframework.http.HttpHeaders;

/**
 * Interface describing a request signer that signs service requests.
 *
 * @author Himanshu Tomar
 */
public interface RequestSigner {

    /**
     * Provides authentication headers for the request.
     *
     * @param credential the credential used in the signing.
     * @return the signed request.
     * @throws RequestSigningException if an exception occurs while signing request.
     */
    HttpHeaders sign(ClientCredential credential, String hostName,
                     String invalidationEndPoint, PurgeRequest purgeRequest)
            throws RequestSigningException;
}