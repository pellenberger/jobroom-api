package ch.admin.seco.jobroom.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class SelfLink(val href: String) {

    val rel = "self"

}
