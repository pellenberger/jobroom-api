package ch.admin.seco.jobroom.model

import javax.persistence.*

@Entity @Table(name = "AOSTE_ACCESSKEYS") data class RestAccessKey (

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @field:Column(name = "ID")
        var id: Int? = null,

        @field:Column(name = "ACCESSKEY")
        val accessKey: String,

        @field:Column(name = "OWNER")
        val keyOwner: String,

        @field:Column(name = "ACTIVE_B")
        var active: Int? = null
) {
        private constructor() : this(null, "", "", null)
        constructor(accessKey: String, keyOwner: String, active: Int) : this(null, accessKey, keyOwner, active)
}