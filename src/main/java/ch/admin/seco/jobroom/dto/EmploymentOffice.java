package ch.admin.seco.jobroom.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Dto representing the "RAV" business object, used in the API module of the Job-Room
 *
 * @since 1.4.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmploymentOffice {

    private String code;

    private String nameDe;

    private String nameFr;

    private String nameIt;

    private String streetDe;

    private String streetFr;

    private String streetIt;

    private String houseNumber;

    private String zip;

    private String cityDe;

    private String cityFr;

    private String cityIt;

    private String phone;

    private String fax;

    private String eMail;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNameDe() {
        return nameDe;
    }

    public void setNameDe(String nameDe) {
        this.nameDe = nameDe;
    }

    public String getNameFr() {
        return nameFr;
    }

    public void setNameFr(String nameFr) {
        this.nameFr = nameFr;
    }

    public String getNameIt() {
        return nameIt;
    }

    public void setNameIt(String nameIt) {
        this.nameIt = nameIt;
    }

    public String getStreetDe() {
        return streetDe;
    }

    public void setStreetDe(String streetDe) {
        this.streetDe = streetDe;
    }

    public String getStreetFr() {
        return streetFr;
    }

    public void setStreetFr(String streetFr) {
        this.streetFr = streetFr;
    }

    public String getStreetIt() {
        return streetIt;
    }

    public void setStreetIt(String streetIt) {
        this.streetIt = streetIt;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCityDe() {
        return cityDe;
    }

    public void setCityDe(String cityDe) {
        this.cityDe = cityDe;
    }

    public String getCityFr() {
        return cityFr;
    }

    public void setCityFr(String cityFr) {
        this.cityFr = cityFr;
    }

    public String getCityIt() {
        return cityIt;
    }

    public void setCityIt(String cityIt) {
        this.cityIt = cityIt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
}
