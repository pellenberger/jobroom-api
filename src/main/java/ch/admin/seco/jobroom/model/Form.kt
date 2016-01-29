package ch.admin.seco.jobroom.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.validator.constraints.Email

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@JsonIgnoreProperties(ignoreUnknown = true)
class Form {

    @NotNull
    var isWrittenApplication: Boolean = false

    @NotNull
    var isElectronicApplication: Boolean = false

    @NotNull
    var isPhoneApplication: Boolean = false

    @Email
    var applicationEMail: String? = null

    var applicationUrl: String? = null

    var applicationPhoneNo: String? = null

    @Size(max = 240)
    var applicationRemarks: String? = null
}
