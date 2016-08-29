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
        var id: Int? = null,

        @Version
        var version: Int? = null,

        val publicationStartDate: LocalDate,

        var publicationEndDate: LocalDate? = null,

        var reference: String? = "",

        var url: String? = "",

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
        @JsonIgnore
        var owner: RestAccessKey? = null,

        @JsonIgnore
        var creationDate: Timestamp? = null,

        @JsonIgnore
        var lastModificationDate: Timestamp? = null,

        @JsonIgnore
        var cancellationDate: Timestamp? = null,

        @JsonIgnore
        var cancellationReasonCode: String? = null
) {
    // This private "default" constructor is only used by JPA layer
    private constructor() : this(null,  null, LocalDate.now(), null, "", "", Job(), Company(), Contact(), Application(), null, null, null, null, null)

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
        var startDate: LocalDate? = null,
        var endDate: LocalDate? = null,

        @Embedded
        val location: Location,

        @field:javax.validation.constraints.Size(max=5)
        @ElementCollection(fetch = FetchType.EAGER)
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
        no_knowledge,
        basic_knowledge,
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
        var additionalDetails: String? = ""
) {
    // This "default" constructor is only used by JPA layer
    constructor() : this("", "", "", "");
}

@Embeddable
data class Company (

        val name: String,
        val countryCode: String,
        val street: String,
        val houseNumber: String,
        val locality: String,
        val postalCode: String,
        var phoneNumber: String? = "",
        var email: String? = "",
        var website: String? = "",

        @Embedded
        val postbox: Postbox
){
    // This "default" constructor is only used by JPA layer
    constructor() : this("", "", "", "", "", "", "", "", "", Postbox())
}


@Embeddable
data class Postbox(

        var number: String? = "",
        var locality: String? = "",
        var postalCode: String? = ""
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
        val electronic: Boolean,
        var additionalDetails: String? = "",
        var url: String? = ""
) {
    // This private "default" constructor is only used by JPA layer
    constructor() : this(false, false, false, "", "")
}
