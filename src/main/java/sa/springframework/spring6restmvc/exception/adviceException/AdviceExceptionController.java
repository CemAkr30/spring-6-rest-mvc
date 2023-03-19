package sa.springframework.spring6restmvc.exception.adviceException;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import sa.springframework.spring6restmvc.exception.NotFoundException;

@ControllerAdvice
public class AdviceExceptionController {

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Resource Not Found")
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity notValid() {
        return ResponseEntity.ok("Hibernate Validation");
    }
}
