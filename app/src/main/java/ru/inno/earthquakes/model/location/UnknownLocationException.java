package ru.inno.earthquakes.model.location;

/**
 * Thrown when we can't get location from a device
 *
 * @author Artur Badretdinov (Gaket)
 *         06.08.17
 */
public class UnknownLocationException extends RuntimeException {

    public UnknownLocationException(String message) {
        super(message);
    }
}
