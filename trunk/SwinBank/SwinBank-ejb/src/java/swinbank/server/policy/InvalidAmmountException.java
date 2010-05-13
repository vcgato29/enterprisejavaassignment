/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package swinbank.server.policy;

/**
 *
 * @author James
 */
public class InvalidAmmountException extends RuntimeException {

    public InvalidAmmountException(Throwable cause) {
        super(cause);
    }

    public InvalidAmmountException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAmmountException(String message) {
        super(message);
    }

    public InvalidAmmountException() {
    }
}

