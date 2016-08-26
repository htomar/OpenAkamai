package org.htomar.openakamai.edge.auth.signer;

import org.htomar.openakamai.edge.auth.credentials.ClientCredential;
import org.htomar.openakamai.edge.auth.exception.RequestSigningException;
import org.htomar.openakamai.edge.auth.request.PurgeRequest;
import org.springframework.http.HttpHeaders;

/**
 * Interface describing a request signer that signs service requests.
 *
 */
public interface RequestSigner {

	/**
	 * Provides authentication headers for the request.
	 * 
	 * @param request
	 *            the request to sign.
	 * @param credential
	 *            the credential used in the signing.
	 * @return the signed request.
	 * @throws RequestSigningException
	 */
	HttpHeaders sign(ClientCredential credential, String hostName,
			String invalidationEndPoint, PurgeRequest purgeRequest)
			throws RequestSigningException;
}