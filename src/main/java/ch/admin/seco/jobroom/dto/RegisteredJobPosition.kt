package ch.admin.seco.jobroom.dto

// FIXME consider composition/mixin style instead of inheritance ?
class RegisteredJobPosition(

        val id: Long,

        // TODO: not necessary to keep this reference!
        private val job: JobPosition

    ) : JobPosition(

        title = job.title,
        description = job.description,
        countryCode = job.countryCode,
        city = job.city,
        zip = job.zip,
        startImmediate = job.startImmediate,
        languageSkills = job.languageSkills

    )

//    val links: Set<SelfLink> = HashSet()
//
//    var state: String? = null

