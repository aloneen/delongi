package kz.seisen.delongi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class MachineUnavailableException extends RuntimeException {


    public MachineUnavailableException(String message) {
        super(message);

    }

}
