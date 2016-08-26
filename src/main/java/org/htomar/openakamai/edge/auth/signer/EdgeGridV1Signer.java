package org.htomar.openakamai.edge.auth.signer;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.htomar.akamai.headers.CustomHeaders;
import org.htomar.openakamai.edge.auth.credentials.ClientCredential;
import org.htomar.openakamai.edge.auth.exception.RequestSigningException;
import org.htomar.openakamai.edge.auth.request.PurgeRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Class representing the EdgeGrid version 1 signer that implements the
 * {@link RequestSigner}.
 * 
 * <p>
 * The signer sets the Authorization header in the request as algorithm name, '
 * ' (space), followed by an ordered list of name=value fields separated with
 * ';'.
 * </p>
 * 
 * <p>
 * The names of the fields are:
 * </p>
 * 
 * <ol>
 * <li>client_token: for the client token;</li>
 * <li>access_token: for the access token;</li>
 * <li>timestamp: for the timestamp when the request is signed;</li>
 * <li></li> nonce: for possible nonce checking;
 * <li>signature: for the request signature.</li>
 * </ol>
 *
 */
public class EdgeGridV1Signer implements RequestSigner {

	/**
	 * The logger used for logging.
	 */
	private static final Logger LOGGER = LoggerFactory
			.getLogger(EdgeGridV1Signer.class);

	/**
	 * The signing algorithm for the EdgeGrid version 1 protocol.
	 */
	private static final String ALGORHTM = "EG1-HMAC-SHA256";

	/**
	 * The HMAC algorithm used.
	 */
	private static final String HMAC_ALG = "HmacSHA256";

	/**
	 * The message digest algorithm used.
	 */
	private static final String MD_ALG = "SHA-256";

	/**
	 * The field name for the client token in the authorization header.
	 */
	private static final String AUTH_CLIENT_TOKEN_NAME = "client_token";

	/**
	 * The field name for the access token in the authorization header.
	 */
	private static final String AUTH_ACCESS_TOKEN_NAME = "access_token";

	/**
	 * The field name for the time stamp in the authorization header.
	 */
	private static final String AUTH_TIMESTAMP_NAME = "timestamp";

	/**
	 * The field name for the nonce in the authorization header.
	 */
	private static final String AUTH_NONCE_NAME = "nonce";

	/**
	 * The field name for the signature in the authorization header.
	 */
	private static final String AUTH_SIGNATURE_NAME = "signature";

	/**
	 * The maximum allowed body size in bytes for POST and PUT requests.
	 */
	private int maxBodySize = 50000;

	/**
	 * The charset used for String to bytes conversions.
	 */
	private final Charset UTF8_CHARSET = Charset.forName("UTF-8");

	/**
	 * Constructor
	 * 
	 * <p>
	 * Note: the parameters should be published by the service provider when the
	 * service is published. Refer to the API documentation for any special
	 * instructions.
	 * </p>
	 * 
	 * @param headers
	 *            the ordered list of header names to include in the signature.
	 * @param maxBodySize
	 *            the maximum allowed body size in bytes for POST and PUT
	 *            requests.
	 */
	public EdgeGridV1Signer(final int maxBodySize) {
		this.maxBodySize = maxBodySize;
	}

	public EdgeGridV1Signer() {
	}

	/**
	 * Signs the given request with the given client credential.
	 * 
	 * @param request
	 *            the request to sign.
	 * @param credential
	 *            the credential used in the signing.
	 * @param purgeRequest
	 * @param invalidationEndPoint
	 * @return the signed request.
	 * @throws RequestSigningException
	 */
	@Override
	public CustomHeaders sign(final ClientCredential credential,
			final String hostName, final String invalidationEndPoint,
			final PurgeRequest purgeRequest) throws RequestSigningException {
		CustomHeaders signedHeaders = null;
		long currentTime = System.currentTimeMillis();
		String timeStamp = getTimeStamp(currentTime);

		StringBuilder sb = getAuthHeaders(credential, timeStamp);
		String authData = sb.toString();

		String clientSecret = credential.getClientSecret();
		byte[] signingKeyBytes = sign(timeStamp,
				clientSecret.getBytes(UTF8_CHARSET), HMAC_ALG);
		String signingKey = Base64.encodeBase64String(signingKeyBytes);

		StringBuilder signData = new StringBuilder(contentToBeSigned(hostName,
				invalidationEndPoint, purgeRequest, authData));
		signData.append(authData);
		String stringToSign = signData.toString();

		LOGGER.info(String.format("String to sign : '%s'", stringToSign));

		byte[] signatureBytes = sign(stringToSign,
				signingKey.getBytes(UTF8_CHARSET), HMAC_ALG);
		String signature = Base64.encodeBase64String(signatureBytes);

		LOGGER.info(String.format("Signature : '%s'", signature));

		// add the signature
		sb.append(AUTH_SIGNATURE_NAME);
		sb.append('=');
		sb.append(signature);

		signedHeaders = new CustomHeaders(sb.toString());
		LOGGER.info("Final Headers: " + sb.toString());
		return signedHeaders;
	}

