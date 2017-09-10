package org.htomar.openakamai.edge.auth.credentials;

import org.springframework.util.Assert;

/**
 * Class used for basic auth mechanism. Accepts username and password for authentication.
 * @author Himanshu Tomar
 */
public class BasicCredential implements Credential {
    private final String username;
    private final String password;

    /**
     * Constructor for BasicCredential class. Validates input and throws
     * {@link IllegalArgumentException} in case input are not valid.
     *
     * @param username basic auth username
     * @param password basic auth password
     */
    public BasicCredential(final String username, final String password) {
        Assert.hasText(username,
                "username cannot be null.");
        Assert.hasText(password,
                "password cannot be null.");
        this.username = username;
        this.password = password;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }
}
