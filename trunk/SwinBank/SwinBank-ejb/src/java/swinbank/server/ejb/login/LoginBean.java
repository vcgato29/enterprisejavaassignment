package swinbank.server.ejb.login;

import java.rmi.RemoteException;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.rmi.PortableRemoteObject;
import swinbank.server.policy.ClientType;
import swinbank.server.policy.SwinDatabase;
import swinbank.server.policy.SwinDatabase.User;

/**
 *
 * @author Daniel
 */
@Stateless
@Remote(LoginRemote.class)
public class LoginBean extends PortableRemoteObject implements LoginRemote {

    @PersistenceContext
    private EntityManager em;

    public LoginBean() throws RemoteException{

    }

    public boolean login(String userId, String password, ClientType clientType) throws RemoteException{

        User user = new SwinDatabase().getUser(userId);
        Query queryGetUser = em.createNamedQuery("Login.findByCustid").setParameter("custid", userId);
        if (user.password.equals(password))
        {
            if(clientType == ClientType.TM)
            {
                return user.isStaff;
            }
            else
            {
                //check if they have accounts
                Query countQuery = em.createNamedQuery("Login.accountsCount").setParameter("userid", user.userId);
                int count =(Integer) countQuery.getSingleResult();
                if (count > 0)
                    return !user.isStaff;
            }
        }
        return false;
        

    }
}
