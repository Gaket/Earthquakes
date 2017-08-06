package ru.inno.earthquakes.model.location;

/**
 * @author Artur Badretdinov (Gaket)
 *         06.08.17
 */
public class UnknownLocationException extends RuntimeException {

    public UnknownLocationException(String message) {
        super(message);
    }
}
