package swinbank.server.policy;

/**
 *
 * @author Daniel
 * @version 1.0
 * Modified by Vipin on 29th April, 2010
 * @version 1.1
 */
public class SwinDatabase {

    private User[] users;
    private UserAccount[] accounts;

    public SwinDatabase() {
        users = new User[]{new User("123", "123", false),
                    new User("789", "789", false),
                    new User("456", "456", true)};
        accounts = new UserAccount[]{
                    new UserAccount("1", "123", 100.00),
                    new UserAccount("2", "789", 200.00),
                    new UserAccount("3", "123", 500.00),
                    new UserAccount("5", "123", 300.00)
                };
    }

    public User getUser(String userId) {

        for (int i = 0; i <
                users.length; i++) {
            User user = users[i];

            if (user.userId.equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public UserAccount[] getAccounts() {
        return accounts;
    }

    public static UserAccount getAccount(String accountId) {
        SwinDatabase swinDB = new SwinDatabase();
        UserAccount[] accs = swinDB.getAccounts();
        for (int i = 0; i < accs.length; i++) 
        {

            UserAccount userAccount = accs[i];
            if (userAccount.accountId.equals(accountId))
            {
                return userAccount;
            } else
            {
                continue;
            }
        }
        return null;
    }

    public class User {

        public String userId, password;
        public boolean isStaff;

        public User(String userId, String password, boolean isStaff) {
            this.userId = userId;
            this.password = password;
            this.isStaff = isStaff;
        }
    }

    public class UserAccount {

        public String accountId, userId;
        public double balance;

        public UserAccount(String accountId, String userId, double balance) {
            this.accountId = accountId;
            this.userId = userId;
            this.balance = balance;
        }
    }
}
