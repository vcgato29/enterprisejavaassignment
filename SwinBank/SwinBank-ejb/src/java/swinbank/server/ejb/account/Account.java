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

    public double getBalance(String accountId, String custID, ClientType clientType) throws AccessDeniedException {

        //Call made to static method getbalance()
        UserAccount acc = SwinDatabase.getAccount(accountId);
        //need to check if the account is active
        if (acc == null) {
            throw new AccessDeniedException("\nAccount does not Exist!");
        }

        if (acc.userId.equals(custID)) {
            return acc.balance;
        }

        if (clientType == ClientType.TM) {
            return acc.balance;
        } else {
            throw new AccessDeniedException("\nClient does not have enough priviliges to perform this action!");
        }

    }

    //current Customer
    public void createAccount(String custID, ClientType clientType, AccountType accountType) throws AccessDeniedException {
        if (clientType == ClientType.TM) {
            //check that the customer exist and isnt staff
            if (CheckCustomerID(custID)) {
                //Call the Create Account method in the database it will need to take the CumtomerID and the AccountType
                SwinDatabase.CreateAccount(custID, accountType);
            } else {
                throw new AccessDeniedException("\nCan not create an account for that Customer!");
            }
        } else {
            throw new AccessDeniedException("\nClient does not have enough priviliges to perform this action!");
        }
    }

    //create an account for a new customer
    public void createAccount(ClientType clientType, AccountType accountType) throws AccessDeniedException {

        if (clientType == ClientType.TM) {
            //Call the Create Account method in the database it will need to take the AccountType
            SwinDatabase.CreateAccount(accountType);
        } else {
            throw new AccessDeniedException("\nClient does not have enough priviliges to perform this action!");
        }
    }

    public void removeAccount(String custID, String accountId, ClientType clientType) throws AccessDeniedException {
        //check if its a TM
        if (clientType == ClientType.TM) {

            UserAccount acc = SwinDatabase.getAccount(accountId);

            //check if the account exisis
            if (acc == null) {
                throw new AccessDeniedException("\nAccount does not Exist!");
            }

            //check that the customer exist and isnt staff
            if (CheckCustomerID(custID)) {
                //check if the customer has any accounts
                if (getAccounts(custID).size() == 1) {
                    SwinDatabase.SetAccountInactive(accountId);
                    SwinDatabase.SetCustomerInactive(custID);
                } else {
                    SwinDatabase.SetAccountInactive(accountId);
                }


            } else {
                throw new AccessDeniedException("\nCan not remove a Account from that Customer");
            }
        } else {
            throw new AccessDeniedException("\nClient does not have enough priviliges to perform this action!");
        }

    }

    //returns the id of each accout in the list
    public List<String> getAccounts(String custID) throws AccessDeniedException {
        if (CheckCustomerID(custID)) {
            return SwinDatabase.GetAccounts(custID);
        } else {
            throw new AccessDeniedException("\nCan not get the Accounts for that Customer");
        }
    }

    public List<String> getTransactions(String accountId, String custID, ClientType clientType, Date fromDate, Date toDate) throws AccessDeniedException {

        //check Data errors
        if (toDate.after(new Date())) {
            throw new AccessDeniedException("\nTo Data can not be in the future");
        }
        if (fromDate.after(new Date())) {
            throw new AccessDeniedException("\nFrom Data can not be in the future");
        }


        //Call made to static method getbalance()
        UserAccount acc = SwinDatabase.getAccount(accountId);
        //need to check if the account is active

        //check if the account exisis
        if (acc == null) {
            throw new AccessDeniedException("\nAccount does not Exist!");
        }

        //check that the account is owned by the customer
        if (acc.userId.equals(custID)) {
            return SwinDatabase.GetTransactions(accountId, fromDate, toDate);
        }

        if (clientType == ClientType.TM) {
            return SwinDatabase.GetTransactions(accountId, fromDate, toDate);
        } else {
            throw new AccessDeniedException("\nClient does not have enough priviliges to perform this action!");
        }
    }

    private Boolean CheckCustomerID(String custID) {
        User user = new SwinDatabase().getUser(custID);
        if (user == null) {
            return false;
        }
        if (user.isStaff) {
            return false;
        }
        return true;
    }
}



