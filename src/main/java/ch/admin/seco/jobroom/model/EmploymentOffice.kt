package ch.admin.seco.jobroom.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class EmploymentOffice {

    var code: String? = null

    var nameDe: String? = null

    var nameFr: String? = null

    var nameIt: String? = null

    var streetDe: String? = null

    var streetFr: String? = null

    var streetIt: String? = null

    var houseNumber: String? = null

    var zip: String? = null

    var cityDe: String? = null

    var cityFr: String? = null

    var cityIt: String? = null

    var phone: String? = null

    var fax: String? = null

    var email: String? = null

}
