package swinbank.server.ejb.transaction;

import javax.ejb.Remote;
import java.rmi.RemoteException;
import swinbank.server.policy.ClientType;

/**
 *
 * @author James
 */
@Remote
public interface TransactionRemote extends java.rmi.Remote {

    void billPayment(String custId, String accountId, String billerId, ClientType clientType, Double amount, String description) throws RemoteException;
    void moneyTransfer(String custId, String toAccountId, String fromAccountId, ClientType clientType, Double amount, String description) throws RemoteException;
    void withdrawal(String custId, String accountId, ClientType clientType, Double amount, String description) throws RemoteException;
    void deposit(String accountId, ClientType clientType, Double amount, String description) throws RemoteException;

}
