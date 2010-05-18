package swinbank.server.ejb.account;

import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import swinbank.server.entity.Account;
import swinbank.server.entity.Transactions;
import swinbank.server.policy.AccountType;
import swinbank.server.policy.ClientType;

/**
 *
 * @author Daniel
 */
@Remote
public interface AccountRemote extends java.rmi.Remote{

    double getBalance(int accountId, int userId, ClientType clientType) throws RemoteException;
    void createAccount(int custID, ClientType clientType, AccountType accountType) throws RemoteException;
    void createAccountNewCustomer(ClientType clientType, AccountType accountType, String password) throws RemoteException;
    void removeAccount(int accountId, ClientType clientType) throws RemoteException;
    List<Account> getAccounts(int custId) throws RemoteException;
    List<Transactions> getTransactions(int accountId, int custId, ClientType clientType, Date fromDate, Date toDate) throws RemoteException;
}
