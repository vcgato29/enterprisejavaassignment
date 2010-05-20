package swinbank.server.ejb.account;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import swinbank.server.entity.Account;
import swinbank.server.entity.Login;
import swinbank.server.entity.Transactions;
import swinbank.server.policy.AccessDeniedException;
import swinbank.server.policy.AccountType;
import swinbank.server.policy.ClientType;
import swinbank.server.policy.InvalidAccountException;
import swinbank.server.policy.InvalidClientException;
import swinbank.server.policy.InvalidDataException;

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

        //check if the  account exisis
        try {
            account = getAccount(accountId);
        } catch (NoResultException e) {
            throw new AccessDeniedException("\nAccount does not Exist!");
        }

        //check if the  login exisis
        try {
            login = getLogin(custId);
        } catch (NoResultException e) {
            throw new AccessDeniedException("\nUser does not exist");
        }

        if (clientType == ClientType.IB || clientType == ClientType.ATM) {
            //check if the customer owns the accounts
            System.out.println(account.getCustid());
            System.out.println(login.getCustid());
            if (account.getCustid().equals(login.getCustid())) {
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
        Login login = null;
        //check if the  login exisis
        try {
            login = getLogin(custId);
        } catch (NoResultException e) {
            throw new AccessDeniedException("\nUser does not exist");
        }
        if (clientType == ClientType.TM) {
            //check that the customer exist and isnt staff
            if (login.getIsstaff() == 1) {
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

    //create an account for a new customer
    public void createAccountNewCustomer(ClientType clientType, AccountType accountType, String password) throws AccessDeniedException {

        if (clientType == ClientType.TM) {
            Login login = new Login();
            login.setPassword(password);
            short isFalse = 0;
            login.setIsstaff(isFalse);
            em.persist(login);
            Account account = new Account();
            account.setAccounttype(accountType.getCharRepresentation());
            account.setBalance(0d);
            account.setCustid(login.getCustid());
            em.persist(account);
        } else {// ATM || IB
            throw new InvalidClientException("\nClient does not have enough priviliges to perform this action!");
        }
    }

    public void removeAccount(int accountId, ClientType clientType) throws AccessDeniedException {
        //check if its a TM
        if (clientType == ClientType.TM) {

            Account account = null;

            //check if the  account exisis
            try {
                account = getAccount(accountId);
            } catch (NoResultException e) {
                throw new AccessDeniedException("\nAccount does not Exist!");
            }
            short isFalse = 0;
            account.setIsactive(isFalse);

        } else {
            throw new InvalidClientException("\nClient does not have enough priviliges to perform this action!");
        }

    }

    //returns the id of each accout in the list
    public List<Account> getAccounts(int custID) throws AccessDeniedException {
        //check if customer exists

        Login login = null;
        //check if the  account exisis
        try {
            login = getLogin(custID);
        } catch (NoResultException e) {
            throw new AccessDeniedException("\nUser does not exist");
        }
        if (login.getIsstaff() == 1) {
            throw new AccessDeniedException("\nStaff can not have any accounts");
        }
        short isTrue = 1;
        List<Account> accountList = em.createNamedQuery("Account.findByCustidAndIsActive").setParameter("custid", custID).setParameter("isactive", isTrue).getResultList();
        return accountList;
    }

    public List<Transactions> getTransactions(int accountId, int custId, ClientType clientType, Date fromDate, Date toDate) throws AccessDeniedException {
        //check Data errors
        if (toDate.after(new Date())) {
            throw new InvalidDataException("\nTo Data can not be in the future");
        }
        if (fromDate.after(new Date())) {
            throw new InvalidDataException("\nFrom Data can not be in the future");
        }

        Account account = null;
        Login login = null;

        //check if the  account exisis
        try {
            account = getAccount(accountId);
        } catch (NoResultException e) {
            throw new AccessDeniedException("\nAccount does not Exist!");
        }

        //check if the  login exisis
        try {
            login = getLogin(custId);
        } catch (NoResultException e) {
            throw new AccessDeniedException("\nUser does not exist");
        }

        if (clientType == ClientType.IB || clientType == ClientType.ATM) {
            //check if the customer owns the accounts
            if (account.getCustid() == login.getCustid()) {
                List<Transactions> transcationList = em.createNamedQuery("Transactions.findTransactionList").setParameter("account", account.getAccountid()).setParameter("todate", toDate).setParameter("fromdate", fromDate).getResultList();
                return transcationList;
            } else {
                throw new AccessDeniedException("\nYou do not own of the Account!");
            }
        } else {//TM
            List<Transactions> transcationList = em.createNamedQuery("Transactions.findTransactionList").setParameter("account", account.getAccountid()).setParameter("todate", toDate).setParameter("fromdate", fromDate).getResultList();
            return transcationList;
        }
    }

    private Login getLogin(int userId) {
        Query userByLoginId = em.createNamedQuery("Login.findByCustid").setParameter("custid", userId);
        Login login = (Login) userByLoginId.getSingleResult();
        return login;
    }

    private Account getAccount(int accountId) {
        Query acountByIdQuery = em.createNamedQuery("Account.findByAccountid").setParameter("accountid", accountId);
        Account account = (Account) acountByIdQuery.getSingleResult();
        return account;
    }
}



