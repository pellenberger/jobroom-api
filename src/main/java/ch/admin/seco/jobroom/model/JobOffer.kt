package ch.admin.seco.jobroom.model

import java.sql.Date
import java.util.*
import javax.persistence.*

// TODO: constraints like min/max should be (ONLY?) defined at SQL level!!!!
// TODO: ideally the same data-centric approach for more advanced constraints (e.g. publicationDate update)

@Entity data class JobOffer(

        @Id @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
        var id: Long? = null,

        @Version
        var version: Long? = null,

        val publicationStartDate: Date,

        var publicationEndDate: Date? = null,

        @Embedded
        val job: Job,

        @Embedded
        val company: Company,

        @Embedded
        val contact: Contact,

        @Embedded
        val application: Application
) {
    // This private "default" constructor is only used by JPA layer
    private constructor() : this(null,  null, Date(Calendar.getInstance().getTime().time), null, Job(), Company(), Contact(), Application())
}

@Embeddable
data class Job(

        val title: String,
        val description: String,
        val workingTimePercentageFrom: Int,
        val workingTimePercentageTo: Int,
        var startDate: Date? = null,
        var endDate: Date? = null,

        @Embedded
        val location: Location,

        @ElementCollection
        val languageSkills: Collection<LanguageSkill>

) {
    // This "default" constructor is only used by JPA layer
    constructor() : this("", "", 0, 0, null, null, Location(), listOf())
}

@Embeddable
data class LanguageSkill(

        // Hibernate bug : @ElementCollection is incompatible with DefaultComponentSafeNamingStrategy property
        // Need to explicitly define column names
        @Column(name="language")
        val language: String,

        @Column(name="spokenlevel")
        @Enumerated(EnumType.STRING)
        val spokenLevel: LanguageSkill.Level,

        @Column(name="writtenlevel")
        @Enumerated(EnumType.STRING)
        val writtenLevel: LanguageSkill.Level
) {
    enum class Level {
        average,
        good,
        very_good
    }

    // This "default" constructor is only used by JPA layer
    constructor() : this("", Level.good, Level.good)
}

@Embeddable
data class Location(

        val countryCode: String,
        val locality: String,
        val postalCode: String,
        var additionalDetails: String? = null
) {
    // This "default" constructor is only used by JPA layer
    constructor() : this("", "", "", null);
}

@Embeddable
data class Company (

        val name: String,
        val countryCode: String,
        val street: String,
        val houseNumber: String,
        val locality: String,
        val postalCode: String,
        val phoneNumber: String,
        val email: String,
        val website: String,

        @Embedded
        val postbox: Postbox
){
    // This "default" constructor is only used by JPA layer
    constructor() : this("", "", "", "", "", "", "", "", "", Postbox())
}


@Embeddable
data class Postbox(

        val number: String,
        val locality: String,
        val postalCode: String
) {
    // This private "default" constructor is only used by JPA layer
    constructor() : this("", "", "")
}


@Embeddable
data class Contact(

        @Enumerated(EnumType.STRING)
        val title: Contact.Title,

        val firstName: String,
        val lastName: String,
        val phoneNumber: String,
        val email: String
) {
    enum class Title {
        mister,
        madam
    }

    // This private "default" constructor is only used by JPA layer
    constructor() : this(Title.mister, "", "", "", "")
}

@Embeddable
data class Application(

        val telephonic: Boolean,
        val written: Boolean,
        val electronic: Boolean
) {
    // This private "default" constructor is only used by JPA layer
    constructor() : this(false, false, false)
}
