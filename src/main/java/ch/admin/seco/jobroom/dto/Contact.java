package ch.admin.seco.jobroom.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.*;

/**
 * Dto representing contact data of a job
 *
 * @since 1.4.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contact {

    @NotNull
    @Size(min = 2, max = 2)
    @Pattern(regexp = "de|fr|it")
    private String language;

    @NotNull
    @Min(1)
    @Max(2)
    private Integer salutation;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    private String phoneNo;

    @NotNull
    @Email
    private String eMail;

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Integer getSalutation() {
        return salutation;
    }

    public void setSalutation(Integer salutation) {
        this.salutation = salutation;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
}
