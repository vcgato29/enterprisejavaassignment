/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package swinbank.server.policy;

/**
 *
 * @author James
 */
public class InvalidFundsException extends RuntimeException {

    public InvalidFundsException(Throwable cause) {
        super(cause);
    }

    public InvalidFundsException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFundsException(String message) {
        super(message);
    }

    public InvalidFundsException() {
    }
}

