/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package swinbank.server.policy;

/**
 *
 * @author James
 */
public class InvalidClientException extends RuntimeException {

    public InvalidClientException(Throwable cause) {
        super(cause);
    }

    public InvalidClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidClientException(String message) {
        super(message);
    }

    public InvalidClientException() {
    }
}

