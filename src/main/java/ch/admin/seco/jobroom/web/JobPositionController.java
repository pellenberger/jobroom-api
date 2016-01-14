package ch.admin.seco.jobroom.web;

import ch.admin.seco.jobroom.dto.JobPosition;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@RequestMapping(value = "/job")
public class JobPositionController {

    private static final String HEADER_ACCESS_KEY = "accessKey";

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> add(@RequestBody JobPosition job, HttpServletRequest request) throws IOException {
        String accessKey = request.getHeader(HEADER_ACCESS_KEY);

        // Dumb test, just here to force a value on a field
        if (job.getTitle() == null) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        if (job.getTitle().isEmpty()) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        if (checkAccess(accessKey, null)) {
            return storeJobOffer(job, accessKey);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    // Dumb function, just here to get a value on GET /job
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> get() {
        JobPosition job = new JobPosition();
        job.setTitle("Software engineer");
        job.setCity("Berne");
        job.setCountryCode("CH");
        job.setZip("3002");
        job.setStartImmediate(true);
        return new ResponseEntity<>(job, HttpStatus.OK);
    }

    private ResponseEntity<?> storeJobOffer(JobPosition job, String accessKey) throws JsonProcessingException {
        return new ResponseEntity<>(job, HttpStatus.CREATED);
    }

    private boolean checkAccess(String accessKey, Object o) {
        return true;
    }
}
