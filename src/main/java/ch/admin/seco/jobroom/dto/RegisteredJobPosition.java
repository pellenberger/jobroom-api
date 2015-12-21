package ch.admin.seco.jobroom.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisteredJobPosition extends JobPosition {

    private String egovId;

    private final Set<SelfLink> links = new HashSet<SelfLink>();

    private String state;

    private EmploymentOffice office;

    public String getEgovId() {
        return egovId;
    }

    public void setEgovId(String egovId) {
        this.egovId = egovId;
    }

    public Set<SelfLink> getLinks() {
        return links;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public EmploymentOffice getOffice() {
        return office;
    }

    public void setOffice(EmploymentOffice office) {
        this.office = office;
    }
}
