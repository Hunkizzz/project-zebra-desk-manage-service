package team.projectzebra.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Proudly created by dgonyak on 09/09/2019.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class ValidationFailedException extends Exception {
    private static final long serialVersionUID = 2L;

    public ValidationFailedException(String message) {
        super(message);
    }
}
