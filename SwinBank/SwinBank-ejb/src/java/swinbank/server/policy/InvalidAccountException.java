/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package swinbank.server.policy;

/**
 *
 * @author James
 */
public class InvalidAccountException extends RuntimeException {

    public InvalidAccountException(Throwable cause) {
        super(cause);
    }

    public InvalidAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidAccountException(String message) {
        super(message);
    }

    public InvalidAccountException() {
    }
}

