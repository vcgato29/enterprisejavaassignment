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

    void billPayment(int custId, int accountId, int billerId, ClientType clientType, Double amount, String description) throws RemoteException;
    void moneyTransfer(int custId, int toAccountId, int fromAccountId, ClientType clientType, Double amount, String description) throws RemoteException;
    void withdrawal(int custId, int accountId, ClientType clientType, Double amount, String description) throws RemoteException;
    void deposit(int accountId, ClientType clientType, Double amount, String description) throws RemoteException;

}
