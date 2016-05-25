package ch.admin.seco.jobroom.model

import javax.persistence.*

@Entity @Table(name = "API_ACCESSKEY") data class RestAccessKey (

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @field:Column(name = "ID")
        var id: Int? = null,

        @field:Column(name = "ACCESSKEY")
        val accessKey: String,

        @field:Column(name = "OWNER_EMAIL")
        val ownerEmail: String,

        @field:Column(name = "OWNER_NAME")
        var ownerName: String? = null,

        @field:Column(name = "ACTIVE")
        var active: Int? = null
) {
        private constructor() : this(null, "", "", null, null)
        constructor(accessKey: String, ownerEmail: String, ownerName: String, active: Int) : this(null, accessKey, ownerEmail, ownerName, active)
}