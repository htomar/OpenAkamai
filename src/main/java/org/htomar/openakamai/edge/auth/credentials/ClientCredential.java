package org.htomar.openakamai.edge.auth.credentials;

/**
 * Interface representing the client credential that is used in service
 * requests.
 * <p>
 * It contains the client token that represents the service client, the client
 * secret that is associated with the client token used for request signing, and
 * the access token that represents the authorizations the client has for
 * accessing the service.
 *
 * @author Himanshu Tomar
 */
public interface ClientCredential extends Credential {
    /**
     * Gets the client token.
     *
     * @return The client token.
     */
    String getClientToken();

    /**
     * Gets the access token.
     *
     * @return the access token.
     */
    String getAccessToken();

    /**
     * Gets the secret associated with the client.
     *
     * @return the secret.
     */
    String getClientSecret();

    /**
     * Gets the base URL associated with the client.
     *
     * @return the secret.
     */
    String getBaseURL();
}
