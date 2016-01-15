package ch.admin.seco.jobroom.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.validator.constraints.Email

import javax.validation.constraints.*

@JsonIgnoreProperties(ignoreUnknown = true)
class Contact {

    @NotNull
    @Size(min = 2, max = 2)
    @Pattern(regexp = "de|fr|it")
    var language: String? = null

    @NotNull
    @Min(1)
    @Max(2)
    var salutation: Int? = null

    @NotNull
    var firstname: String? = null

    @NotNull
    var lastname: String? = null

    @NotNull
    var phoneNo: String? = null

    @NotNull
    @Email
    private var email: String? = null

}
