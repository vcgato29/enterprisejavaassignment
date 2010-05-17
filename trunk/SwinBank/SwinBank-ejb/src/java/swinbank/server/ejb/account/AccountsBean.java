package swinbank.server.ejb.account;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import swinbank.server.ejb.login.LoginBean;
import swinbank.server.entity.Account;
import swinbank.server.entity.Login;
import swinbank.server.policy.AccessDeniedException;
import swinbank.server.policy.AccountType;
import swinbank.server.policy.ClientType;
import swinbank.server.policy.InvalidAccountException;
import swinbank.server.policy.InvalidClientException;
import swinbank.server.policy.InvalidDataException;
import swinbank.server.policy.SwinDatabase;
import swinbank.server.policy.SwinDatabase.UserAccount;
import swinbank.server.policy.SwinDatabase.User;

/**
 *
 * @author Daniel
 */
@Stateless
@Remote(AccountRemote.class)
public class AccountsBean implements AccountRemote {

    @PersistenceContext
    private EntityManager em;

    @Override
    public double getBalance(int accountId, int custId, ClientType clientType) throws AccessDeniedException {

        /*
         * Query query = em.createNamedQuery("Customer.findAll");
return List<Customer> query.getResultList();
         * */

       
        
        
        Account account = null;
        Login login = null;
        try
        {
           account  =getAccount(accountId);
        }
        catch(NoResultException e)
        {
            throw new AccessDeniedException("\nAccount does not Exist!");
        }
        try{
            login = getLogin(custId);
        }
        catch(NoResultException e)
        {
            throw new AccessDeniedException("User does not exist");
        }
        //check if the  account exisis

        if (clientType == ClientType.IB || clientType == ClientType.ATM) {
            //check if the customer owns the accounts
            if (isOwnAccount(account, login)) {
                return account.getBalance();
            } else {
                throw new AccessDeniedException("\nYou do not own of the Account!");
            }
        } else {//TM

            return account.getBalance();
        }

    }

    //current Customer
    public void createAccount(int custId, ClientType clientType, AccountType accountType) throws AccessDeniedException {

        Login login = getLogin(custId);
        if (clientType == ClientType.TM) {
            //check that the customer exist and isnt staff
            if (isCustomer(login)) {
                //Call the Create AccountsBean method in the database it will need to take the CumtomerID and the AccountType
                Account account = new Account();
                account.setAccounttype(accountType.getCharRepresentation());
                account.setBalance(0d);
                account.setCustid(custId);
                em.persist(account);
                
            } else {
                throw new InvalidAccountException("\nCan not create an account for that Customer!");
            }
        } else {
            throw new InvalidClientException("\nClient does not have enough priviliges to perform this action!");
        }
    }

//    //create an account for a new customer
//    public void createAccountNewCustomer(ClientType clientType, AccountType accountType) throws AccessDeniedException {
//
//        if (clientType == ClientType.TM) {
//            //Call the Create AccountsBean method in the database it will need to take the AccountType
//            SwinDatabase.CreateAccount(accountType);
//        } else {
//            throw new InvalidClientException("\nClient does not have enough priviliges to perform this action!");
//        }
//    }

    public void removeAccount(int accountId, ClientType clientType) throws AccessDeniedException {
        //check if its a TM
        if (clientType == ClientType.TM) {

            Account account = getAccount(accountId);
            //check if the  account exisis
            if (!doesAccountExist(account)) {
                throw new InvalidAccountException("\nAccount does not Exist!");
            }
            short isFalse = 0;
            account.setIsactive(isFalse);
            
        } else {
            throw new InvalidClientException("\nClient does not have enough priviliges to perform this action!");
        }

    }

    //returns the id of each accout in the list
    public List<String> getAccounts(int custID) throws AccessDeniedException {
        //check if customer exists

        Login login = getLogin(custID);
        if (checkCustomerID(login)) {
            //********************************************
            //   THIS MUST ONLY RETURN ACTIVE ACCOUNTS
            //********************************************
            return SwinDatabase.GetAccounts(custID);
        } else {
            throw new InvalidAccountException("\nCan not get the Accounts for that Customer");
        }
    }

    public List<String> getTransactions(int accountId, int custId, ClientType clientType, Date fromDate, Date toDate) throws AccessDeniedException {
        //check Data errors
        if (toDate.after(new Date())) {
            throw new InvalidDataException("\nTo Data can not be in the future");
        }
        if (fromDate.after(new Date())) {
            throw new InvalidDataException("\nFrom Data can not be in the future");
        }

        Login login = getLogin(custId);
        

        //check if the  account exisis
        if (!doesAccountExist(login)) {
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

    private Boolean isCustomer(Login login) {
         
        if (login == null) {
            return false;
        }
        if (login.isStaff()) {
            return false;
        }
        return true;
    }

    private boolean isOwnAccount(Account account, Login login) {
        if (account.getCustid() == login.getCustid())
        {
            return true;
        } else {
            return false;
        }
    }

    private boolean doesAccountExist(Account account) {
        
        //check if the account exisis
        if (account == null) {
            return false;
        }
        return true;
    }

    private Login getLogin(int userId)
    {
        Query userByLoginId = em.createNamedQuery("Login.findByCustid").setParameter("custid", userId);
        Login login = (Login)userByLoginId.getSingleResult();
        return login;
    }

    private Account getAccount(int accountId)
    {
         Query acountByIdQuery = em.createNamedQuery("Accounts.findByAccountid").setParameter("accountid", accountId);
         Account account = (Account)acountByIdQuery.getSingleResult();
         return account;
    }

    
}



