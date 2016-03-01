package com.quote.web;

import com.quote.model.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class QuoteController extends HttpServlet {

  public void doPost( HttpServletRequest request, 
                      HttpServletResponse response) 
                      throws IOException, ServletException {

    String name = request.getParameter("clientName");

    QuoteGenerator qg = new QuoteGenerator();

    String result = qg.getQuote(name);

    // The results will be passed back (as an attribute) to the JSP view
    // The attribute will be a name/value pair, the value in this case will be a List object 
    request.setAttribute("quote", result);
    RequestDispatcher view = request.getRequestDispatcher("result.jsp");
    view.forward(request, response); 
  }
}