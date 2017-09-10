package org.htomar.akamai.request;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Purge request object used for both CCU v2 & v3.
 *
 * @author Himanshu Tomar
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PurgeRequest<T> {
    private String type;
    private String hostname;
    private List<T> objects;

    /**
     * @return the hostname
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * @param hostname the hostname to set
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * @return the objects
     */
    public List<T> getObjects() {
        return objects;
    }

    /**
     * @param objects the objects to set
     */
    public void setObjects(List<T> objects) {
        this.objects = objects;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PurgeRequest [type=" + type + ", hostname=" + hostname
                + ", objects=" + objects + "]";
    }
}
