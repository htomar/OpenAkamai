package org.htomar.openakamai.edge.auth.credentials;

import org.springframework.util.Assert;

/**
 * Default implementation of the {@link ClientCredential}.
 *
 */
public class DefaultCredential implements ClientCredential {

	/**
	 * The client token.
	 */
	private final String clientToken;

	/**
	 * The access token.
	 */
	private final String accessToken;

	/**
	 * The secret associated with the client token.
	 */
	private final String clientSecret;

	/**
	 * Constructor.
	 * 
	 * @param clientToken
	 *            the client token, cannot be null or empty.
	 * @param accessToken
	 *            the access token, cannot be null or empty.
	 * @param clientSecret
	 *            the client secret, cannot be null or empty.
	 * 
	 * @throws IllegalArgumentException
	 *             if any of the parameters is null or empty.
	 */
	public DefaultCredential(String clientToken, String accessToken,
			String clientSecret) {
		Assert.hasText(clientToken);
		Assert.hasText(accessToken);
		Assert.hasText(clientSecret);

		this.clientToken = clientToken;
		this.accessToken = accessToken;
		this.clientSecret = clientSecret;
	}

	/**
	 * Gets the client token.
	 * 
	 * @return The client token.
	 */
	@Override
	public String getClientToken() {
		return clientToken;
	}

	/**
	 * Gets the access token.
	 * 
	 * @return the access token.
	 */
	@Override
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * Gets the secret associated with the client token.
	 * 
	 * @return the secret.
	 */
	@Override
	public String getClientSecret() {
		return clientSecret;
	}
}