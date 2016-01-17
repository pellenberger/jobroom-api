package ch.admin.seco.jobroom.web

import ch.admin.seco.jobroom.dto.JobPosition
import ch.admin.seco.jobroom.dto.LanguageSkill
import ch.admin.seco.jobroom.dto.RegisteredJobPosition
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import kotlin.collections.listOf

@Controller
@RequestMapping(value = "/job")
class JobPositionController {

    companion object {
        public const val HEADER_ACCESS_KEY = "accessKey"
    }

    @RequestMapping(method = arrayOf(RequestMethod.POST))
    //@Throws(IOException::class)
    fun add(@Valid @RequestBody job: JobPosition, request: HttpServletRequest): ResponseEntity<*> {
        // TODO: missing the owner identifier? (allow to change its key...)
        val accessKey = request.getHeader(HEADER_ACCESS_KEY)

        if (checkAccess(accessKey)) {

            // TODO ensure that all the input validation is instrumented and applied upstream via Spring MVC (see @Valid)
            // HttpStatus.BAD_REQUEST responses must be handled in a generic/framework approach

            return storeJobOffer(job, accessKey)
        }

        // FIXME use a a generic/framework class for the returned error message (exception style: user level, developer level,...)
        return ResponseEntity("Oops!", HttpStatus.UNAUTHORIZED)
    }

    // Dumb function, just here to get a value on GET /job
    @RequestMapping(method = arrayOf(RequestMethod.GET))
    fun get(): ResponseEntity<*> {
        val job = JobPosition(
                title = "Software engineer",
                description = "bla, bla,...",
                countryCode = "CH",
                city = "Bern",
                zip = "3002",
                startImmediate = true,
                languageSkills = listOf(LanguageSkill(1, 1, 1)))

        return ResponseEntity(RegisteredJobPosition(12345, job), HttpStatus.OK)
    }

    private fun storeJobOffer(job: JobPosition, accessKey: String): ResponseEntity<*> {
        return ResponseEntity(RegisteredJobPosition(99999, job), HttpStatus.CREATED)
    }

    private fun checkAccess(accessKey: String): Boolean {
        return true
    }

}
