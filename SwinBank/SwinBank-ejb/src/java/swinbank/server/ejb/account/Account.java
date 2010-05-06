package swinbank.server.ejb.account;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import swinbank.server.policy.AccessDeniedException;
import swinbank.server.policy.AccountType;
import swinbank.server.policy.ClientType;
import swinbank.server.policy.SwinDatabase;
import swinbank.server.policy.SwinDatabase.UserAccount;
import swinbank.server.policy.SwinDatabase.User;

/**
 *
 * @author Daniel
 */
@Stateless
@Remote(AccountRemote.class)
public class Account implements AccountRemote {

    public double getBalance(String accountId, String custId, ClientType clientType) throws AccessDeniedException {

        //check if the  account exisis
        if (!accountExists(accountId)) {
            throw new AccessDeniedException("\nAccount does not Exist!");
        }

        if (clientType == ClientType.IB || clientType == ClientType.ATM) {
            //check if the customer owns the accounts
            if (ownAccount(accountId, custId)) {
                return SwinDatabase.account(accountId).balance;
            } else {
                throw new AccessDeniedException("\nYou do not own of the Account!");
            }
        } else {//TM

            return SwinDatabase.account(accountId).balance;
        }

    }

    //current Customer
    public void createAccount(String custId, ClientType clientType, AccountType accountType) throws AccessDeniedException {
        if (clientType == ClientType.TM) {
            //check that the customer exist and isnt staff
            if (checkCustomerID(custId)) {
                //Call the Create Account method in the database it will need to take the CumtomerID and the AccountType
                SwinDatabase.createAccount(custId, accountType);
            } else {
                throw new AccessDeniedException("\nCan not create an account for that Customer!");
            }
        } else {
            throw new AccessDeniedException("\nClient does not have enough priviliges to perform this action!");
        }
    }

    //create an account for a new customer
    public void createAccountNewCustomer(ClientType clientType, AccountType accountType) throws AccessDeniedException {

        if (clientType == ClientType.TM) {
            //Call the Create Account method in the database it will need to take the AccountType
            SwinDatabase.CreateAccount(accountType);
        } else {
            throw new AccessDeniedException("\nClient does not have enough priviliges to perform this action!");
        }
    }

    public void removeAccount(String accountId, ClientType clientType) throws AccessDeniedException {
        //check if its a TM
        if (clientType == ClientType.TM) {
            //check if the  account exisis
            if (!accountExists(accountId)) {
                throw new AccessDeniedException("\nAccount does not Exist!");
            }
            SwinDatabase.SetAccountInactive(accountId);
        } else {
            throw new AccessDeniedException("\nClient does not have enough priviliges to perform this action!");
        }

    }

    //returns the id of each accout in the list
    public List<String> getAccounts(String custID) throws AccessDeniedException {
        //check if customer exists
        if (checkCustomerID(custID)) {
            return SwinDatabase.GetAccounts(custID);
        } else {
            throw new AccessDeniedException("\nCan not get the Accounts for that Customer");
        }
    }

    public List<String> getTransactions(String accountId, String custId, ClientType clientType, Date fromDate, Date toDate) throws AccessDeniedException {
        //check Data errors
        if (toDate.after(new Date())) {
            throw new AccessDeniedException("\nTo Data can not be in the future");
        }
        if (fromDate.after(new Date())) {
            throw new AccessDeniedException("\nFrom Data can not be in the future");
        }

        //check if the  account exisis
        if (!accountExists(accountId)) {
            throw new AccessDeniedException("\nAccount does not Exist!");
        }

        if (clientType == ClientType.IB || clientType == ClientType.ATM) {
            //check if the customer owns the accounts
            if (ownAccount(accountId, custId)) {
                return SwinDatabase.GetTransactions(accountId, fromDate, toDate);
            } else {
                throw new AccessDeniedException("\nYou do not own of the Account!");
            }
        } else {//TM
            return SwinDatabase.GetTransactions(accountId, fromDate, toDate);
        }
    }

    private Boolean checkCustomerID(String custID) {
        User user = new SwinDatabase().getUser(custID);
        if (user == null) {
            return false;
        }
        if (user.isStaff) {
            return false;
        }
        return true;
    }

    private boolean ownAccount(String accountId, String custId) {
        UserAccount acc = SwinDatabase.getAccount(accountId);
        if (acc.userId.equals(custId)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean accountExists(String accountId) {
        UserAccount acc = SwinDatabase.getAccount(accountId);
        //check if the account exisis
        if (acc == null) {
            return false;
        }
        return true;
    }
}



