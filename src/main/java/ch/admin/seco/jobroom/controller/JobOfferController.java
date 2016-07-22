package ch.admin.seco.jobroom.controller;

import ch.admin.seco.jobroom.model.JobOffer;
import ch.admin.seco.jobroom.repository.JobOfferRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

@BasePathAwareController
public class JobOfferController {

    @Autowired
    JobOfferRepository jobOfferRepository;

    @RequestMapping(value = "/joboffers/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> preventsPut() {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

    @RequestMapping(value = "/joboffers/{id}/cancel", method = RequestMethod.POST)
    public ResponseEntity<?> cancelJoboffer(@PathVariable("id") int id, HttpServletRequest request) {

        JobOffer jobOffer = jobOfferRepository.findOne(id);

        // job offer doesn't exist or is owned by another user
        if (jobOffer == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }

        String cancellationReasonCode = request.getParameter("reasonCode");

        if (cancellationReasonCode == null || cancellationReasonCode.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Missing reasonCode");
        }

        //TODO add more validation

        jobOffer.setCancellationReasonCode(cancellationReasonCode);
        jobOffer.setCancellationDate(new Timestamp(DateTime.now().getMillis()));

        jobOfferRepository.save(jobOffer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
