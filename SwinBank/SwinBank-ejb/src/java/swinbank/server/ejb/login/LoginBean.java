package swinbank.server.ejb.login;

import java.rmi.RemoteException;
import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.rmi.PortableRemoteObject;
import swinbank.server.entity.Account;
import swinbank.server.entity.Login;
import swinbank.server.policy.AccessDeniedException;
import swinbank.server.policy.ClientType;

/**
 *
 * @author Daniel
 */
@Stateless
@Remote(LoginRemote.class)
public class LoginBean extends PortableRemoteObject implements LoginRemote {

    @PersistenceContext
    private EntityManager em;

    public LoginBean() throws RemoteException {
    }

    public boolean login(int custId, String password, ClientType clientType) throws RemoteException {
        Login login = null;

                System.out.println("1");
        //check if the  login exisis
        try {

                System.out.println("2");
            login = getLogin(custId);
        } catch (NoResultException e) {
            throw new AccessDeniedException("\nUser does not exist");
        }

                System.out.println("3");
        if (login.getPassword().equals(password)) {

                System.out.println("4");
            if (clientType == ClientType.TM) {

                System.out.println("5");
                if (login.getIsstaff() == 1) {

                System.out.println("6");
                    return true;
                } else {

                System.out.println("7");
                    return false;
                }
            } else {

                System.out.println("8");
                //check if they have accounts
                Query countQuery = em.createNamedQuery("Account.accountsCountActive").setParameter("custid", custId);
                System.out.println("9");
                List<Account> count = countQuery.getResultList();
                System.out.println("test");
                if (count.size() > 0) {
                    if (login.getIsstaff() == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }


        return false;

    }

    private Login getLogin(int userId) {
        System.out.println("login 1");
        
        Query userByLoginId = em.createNamedQuery("Login.findByCustid").setParameter("custid", userId);
        System.out.println("login 2");
        Login login = (Login) userByLoginId.getSingleResult();
        System.out.println("login 3");
        return login;
    }
}
