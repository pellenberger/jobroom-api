package ch.admin.seco.jobroom.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Dto representing (application)form data of a job
 *
 * @since 1.4.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Form {

    @NotNull
    private boolean writtenApplication;

    @NotNull
    private boolean electronicApplication;

    @NotNull
    private boolean phoneApplication;

    @Email
    private String applicationEMail;

    private String applicationUrl;

    private String applicationPhoneNo;

    @Size(max = 240)
    private String applicationRemarks;

    public boolean isWrittenApplication() {
        return writtenApplication;
    }

    public void setWrittenApplication(boolean writtenApplication) {
        this.writtenApplication = writtenApplication;
    }

    public boolean isElectronicApplication() {
        return electronicApplication;
    }

    public void setElectronicApplication(boolean electronicApplication) {
        this.electronicApplication = electronicApplication;
    }

    public String getApplicationEMail() {
        return applicationEMail;
    }

    public void setApplicationEMail(String applicationEMail) {
        this.applicationEMail = applicationEMail;
    }

    public String getApplicationUrl() {
        return applicationUrl;
    }

    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl;
    }

    public boolean isPhoneApplication() {
        return phoneApplication;
    }

    public void setPhoneApplication(boolean phoneApplication) {
        this.phoneApplication = phoneApplication;
    }

    public String getApplicationPhoneNo() {
        return applicationPhoneNo;
    }

    public void setApplicationPhoneNo(String applicationPhoneNo) {
        this.applicationPhoneNo = applicationPhoneNo;
    }

    public String getApplicationRemarks() {
        return applicationRemarks;
    }

    public void setApplicationRemarks(String applicationRemarks) {
        this.applicationRemarks = applicationRemarks;
    }
}
