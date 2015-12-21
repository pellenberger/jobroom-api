package ch.admin.seco.jobroom.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Dto representing medium data of a job
 *
 * @since 1.4.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Medium {

    @NotNull
    private Boolean eures;

    @NotNull
    private Boolean ssi;

    @NotNull
    private Boolean teletext;

    @Min(1)
    @Max(3)
    private Integer teletextChannel;

    @Size(max = 117)
    private String teletextDescription;

    public Boolean isEures() {
        return eures;
    }

    public void setEures(Boolean eures) {
        this.eures = eures;
    }

    public Boolean isSsi() {
        return ssi;
    }

    public void setSsi(Boolean ssi) {
        this.ssi = ssi;
    }

    public Boolean isTeletext() {
        return teletext;
    }

    public void setTeletext(Boolean teletext) {
        this.teletext = teletext;
    }

    public Integer getTeletextChannel() {
        return teletextChannel;
    }

    public void setTeletextChannel(Integer teletextChannel) {
        this.teletextChannel = teletextChannel;
    }

    public String getTeletextDescription() {
        return teletextDescription;
    }

    public void setTeletextDescription(String teletextDescription) {
        this.teletextDescription = teletextDescription;
    }
}
