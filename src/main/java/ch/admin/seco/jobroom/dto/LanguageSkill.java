package ch.admin.seco.jobroom.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Dto representing language skill data of a job
 *
 * @since 1.4.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LanguageSkill {

    @Min(1)
    @Max(40)
    private Integer language;

    @Min(1)
    @Max(3)
    private Integer languageOralSkill;

    @Min(1)
    @Max(3)
    private Integer languageWrittenSkill;

    public LanguageSkill() {
    }

    public LanguageSkill(Integer language, Integer languageOralSkill, Integer languageWrittenSkill) {
        this.language = language;
        this.languageOralSkill = languageOralSkill;
        this.languageWrittenSkill = languageWrittenSkill;
    }

    public Integer getLanguage() {
        return language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Integer getLanguageOralSkill() {
        return languageOralSkill;
    }

    public void setLanguageOralSkill(Integer languageOralSkill) {
        this.languageOralSkill = languageOralSkill;
    }

    public Integer getLanguageWrittenSkill() {
        return languageWrittenSkill;
    }

    public void setLanguageWrittenSkill(Integer languageWrittenSkill) {
        this.languageWrittenSkill = languageWrittenSkill;
    }
}
