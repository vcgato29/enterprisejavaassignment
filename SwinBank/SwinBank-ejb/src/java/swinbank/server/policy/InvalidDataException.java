/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package swinbank.server.policy;

/**
 *
 * @author James
 */
public class InvalidDataException extends RuntimeException {

    public InvalidDataException(Throwable cause) {
        super(cause);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException() {
    }
}

