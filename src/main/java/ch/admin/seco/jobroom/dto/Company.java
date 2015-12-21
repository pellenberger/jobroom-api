package ch.admin.seco.jobroom.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Dto representing company data of a job
 *
 * @since 1.4.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Company {

    @NotNull
    private String name;

    @NotNull
    private String street;

    private String houseNo;

    @NotNull
    @Digits(integer = 5, fraction = 0)
    private String zip;

    @NotNull
    private String city;

    @NotNull
    @Size(min = 2, max = 2)
    //CHECKSTYLE:OFF
    @Pattern(regexp = "^(A(D|E|F|G|I|L|M|N|O|R|S|T|Q|U|W|X|Z)|B(A|B|D|E|F|G|H|I|J|L|M|N|O|R|S|T|V|W|Y|Z)|C(A|C|D|F|G|H|I|K|L|M|N|O|R|U|V|X|Y|Z)|D(E|J|K|M|O|Z)|E(C|E|G|H|R|S|T)|F(I|J|K|M|O|R)|G(A|B|D|E|F|G|H|I|L|M|N|P|Q|R|S|T|U|W|Y)|H(K|M|N|R|T|U)|I(D|E|Q|L|M|N|O|R|S|T)|J(E|M|O|P)|K(E|G|H|I|M|N|P|R|W|Y|Z)|L(A|B|C|I|K|R|S|T|U|V|Y)|M(A|C|D|E|F|G|H|K|L|M|N|O|Q|P|R|S|T|U|V|W|X|Y|Z)|N(A|C|E|F|G|I|L|O|P|R|U|Z)|OM|P(A|E|F|G|H|K|L|M|N|R|S|T|W|Y)|QA|R(E|O|S|U|W)|S(A|B|C|D|E|G|H|I|J|K|L|M|N|O|R|T|V|Y|Z)|T(C|D|F|G|H|J|K|L|M|N|O|R|T|V|W|Z)|U(A|G|M|S|Y|Z)|V(A|C|E|G|I|N|U)|W(F|S)|Y(E|T)|Z(A|M|W))$")
    //CHECKSTYLE:ON
    private String countryCode;

    private String postboxNo;

    @Digits(integer = 5, fraction = 0)
    private String postboxZip;

    private String postboxCity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public void setHouseNo(String houseNo) {
        this.houseNo = houseNo;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostboxNo() {
        return postboxNo;
    }

    public void setPostboxNo(String postboxNo) {
        this.postboxNo = postboxNo;
    }

    public String getPostboxZip() {
        return postboxZip;
    }

    public void setPostboxZip(String postboxZip) {
        this.postboxZip = postboxZip;
    }

    public String getPostboxCity() {
        return postboxCity;
    }

    public void setPostboxCity(String postboxCity) {
        this.postboxCity = postboxCity;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
