/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package swinbank.server.ejb.login;

import javax.ejb.Remote;
import java.rmi.RemoteException;
import swinbank.server.policy.ClientType;

/**
 *
 * @author Daniel
 */
@Remote
public interface LoginRemote extends java.rmi.Remote{

    boolean login(String userId, String password, ClientType clientType) throws RemoteException;
    
}
