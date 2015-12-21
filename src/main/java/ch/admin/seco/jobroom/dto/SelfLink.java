package ch.admin.seco.jobroom.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Link representing a restful resource
 *
 * @since 1.4.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SelfLink {

    private final String rel = "self";

    private final String href;

    public SelfLink(String href) {
        this.href = href;
    }

    public String getRel() {
        return rel;
    }

    public String getHref() {
        return href;
    }
}
