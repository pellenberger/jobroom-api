package ch.admin.seco.jobroom.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import javax.validation.constraints.Max
import javax.validation.constraints.Min

@JsonIgnoreProperties(ignoreUnknown = true)
class LanguageSkill {

    @Min(1)
    @Max(40)
    var language: Int? = null

    @Min(1)
    @Max(3)
    var languageOralSkill: Int? = null

    @Min(1)
    @Max(3)
    var languageWrittenSkill: Int? = null

    constructor() {
    }

    constructor(language: Int?, languageOralSkill: Int?, languageWrittenSkill: Int?) {
        this.language = language
        this.languageOralSkill = languageOralSkill
        this.languageWrittenSkill = languageWrittenSkill
    }
}
