package ch.admin.seco.jobroom.model

import org.joda.time.LocalDate
import javax.persistence.*

@Embeddable
data class Job(

        val title: String,

        @Lob
        @field:javax.validation.constraints.Size(max = 10000)
        val description: String,

        val workingTimePercentageFrom: Int,
        val workingTimePercentageTo: Int,
        var startDate: LocalDate?,
        var endDate: LocalDate?,

        @Embedded
        val location: Location,

        @field:javax.validation.constraints.Size(max=5)
        @ElementCollection(fetch = javax.persistence.FetchType.EAGER)
        var languageSkills: Collection<LanguageSkill>?

) {
    // This default constructor is required by JPA
    constructor() : this("", "", 0, 0, null, null, Location(), null)
}

@Embeddable
data class LanguageSkill(

        // Hibernate bug : @ElementCollection is incompatible with DefaultComponentSafeNamingStrategy property
        // Need to explicitly define column names
        @Column(name="language")
        val language: String,

        @Column(name="spokenlevel")
        @Enumerated(javax.persistence.EnumType.STRING)
        val spokenLevel: LanguageSkill.Level,

        @Column(name="writtenlevel")
        @Enumerated(javax.persistence.EnumType.STRING)
        val writtenLevel: LanguageSkill.Level
) {
    enum class Level {
        no_knowledge,
        basic_knowledge,
        good,
        very_good
    }

    // This default constructor is required by JPA
    constructor() : this("", Level.good, Level.good)
}

@Embeddable
data class Location(

        val countryCode: String,
        var locality: String?,
        var postalCode: String?,
        var additionalDetails: String?
) {
    // This default constructor is required by JPA
    constructor() : this("", null, null, null)
}

@Embeddable
data class Company (

        val name: String,
        val countryCode: String,
        val street: String,
        val houseNumber: String,
        val locality: String,
        val postalCode: String,
        var phoneNumber: String?,
        var email: String?,
        var website: String?,

        @Embedded
        var postbox: Postbox?
){
    // This default constructor is required by JPA
    constructor() : this("", "", "", "", "", "", null, null, null, null)
}


@Embeddable
data class Postbox(

        var number: String?,
        var locality: String?,
        var postalCode: String?
) {
    // This default constructor is required by JPA
    constructor() : this(null, null, null)
}


@Embeddable
data class Contact(

        @Enumerated(javax.persistence.EnumType.STRING)
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

    // This default constructor is required by JPA
    constructor() : this(Title.mister, "", "", "", "")
}

@Embeddable
data class Application(

        val telephonic: Boolean,
        val written: Boolean,
        val electronic: Boolean,
        var additionalDetails: String?,
        var url: String?
) {
    // This default constructor is required by JPA
    constructor() : this(false, false, false, null, null)
}
