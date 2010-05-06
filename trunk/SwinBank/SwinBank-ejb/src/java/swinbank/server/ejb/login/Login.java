package swinbank.server.ejb.login;

import java.rmi.RemoteException;
import javax.ejb.Remote;
import javax.ejb.Stateless;
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
public class Login extends PortableRemoteObject implements LoginRemote {

    public Login() throws RemoteException{

    }

    public boolean login(String userId, String password, ClientType clientType) throws RemoteException{

        User user = new SwinDatabase().getUser(userId);

        if (user.password.equals(password))
        {
            if(clientType == ClientType.TM)
            {
                return user.isStaff;
            }
            else
            {
                //check if they have accounts
                if (user.getAccounts().size() > 1)
                    return !user.isStaff;
            }
        }
        return false;
        

    }
}
