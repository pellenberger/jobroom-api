package ch.admin.seco.jobroom.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@JsonIgnoreProperties(ignoreUnknown = true)
class Medium {

    @NotNull
    var isEures: Boolean? = null

    @NotNull
    var isSsi: Boolean? = null

    @NotNull
    var isTeletext: Boolean? = null

    @Min(1)
    @Max(3)
    var teletextChannel: Int? = null

    @Size(max = 117)
    var teletextDescription: String? = null
}
