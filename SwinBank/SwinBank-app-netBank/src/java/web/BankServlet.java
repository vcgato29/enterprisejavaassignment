/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import java.io.IOException;
import java.rmi.RemoteException;
import javax.naming.InitialContext;
import javax.naming.InvalidNameException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import swinbank.server.ejb.account.AccountRemote;
import swinbank.server.ejb.login.LoginRemote;
import swinbank.server.policy.ClientType;

/**
 *
 * @author Mark
 */
public class BankServlet extends HttpServlet {

    private LoginRemote login;
    private AccountRemote account;
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
        } catch (Exception e) {
            System.out.println("Could not locate Instance of LoginRemote");
            e.printStackTrace();
        }

    }

    @Override
    public void destroy() {
        login = null;
        user = null;
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
                boolean loggedIn = false;

                loggedIn = loginService(username, password);

                System.out.println("LoggedIn = " + loggedIn);
                if (loggedIn) {
                    user = new UserBean();
                    user.setUsername(username);

                    session.setAttribute("userBean", user);


                    dispatchPage = "/accounts.jsp";
                } else {
                    dispatchPage = "/retry_login.jsp";
                }

            } else if (fromPage.equals("/account")) {
                String accountId = request.getParameter("accountId");
                double accountBalance = 0;
                try
                {
                    //BEAN. ACCOUNT BEAN. GETBALANCE()
                    accountBalance = account.getBalance(accountId, user.getUsername(), client);
                     user.setBalance(accountBalance);
                    dispatchPage = "/BankMenu.jsp";
                } catch (RemoteException e)
                {
                    String message = e.getCause().getCause().getMessage();
                    
                    if(message.equals("\nAccount does not Exist!"))
                    {
                        dispatchPage = "/accounts_NoAccount.jsp";
                    }
                    else if(message.equals("\nClient does not have enough priviliges to perform this action!"))
                    {
                        dispatchPage = "/accounts_AccessDenied.jsp";
                    }
                    else
                    {
                        dispatchPage ="/unknown.html";
                    }

                    
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
        } catch (RemoteException e) {
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

    private boolean loginService(String userId, String password) throws RemoteException {
        boolean validLogin = false;

        if (userId != null && password != null) {

            validLogin = login.login(userId, password, ClientType.IB);

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
