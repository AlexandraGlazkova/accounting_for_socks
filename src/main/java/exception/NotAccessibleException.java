package exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotAccessibleException extends IllegalArgumentException {
    public NotAccessibleException(String message) {
        super(message);
        log.error(message);
    }
}
