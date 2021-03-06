package com.wlas.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.wlas.conn.ConnectionUtils;
import com.wlas.conn.SendEmail;

/**
 * Servlet implementation class ForgotPassword
 */
@WebServlet("/ForgotPassword")
public class ForgotPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ForgotPassword() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		String userId=request.getParameter("userId");
		String email= request.getParameter("email");
		HttpSession sess= request.getSession();
		String redirectPage= "";
		
		sess.setAttribute("message", "");
		
		try{
			  Connection conn = ConnectionUtils.getConnection();
			    
		      // the mysql insert statement
		      String query = "select * from user where userid=? and email=?;";
		      		
		      
		      // create the mysql insert preparedstatement
		      PreparedStatement preparedStmt = conn.prepareStatement(query);
		      preparedStmt.setString (1, userId);
		      preparedStmt.setString (2, email);
		     

		      // execute the preparedstatement
		       ResultSet rs= preparedStmt.executeQuery();
		       
		       if (rs.next())
		       {
		    	   
		    	    String query1="update user set password=? where userid=?";
		    	   PreparedStatement preparedStmt1 = conn.prepareStatement(query1);
				      preparedStmt1.setString (1, "0000");
				      preparedStmt1.setString (2, userId);
				      // execute the preparedstatement
				     preparedStmt1.executeUpdate();
				     SendEmail.toMail(email,  "email has been sent");
				   sess.setAttribute("message", "Your password has been reset to 1111");  
		    	  
		       }
		    	   
		       else
		       {
		    	 sess.setAttribute("message", "Invalid User Id or Email id.");
		       }
		      
		      ConnectionUtils.closeQuietly(conn);
		    }
		    catch (Exception e)
		    {
		      System.err.println("Got an exception!");
		      System.err.println(e.getMessage());
		      e.printStackTrace();
		    }
		
		response.sendRedirect("Forgot_result.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
