package ch.admin.seco.jobroom.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class SelfLink(val href: String) {

    val rel = "self"

}
