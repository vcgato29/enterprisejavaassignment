package swinbank.server.ejb.login;

import java.rmi.RemoteException;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.rmi.PortableRemoteObject;
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
        //check if the  login exisis
        try {
            login = getLogin(custId);
        } catch (NoResultException e) {
            throw new AccessDeniedException("\nUser does not exist");
        }
        if (login.getPassword().equals(password)) {
            if (clientType == ClientType.TM) {
                if (login.getIsstaff() == 1) {
                    return true;
                } else {
                    return false;
                }
            } else {
                //check if they have accounts
                Query countQuery = em.createNamedQuery("Login.accountsCount").setParameter("userid", custId);
                int count = (Integer) countQuery.getSingleResult();
                if (count > 0) {
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
        Query userByLoginId = em.createNamedQuery("Login.findByCustid").setParameter("custid", userId);
        Login login = (Login) userByLoginId.getSingleResult();
        return login;
    }
}
