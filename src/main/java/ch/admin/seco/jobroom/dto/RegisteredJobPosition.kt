package ch.admin.seco.jobroom.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
class RegisteredJobPosition : JobPosition() {

    var egovId: String? = null

    val links: Set<SelfLink> = HashSet()

    var state: String? = null

    var office: EmploymentOffice? = null
}
