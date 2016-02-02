package ch.admin.seco.jobroom.web;

import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@BasePathAwareController
public class JobPositionController {

    @RequestMapping(value = "/jobPositions/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> preventsPut() {
        return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
    }

}
