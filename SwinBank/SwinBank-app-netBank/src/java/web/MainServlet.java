/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import swinbank.server.ejb.account.AccountRemote;
import swinbank.server.ejb.login.LoginRemote;
import swinbank.server.ejb.transaction.TransactionRemote;
import swinbank.server.entity.Transactions;
import swinbank.server.policy.ClientType;
import swinbank.server.policy.InvalidFundsException;

/**
 *
 * @author Mark
 */
public class MainServlet extends HttpServlet {

    private LoginRemote login;
    private AccountRemote account;
    private TransactionRemote transaction;

    HttpSession session;
    private UserBean user;
    private ClientType client = ClientType.IB;

    @Override
    public void init() throws ServletException {
        super.init();

        try {
            InitialContext context = new InitialContext();
            login = (LoginRemote) context.lookup(LoginRemote.class.getName());

            account = (AccountRemote) context.lookup(AccountRemote.class.getName());
            transaction = (TransactionRemote) context.lookup(TransactionRemote.class.getName());
        } catch (Exception e) {
            System.out.println("Could not locate Instance of LoginRemote");
            e.printStackTrace();
        }

    }

    @Override
    public void destroy() {
        login = null;
        user = null;
        account = null;
        transaction = null;
        session = null;
        super.destroy();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean removeUserBean = false;

        session = request.getSession(true);

        String fromPage = request.getServletPath();
        String dispatchPage = null;

        System.out.println("fromPage is " + fromPage);
        try {
            if (fromPage.equals("/login")) {
                String username = request.getParameter("userid");
                String password = request.getParameter("password");

                int usernameInt = -1;
                boolean loggedIn = false;

                try
                {
                    usernameInt = Integer.parseInt(username);
                    loggedIn = loginService(usernameInt, password);

                } catch (NumberFormatException e)
                {
                    //not logged in, redirect to retry loggin
                }
                catch(RemoteException e)
                {
                    System.out.println("AnErrorOcured");
                }

                

                System.out.println("LoggedIn = " + loggedIn);
                if (loggedIn) {
                    user = new UserBean();
                    user.setUsername(usernameInt);

                    session.setAttribute("userBean", user);


                    dispatchPage = "/MainMenu.jsp";
                } else {
                    dispatchPage = "/retry_login.jsp";
                }

            }
            
            else if(fromPage.equals("/moneyTransfer"))
            {
                
                String toAccountVal = request.getParameter("toAccount");
                String fromAccountVal = request.getParameter("fromAccount");
                String amountVal = request.getParameter("amount");

                
                int toAcount = -1;
                int fromAccount = -1;
                double amount = 0.01;

                boolean TransactionComplete = false;
                try
                {
                    
                    toAcount = Integer.parseInt(toAccountVal);
                    fromAccount = Integer.parseInt(fromAccountVal);
                    amount = Double.parseDouble(amountVal);

                    transaction.moneyTransfer(user.getUsername(), toAcount, fromAccount,client , amount, "");

                    TransactionComplete = true;
                }
                catch(NumberFormatException e)
                {
                    dispatchPage = "/error_MoneyTransfer.jsp";
                }
                catch(RemoteException e)
                {
                    if(e.getCause().getCause() instanceof  InvalidFundsException)
                    {
                        dispatchPage = "/error_funds_MoneyTransfer.jsp";
                    }
                    else
                    {
                        dispatchPage = "/error_MoneyTransfer.jsp";
                    }
                }

                if( TransactionComplete)
                {
                    dispatchPage = "success_MoneyTransfer.jsp";
                }

                //different money transfer pages.
                //-MoneyTransferMenu
                //
                //-success_MoneyTransfer
                //
                //-error_MoneyTransfer
                //-eror_funds_MoneyTransder


            }
            else if(fromPage.equals("/billPayment"))
            {
                String accountIdVal = request.getParameter("fromAccount"); //int
                String billerIdVal = request.getParameter("biller"); //int
                String amountVal = request.getParameter("amount"); //double
                String description = request.getParameter("description");

                int accountId = -1;
                int billerId = -1;
                double amount = 0.01;

                boolean billPayed = false;
                try
                {
                    accountId = Integer.parseInt(accountIdVal);
                    billerId = Integer.parseInt(billerIdVal);
                    amount = Double.parseDouble(amountVal);

                    transaction.billPayment(accountId, billerId, client, amount, description);
                }
                catch(NumberFormatException e)
                {
                    dispatchPage = "/error_Bill.jsp";
                }
                catch(RemoteException e)
                {
                    dispatchPage = "/error_Bill.jsp";
                }
            }
            
            else if (fromPage.equals("/accountSelection")) {
                String accountId = request.getParameter("accountId");
                double accountBalance = 0;
                List<Transactions> transactions = null;
                Date now = new Date();

                try {
                    int accountInt = Integer.parseInt(accountId);
                    //BEAN. ACCOUNT BEAN. GETBALANCE()
                    accountBalance = account.getBalance(accountInt, user.getUsername(), client);
                    transactions = account.getTransactions(accountInt, user.getUsername(), client, null, null);

                    user.setBalance(accountBalance);
                    user.setTransactions(transactions);
                    dispatchPage = "/AccountBalance.jsp";
                } catch (RemoteException e) {
                    String message = e.getCause().getCause().getMessage();

                    if (message.equals("\nAccount does not Exist!")) {
                        dispatchPage = "/accounts_NoAccount.jsp";
                    } else if (message.equals("\nClient does not have enough priviliges to perform this action!")) {
                        dispatchPage = "/accounts_AccessDenied.jsp";
                    } else {
                        dispatchPage = "/unknown.html";
                    }


                }
                catch(NumberFormatException e)
                {

                }


            } else if (fromPage.equals("/logout")) {
                user = (UserBean) session.getAttribute("userBean");

                if (user != null) {
                    dispatchPage = "/logoff.jsp";

                    removeUserBean = true;
                } else {
                    dispatchPage = "/unknown.html";
                }
            } else {
                dispatchPage = "/unknown.html";
            }
        } catch (Exception  e) {
            dispatchPage = "/service_down.html";
        }
        try {
            // use the RequestDispatcher to dispatch the relevant page
            RequestDispatcher disp =
                    this.getServletContext().getRequestDispatcher(dispatchPage);
//                    request.getRequestDispatcher(dispatchPage);

            // actual dispatching of the relevant page
            disp.forward(request, response);

            // need to remove UserBean ?
            if (removeUserBean) {
                session.removeAttribute("userBean");
                user = null;

                removeUserBean = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    private boolean loginService(int username, String password) throws RemoteException {
        boolean validLogin = false;

        if (password != null) {

            validLogin = login.login(username, password, ClientType.IB);

        }

        return validLogin;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
