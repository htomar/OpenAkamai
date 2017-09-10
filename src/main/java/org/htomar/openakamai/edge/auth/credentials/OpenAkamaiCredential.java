package org.htomar.openakamai.edge.auth.credentials;

import org.springframework.util.Assert;

/**
 * {@inheritDoc}
 * Default implementation of the {@link ClientCredential}.
 *
 * @author Himanshu Tomar
 */
public class OpenAkamaiCredential implements ClientCredential {

    /**
     * The client token assigned to the client.
     */
    private final String clientToken;

    /**
     * The access token assigned to the client.
     */
    private final String accessToken;

    /**
     * The secret associated with the client.
     */
    private final String clientSecret;

    /**
     * The base URL associated with the client token.
     */
    private final String baseURL;

    /**
     * Constructor.
     *
     * @param clientToken  the client token, cannot be null or empty.
     * @param accessToken  the access token, cannot be null or empty.
     * @param clientSecret the client secret, cannot be null or empty.
     * @param baseURL      the base URL, cannot be null or empty.
     * @throws IllegalArgumentException if any of the parameters is null or empty.
     */
    public OpenAkamaiCredential(String clientToken, String accessToken,
                                String clientSecret, final String baseURL) {
        Assert.hasText(clientToken,
                "client token cannot be null.");
        Assert.hasText(accessToken,
                "access token cannot be null.");
        Assert.hasText(clientSecret,
                "client secret cannot be null.");
        Assert.hasText(baseURL,
                "base URL cannot be null.");

        this.clientToken = clientToken;
        this.accessToken = accessToken;
        this.clientSecret = clientSecret;
        this.baseURL = baseURL;
    }

    /**
     * Returns the client token.
     *
     * @return The client token.
     */
    @Override
    public String getClientToken() {
        return clientToken;
    }

    /**
     * Returns the access token.
     *
     * @return the access token.
     */
    @Override
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Returns the secret associated with the client token.
     *
     * @return the secret.
     */
    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    /**
     * Returns the base URL for the purge request.
     *
     * @return the base URL.
     */
    @Override
    public String getBaseURL() {
        return baseURL;
    }
}