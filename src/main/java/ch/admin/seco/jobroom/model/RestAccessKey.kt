package ch.admin.seco.jobroom.model

import javax.persistence.*

@Entity @Table(name = "API_ACCESSKEY") data class RestAccessKey (

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @field:Column(name = "ID")
        var id: Int? = null,

        @field:Column(name = "ACCESSKEY")
        val accessKey: String,

        @field:javax.persistence.Column(name = "OWNER_NAME")
        var ownerName: String,

        @field:Column(name = "OWNER_EMAIL")
        val ownerEmail: String? = null,

        @field:Column(name = "ACTIVE")
        var active: Int? = null
) {
        private constructor() : this(null, "", "", null, null)
        constructor(accessKey: String, ownerName: String, ownerEmail: String, active: Int) : this(null, accessKey, ownerName, ownerEmail, active)
}