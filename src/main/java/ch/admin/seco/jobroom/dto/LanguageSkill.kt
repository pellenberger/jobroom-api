package ch.admin.seco.jobroom.dto

import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class LanguageSkill(
    @Min(1) @Max(40) val id: Int, //FIXME --> id (iso name instead...? deal with AVAM mapping behind the scene...)
    @Min(1) @Max(3) val spokenLevel: Int, //FIXME use a type-safe enumeration instead
    @Min(1) @Max(3) val writtenLevel: Int //FIXME same as above
)

