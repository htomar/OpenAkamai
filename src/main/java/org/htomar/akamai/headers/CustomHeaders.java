package org.htomar.akamai.headers;

import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.htomar.akamai.cache.auth.BasicAuth;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class CustomHeaders extends HttpHeaders {
	private static final long serialVersionUID = 4469426168939318611L;

	public CustomHeaders(final String authHeaders) {
		super();
		set("Authorization", authHeaders);
		setContentType(MediaType.APPLICATION_JSON);
		// setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	}

	public CustomHeaders(final BasicAuth basicAuth) {
		String auth = basicAuth.getUsername() + ":" + basicAuth.getPassword();
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
		String authHeader = "Basic " + new String(encodedAuth);
		set("Authorization", authHeader);
		setContentType(MediaType.APPLICATION_JSON);
		setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	}
}
