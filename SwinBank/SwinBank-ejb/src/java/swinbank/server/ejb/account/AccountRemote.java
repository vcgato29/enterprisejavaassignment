package swinbank.server.ejb.account;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import swinbank.server.policy.AccountType;
import swinbank.server.policy.ClientType;

/**
 *
 * @author Daniel
 */
@Remote
public interface AccountRemote extends java.rmi.Remote{

    double getBalance(String accountId, String userId, ClientType clientType) throws RemoteException;
    void createAccount(String custID, ClientType clientType, AccountType accountType) throws RemoteException;
    void createAccount(ClientType clientType, AccountType accountType) throws RemoteException;
    void removeAccount(String custID, String accountId, ClientType clientType) throws RemoteException;
    List<String> getAccounts(String custID) throws RemoteException;
    List<String> getTransactions(String accountId, String custID, ClientType clientType, Date fromDate, Date toDate) throws RemoteException;
}
