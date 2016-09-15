package ch.admin.seco.jobroom.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.joda.time.DateTime
import org.joda.time.LocalDate
import java.sql.Timestamp
import javax.persistence.*

@Entity data class JobOffer(

        @Id
        @GeneratedValue(generator="joboffer_gen")
        @SequenceGenerator(name="joboffer_gen", sequenceName="aoste_seq")
        var id: Int?,

        @Version
        var version: Int?,

        val publicationStartDate: LocalDate,

        var publicationEndDate: LocalDate?,

        var reference: String?,

        var url: String?,

        @Embedded
        @get:javax.validation.Valid
        val job: Job,

        @Embedded
        val company: Company,

        @Embedded
        val contact: Contact,

        @Embedded
        val application: Application,

        @ManyToOne
        @JoinColumn(name="owner_id")
        @get:JsonIgnore
        var owner: RestAccessKey?,

        @get:JsonIgnore
        var creationDate: Timestamp?,

        @get:JsonIgnore
        var lastModificationDate: Timestamp?,

        @get:JsonIgnore
        var cancellationDate: Timestamp?,

        @get:JsonIgnore
        var cancellationReasonCode: String?
) {
    // This default constructor is required by JPA
    constructor() : this(null,  null, LocalDate.now(), null, null, null, Job(), Company(), Contact(), Application(), null, null, null, null, null)

    @PrePersist
    fun onCreate() {
        creationDate = Timestamp(DateTime.now().millis)
    }

    @PreUpdate
    fun onUpdate() {
        lastModificationDate = Timestamp(DateTime.now().millis)
    }
}

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
        @ElementCollection(fetch = FetchType.EAGER)
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
        @Enumerated(EnumType.STRING)
        val spokenLevel: LanguageSkill.Level,

        @Column(name="writtenlevel")
        @Enumerated(EnumType.STRING)
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
