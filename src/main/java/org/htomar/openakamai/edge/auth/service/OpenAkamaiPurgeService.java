package org.htomar.openakamai.edge.auth.service;

import java.util.stream.Collectors;

import org.htomar.openakamai.edge.auth.credentials.ClientCredential;
import org.htomar.openakamai.edge.auth.exception.RequestSigningException;
import org.htomar.openakamai.edge.auth.request.PurgeRequest;
import org.htomar.openakamai.edge.auth.request.PurgeResponse;
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

public class OpenAkamaiPurgeService {
	/**
	 * The logger used for logging.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OpenAkamaiPurgeService.class);
	public static final String DEFAULT_INVALIDATE_ENDPOINT = "/ccu/v3/invalidate/url/production";

	private EdgeGridV1Signer signer = new EdgeGridV1Signer();

	public PurgeResponse purgeByURL(final PurgeRequest purgeRequest,
			final ClientCredential credential, String hostName)
			throws RestClientException, RequestSigningException {
		preProcessRequest(purgeRequest, hostName);
		hostName = hostName.toLowerCase();
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<PurgeResponse> responseEntity = restTemplate.exchange(
				hostName + DEFAULT_INVALIDATE_ENDPOINT, HttpMethod.POST,
				new HttpEntity<>(purgeRequest,
						signer.sign(credential, hostName,
								DEFAULT_INVALIDATE_ENDPOINT, purgeRequest)),
				PurgeResponse.class);
		LOGGER.debug(responseEntity.getBody().toString());
		return responseEntity.getBody();
	}

	private void preProcessRequest(final PurgeRequest purgeRequest,
			final String hostName) {
		Assert.hasText(hostName);
		Assert.isTrue(hostName.toLowerCase().startsWith("https"));
		preProcessPurgeRequest(purgeRequest);
	}

	private void preProcessPurgeRequest(final PurgeRequest purgeRequest) {
		Assert.notEmpty(purgeRequest.getObjects());
		purgeRequest.setObjects(purgeRequest.getObjects().stream()
				.filter(url -> StringUtils.hasText(url))
				.map(url -> url.startsWith("/") || url.startsWith("http")
						? url.toLowerCase()
						: "/" + url.toLowerCase())
				.collect(Collectors.toList()));
	}
}
