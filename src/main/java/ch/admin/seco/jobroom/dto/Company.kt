package ch.admin.seco.jobroom.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@JsonIgnoreProperties(ignoreUnknown = true)
class Company {

    @NotNull
    var name: String? = null

    @NotNull
    var street: String? = null

    var houseNo: String? = null

    @NotNull
    @Digits(integer = 5, fraction = 0)
    var zip: String? = null

    @NotNull
    var city: String? = null

    @NotNull
    @Size(min = 2, max = 2)
    //CHECKSTYLE:OFF
    @Pattern(regexp = "^(A(D|E|F|G|I|L|M|N|O|R|S|T|Q|U|W|X|Z)|B(A|B|D|E|F|G|H|I|J|L|M|N|O|R|S|T|V|W|Y|Z)|C(A|C|D|F|G|H|I|K|L|M|N|O|R|U|V|X|Y|Z)|D(E|J|K|M|O|Z)|E(C|E|G|H|R|S|T)|F(I|J|K|M|O|R)|G(A|B|D|E|F|G|H|I|L|M|N|P|Q|R|S|T|U|W|Y)|H(K|M|N|R|T|U)|I(D|E|Q|L|M|N|O|R|S|T)|J(E|M|O|P)|K(E|G|H|I|M|N|P|R|W|Y|Z)|L(A|B|C|I|K|R|S|T|U|V|Y)|M(A|C|D|E|F|G|H|K|L|M|N|O|Q|P|R|S|T|U|V|W|X|Y|Z)|N(A|C|E|F|G|I|L|O|P|R|U|Z)|OM|P(A|E|F|G|H|K|L|M|N|R|S|T|W|Y)|QA|R(E|O|S|U|W)|S(A|B|C|D|E|G|H|I|J|K|L|M|N|O|R|T|V|Y|Z)|T(C|D|F|G|H|J|K|L|M|N|O|R|T|V|W|Z)|U(A|G|M|S|Y|Z)|V(A|C|E|G|I|N|U)|W(F|S)|Y(E|T)|Z(A|M|W))$")
            //CHECKSTYLE:ON
    var countryCode: String? = null

    var postboxNo: String? = null

    @Digits(integer = 5, fraction = 0)
    var postboxZip: String? = null

    var postboxCity: String? = null
}
