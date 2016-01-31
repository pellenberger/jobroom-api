package ch.admin.seco.jobroom.model

import javax.persistence.*


// TODO: constraints like min/max should be (ONLY?) defined at SQL level!!!!
// TODO: ideally the same data-centric approach for more advanced constraints (e.g. publicationDate update)

@Entity data class JobPosition(
        
        @Id @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
        var id: Long? = null,

        val title: String,

        val description: String,

        val countryCode: String,

        val city: String,

        val zip: String, // TODO renamed as postalCode (zip is US and Philippines, like PLZ for DE,CH,AT,...)

        // FIXME: naming...
        val startImmediate: Boolean = false,

        val

        // FIXME the min constraint is silly (should be 0), but temporarily set to illustrate how validator annotations are (not) applied
        @ElementCollection //TODO check how we could override the table name...
        val languageSkill: Collection<LanguageSkill> = listOf()
) {
    // This private "default" constructor is only used by JPA layer
    private constructor() : this(null, "", "", "", "", "", false)
}

@Embeddable
data class LanguageSkill(
        val language: String,
        val spokenLevel: LanguageSkill.Level,
        val writtenLevel: LanguageSkill.Level
) {
    enum class Level {
        // FIXME: JPA layer by default stores the Enum Integer value, while we use the text representation in the API
        // FIXME: the current situation is not type-safe and error prone (any enum order change will affect the mapped integer values)
        //very_good("very_good"), good("good"), average("average")
        average, // 0
        good, // 1
        very_good  // 2
    }

    // This private "default" constructor is only used by JPA layer
    private constructor() : this("", Level.good, Level.good)
}


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


//    @NotNull
//    @Max(100)
//    @Min(0)
//    private Integer workloadFrom;
//
//    @Max(100)
//    @Min(0)
//    private Integer workloadTo;
//
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
//    }
//}
