package ch.admin.seco.jobroom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JobPosition {

    @NotEmpty
    @Size(max = 240)
    private String title;

    @NotEmpty
    @Size(max = 1900)
    private String description;

    @NotEmpty
    @Size(min = 2, max = 2)
    //CHECKSTYLE:OFF
    @Pattern(regexp = "^(A(D|E|F|G|I|L|M|N|O|R|S|T|Q|U|W|X|Z)|B(A|B|D|E|F|G|H|I|J|L|M|N|O|R|S|T|V|W|Y|Z)|C(A|C|D|F|G|H|I|K|L|M|N|O|R|U|V|X|Y|Z)|D(E|J|K|M|O|Z)|E(C|E|G|H|R|S|T)|F(I|J|K|M|O|R)|G(A|B|D|E|F|G|H|I|L|M|N|P|Q|R|S|T|U|W|Y)|H(K|M|N|R|T|U)|I(D|E|Q|L|M|N|O|R|S|T)|J(E|M|O|P)|K(E|G|H|I|M|N|P|R|W|Y|Z)|L(A|B|C|I|K|R|S|T|U|V|Y)|M(A|C|D|E|F|G|H|K|L|M|N|O|Q|P|R|S|T|U|V|W|X|Y|Z)|N(A|C|E|F|G|I|L|O|P|R|U|Z)|OM|P(A|E|F|G|H|K|L|M|N|R|S|T|W|Y)|QA|R(E|O|S|U|W)|S(A|B|C|D|E|G|H|I|J|K|L|M|N|O|R|T|V|Y|Z)|T(C|D|F|G|H|J|K|L|M|N|O|R|T|V|W|Z)|U(A|G|M|S|Y|Z)|V(A|C|E|G|I|N|U)|W(F|S)|Y(E|T)|Z(A|M|W))$")
    //CHECKSTYLE:ON
    private String countryCode;

    @NotEmpty
    @Digits(integer = 5, fraction = 0)
    private String zip;

    @NotEmpty
    @Size(max = 50)
    private String city;

//    @NotNull
//    @Max(100)
//    @Min(0)
//    private Integer workloadFrom;
//
//    @Max(100)
//    @Min(0)
//    private Integer workloadTo;
//
//    @NotNull
//    private boolean startImmediate;
//
//    @NotNull
//    private boolean perpetual;
//
//    @Future
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    private Date startDate;
//
//    @Future
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    private Date durationLimit;
//
//    private String locationRemark;
//
//    private String job;
//
//    @Max(16)
//    @Min(1)
//    private Integer education;
//
//    private Integer experience;
//
//    @Max(99)
//    @Min(15)
//    private Integer ageFrom;
//
//    @Max(99)
//    @Min(15)
//    private Integer ageTo;
//
//    @Min(1)
//    @Max(2)
//    private Integer gender;
//
//    @Min(1)
//    @Max(70)
//    private Integer driverLicense;
//
//    private boolean testDrive;
//
//    @Size(min = 0, max = 5)
//    private List<LanguageSkill> languageSkills;
//
//    private Company company;
//
//    private Contact contact;
//
//    private Form form;
//
//    private Medium medium;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

//    public String getLocationRemark() {
//        return locationRemark;
//    }
//
//    public void setLocationRemark(String locationRemark) {
//        this.locationRemark = locationRemark;
//    }
//
//    public Integer getWorkloadFrom() {
//        return workloadFrom;
//    }
//
//    public void setWorkloadFrom(Integer workloadFrom) {
//        this.workloadFrom = workloadFrom;
//    }
//
//    public Integer getWorkloadTo() {
//        return workloadTo;
//    }
//
//    public void setWorkloadTo(Integer workloadTo) {
//        this.workloadTo = workloadTo;
//    }
//
//    public boolean isStartImmediate() {
//        return startImmediate;
//    }
//
//    public void setStartImmediate(boolean startImmediate) {
//        this.startImmediate = startImmediate;
//    }
//
//    public Date getStartDate() {
//        return startDate;
//    }
//
//    public void setStartDate(Date startDate) {
//        this.startDate = startDate;
//    }
//
//    public boolean isPerpetual() {
//        return perpetual;
//    }
//
//    public void setPerpetual(boolean perpetual) {
//        this.perpetual = perpetual;
//    }
//
//    public Date getDurationLimit() {
//        return durationLimit;
//    }
//
//    public void setDurationLimit(Date durationLimit) {
//        this.durationLimit = durationLimit;
//    }
//
//    public Company getCompany() {
//        return company;
//    }
//
//    public void setCompany(Company company) {
//        this.company = company;
//    }
//
//    public Contact getContact() {
//        return contact;
//    }
//
//    public void setContact(Contact contact) {
//        this.contact = contact;
//    }
//
//    public Form getForm() {
//        return form;
//    }
//
//    public void setForm(Form form) {
//        this.form = form;
//    }
//
//    public Medium getMedium() {
//        return medium;
//    }
//
//    public void setMedium(Medium medium) {
//        this.medium = medium;
//    }
//
//    public boolean isTestDrive() {
//        return testDrive;
//    }
//
//    public void setTestDrive(boolean testDrive) {
//        this.testDrive = testDrive;
//    }
//
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
//
//    public String getJob() {
//        return job;
//    }
//
//    public void setJob(String job) {
//        this.job = job;
//    }
//
//    public Integer getEducation() {
//        return education;
//    }
//
//    public void setEducation(Integer education) {
//        this.education = education;
//    }
//
//    public Integer getExperience() {
//        return experience;
//    }
//
//    public void setExperience(Integer experience) {
//        this.experience = experience;
//    }
//
//    public Integer getAgeFrom() {
//        return ageFrom;
//    }
//
//    public void setAgeFrom(Integer ageFrom) {
//        this.ageFrom = ageFrom;
//    }
//
//    public Integer getAgeTo() {
//        return ageTo;
//    }
//
//    public void setAgeTo(Integer ageTo) {
//        this.ageTo = ageTo;
//    }
//
//    public Integer getGender() {
//        return gender;
//    }
//
//    public void setGender(Integer gender) {
//        this.gender = gender;
//    }
//
//    public Integer getDriverLicense() {
//        return driverLicense;
//    }
//
//    public void setDriverLicense(Integer driverLicense) {
//        this.driverLicense = driverLicense;
//    }
//
//    public List<LanguageSkill> getLanguageSkills() {
//        return languageSkills;
//    }
//
//    public void setLanguageSkills(List<LanguageSkill> languageSkills) {
//        this.languageSkills = languageSkills;
//    }
}