	private StringBuilder getAuthHeaders(ClientCredential credential,
			String timeStamp) {
		StringBuilder sb = new StringBuilder();
		sb.append(ALGORHTM);
		sb.append(' ');
		sb.append(AUTH_CLIENT_TOKEN_NAME);
		sb.append('=');
		sb.append(credential.getClientToken());
		sb.append(';');

		sb.append(AUTH_ACCESS_TOKEN_NAME);
		sb.append('=');
		sb.append(credential.getAccessToken());
		sb.append(';');

		sb.append(AUTH_TIMESTAMP_NAME);
		sb.append('=');
		sb.append(timeStamp);
		sb.append(';');

		sb.append(AUTH_NONCE_NAME);
		sb.append('=');
		sb.append(UUID.randomUUID().toString());
		sb.append(';');
		return sb;
	}

	protected String getHeaders() {
		// return "content-type:application/json\taccept:application/json\t";
		return "";
	}

	protected String contentToBeSigned(final String hostName,
			final String invalidationEndPoint, final PurgeRequest purgeRequest,
			final String authData) throws RequestSigningException {
		StringBuilder contentToBeSigned = new StringBuilder();
		contentToBeSigned.append("POST\thttps\t")
				.append(hostName.replaceAll("https://", "")).append("\t")
				.append(invalidationEndPoint).append("\t").append(getHeaders())
				.append("\t").append(getContentHash(purgeRequest)).append("\t");
		return contentToBeSigned.toString();
	}

	/**
	 * Helper method to calculate the HMAC signature of a given string.
	 * 
	 * @param s
	 *            the string to sign.
	 * @param key
	 *            the key for the signature.
	 * @param algorithm
	 *            the signing algorithm.
	 * @return the HMac signature.
	 * @throws RequestSigningException
	 */
	private byte[] sign(String s, byte[] key, String algorithm)
			throws RequestSigningException {
		try {
			SecretKeySpec signingKey = new SecretKeySpec(key, algorithm);
			Mac mac = Mac.getInstance(algorithm);
			mac.init(signingKey);

			byte[] valueBytes = s.getBytes(UTF8_CHARSET);
			return mac.doFinal(valueBytes);
		} catch (NoSuchAlgorithmException nsae) {
			throw new RequestSigningException(
					"Failed to sign: algorithm not found", nsae);
		} catch (InvalidKeyException ike) {
			throw new RequestSigningException("Failed to sign: invalid key",
					ike);
		}
	}

	private String getContentHash(final PurgeRequest purgeRequest)
			throws RequestSigningException {
		String contentHash = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			StringWriter stringWriter = new StringWriter();
			mapper.writeValue(stringWriter, purgeRequest);
			String content = stringWriter.toString();
			LOGGER.info("Content String: " + content);
			if (content != null) {
				byte[] contentBytes = content.getBytes(UTF8_CHARSET);

				int lengthToHash = content.length();
				if (lengthToHash > maxBodySize) {
					LOGGER.warn(String.format(
							"Message body length '%d' is larger than the max '%d'. "
									+ "Using '%d' bytes for computing the hash.",
							lengthToHash, maxBodySize, maxBodySize));
					lengthToHash = maxBodySize;
				} else {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(String.format("Content: %s",
								Base64.encodeBase64String(contentBytes)));
					}
				}
				byte[] digestBytes = getHash(contentBytes, 0, lengthToHash);
				contentHash = Base64.encodeBase64String(digestBytes);
			}

		} catch (IOException ioException) {
			throw new RequestSigningException(
					"Failed to get json from the request object", ioException);
		}
		return contentHash;
	}

	/**
	 * Helper method to calculate the message digest.
	 * 
	 * @param contentBytes
	 *            the content bytes for digesting.
	 * @return the digest.
	 * @throws RequestSigningException
	 */
	private byte[] getHash(byte[] contentBytes, int offset, int len)
			throws RequestSigningException {
		try {
			MessageDigest md = MessageDigest.getInstance(MD_ALG);
			md.update(contentBytes, offset, len);
			return md.digest();
		} catch (NoSuchAlgorithmException nsae) {
			throw new RequestSigningException(
					"Failed to get request hash: algorithm not found", nsae);
		}
	}

	/**
	 * Helper to get the formatted time stamp.
	 * 
	 * @param time
	 *            the time stamp as millisecond since the UNIX epoch.
	 * @return the formatted time stamp.
	 */
	private static String getTimeStamp(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ssZ");
		Date date = new Date(time);

		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		return format.format(date);
	}
}
