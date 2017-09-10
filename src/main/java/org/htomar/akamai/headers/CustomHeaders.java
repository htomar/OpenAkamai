package org.htomar.akamai.headers;

import org.apache.commons.codec.binary.Base64;
import org.htomar.openakamai.edge.auth.credentials.BasicCredential;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;

/**
 * Custom Headers class which is used for adding authorization headers.
 * This class supports 2 kinds of headers
 * <ul>
 * <li>Basic Auth - using {@link BasicCredential}</li>
 * <li>Open Akamai Auth - using custom auth headers</li>
 * </ul>
 *
 * @author Himanshu Tomar
 */
public class CustomHeaders extends HttpHeaders {
    /**
     * Serial Version UID used for serialization.
     */
    private static final long serialVersionUID = 4469426168939318611L;

    /**
     * Constructor using custom auth headers. This is used for
     * Open Akamai authorizations.
     *
     * @param authHeaders custom auth headers.
     */
    public CustomHeaders(final String authHeaders) {
        super();
        set("Authorization", authHeaders);
        setContentType(MediaType.APPLICATION_JSON);
    }

    /**
     * Constructor using basic auth headers.
     *
     * @param basicAuth basic authorization headers
     */
    public CustomHeaders(final BasicCredential basicAuth) {
        String auth = basicAuth.getUsername() + ":" + basicAuth.getPassword();
        byte[] encodedAuth = Base64.encodeBase64(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        set("Authorization", authHeader);
        setContentType(MediaType.APPLICATION_JSON);
        setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    }
}
