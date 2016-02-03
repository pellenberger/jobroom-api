package ch.admin.seco.jobroom.model

import javax.persistence.*


// TODO: constraints like min/max should be (ONLY?) defined at SQL level!!!!
// TODO: ideally the same data-centric approach for more advanced constraints (e.g. publicationDate update)

@Entity data class JobOffer(

        @Id @GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
        var id: Long? = null,

        @Version
        var version: Long? = null,

        val title: String,

        val description: String,

        // TODO add for the job location (... code reuse via some Address?)
        //val jobLocationCountryCode: String,
        //val jobLocationPpostalCode: String,
        //val jobLocationLocality: String, ("Arbeitsort")
        //val jobLocationAdditionalInformation: String?

        // FIXME: naming...
        val startImmediate: Boolean = false,

        @Embedded
        // TODO: consider @AttributeOverrides to deal with column prefixes ? (especially if we generalize the Address fields)
        val company: Company,

        // FIXME the min constraint is silly (should be 0), but temporarily set to illustrate how validator annotations are (not) applied
        @ElementCollection //TODO check how we could override the table name...
        val languageSkill: Collection<LanguageSkill> = listOf()
) {
    // This private "default" constructor is only used by JPA layer
    private constructor() : this(null, null, "", "", false, Company("", "", "", "", "", ""))
}

@Embeddable
data class Company(

        @Column(name="company_name")
        val name: String,

        @Column(name="company_country_code")
        val countryCode: String,

        @Column(name="company_street")
        val street: String,

        @Column(name="company_house_number")
        val houseNumber: String,

        @Column(name="company_locality")
        val locality: String,

        @Column(name="company_postal_code")
        val postalCode: String,

        val postboxNumber: String? = null,

        val postboxLocality: String? = null,

        val postboxPostalCode: String? = null
) {
    // This private "default" constructor is only used by JPA layer (and the JobPosition parent declaration)
    private constructor() : this("", "", "", "", "", "")
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
