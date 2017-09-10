package org.htomar.akamai.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Purge response object used by both CCU v2 & v3.
 *
 * @author Himanshu Tomar
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurgeResponse {
    private String httpStatus;
    private String detail;
    private String estimatedSeconds;
    private String purgeId;
    private String supportId;
    private String progressUri;
    private String pingAfterSeconds;

    /**
     * @return the httpStatus
     */
    public String getHttpStatus() {
        return httpStatus;
    }

    /**
     * @param httpStatus the httpStatus to set
     */
    public void setHttpStatus(String httpStatus) {
        this.httpStatus = httpStatus;
    }

    /**
     * @return the detail
     */
    public String getDetail() {
        return detail;
    }

    /**
     * @param detail the detail to set
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * @return the estimatedSeconds
     */
    public String getEstimatedSeconds() {
        return estimatedSeconds;
    }

    /**
     * @param estimatedSeconds the estimatedSeconds to set
     */
    public void setEstimatedSeconds(String estimatedSeconds) {
        this.estimatedSeconds = estimatedSeconds;
    }

    /**
     * @return the purgeId
     */
    public String getPurgeId() {
        return purgeId;
    }

    /**
     * @param purgeId the purgeId to set
     */
    public void setPurgeId(String purgeId) {
        this.purgeId = purgeId;
    }

    /**
     * @return the supportId
     */
    public String getSupportId() {
        return supportId;
    }

    /**
     * @param supportId the supportId to set
     */
    public void setSupportId(String supportId) {
        this.supportId = supportId;
    }

    @Override
    public String toString() {
        return "PurgeResponse [httpStatus=" + httpStatus + ", detail=" + detail
                + ", estimatedSeconds=" + estimatedSeconds + ", purgeId="
                + purgeId + ", supportId=" + supportId + ", progressUri="
                + progressUri + ", pingAfterSeconds=" + pingAfterSeconds + "]";
    }

    /**
     * @return the progressUri
     */
    public String getProgressUri() {
        return progressUri;
    }

    /**
     * @param progressUri the progressUri to set
     */
    public void setProgressUri(String progressUri) {
        this.progressUri = progressUri;
    }

    /**
     * @return the pingAfterSeconds
     */
    public String getPingAfterSeconds() {
        return pingAfterSeconds;
    }

    /**
     * @param pingAfterSeconds the pingAfterSeconds to set
     */
    public void setPingAfterSeconds(String pingAfterSeconds) {
        this.pingAfterSeconds = pingAfterSeconds;
    }
}