import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import bean.*;


public class TradeController extends HttpServlet{

	DatabaseAccessor db = new DatabaseAccessor();
    
    public void doGet(HttpServletRequest request, HttpServletResponse response){
	
	String message = "";

	if(request.getParameter("action").equals("addSecurity")){
	    message = db.addSecurity(request.getParameter("security"));
	}
	
	if(request.getParameter("action").equals("addOrder")){
	    // Kod för att lägga en köp eller säljorder
	    // samt eventuellt skapa en trade
	    message = "addOrder";
	}
	    
	if(request.getParameter("action").equals("viewTrades")){
	    // Kod för att lägga en köp eller säljorder
	    message = "viewTrades";
	}

	if(request.getParameter("action").equals("DBTEST")){
		ArrayList<Security> securities = db.getSecurities();
		for (Security s : securities)
			message += "<br>"+s.getId()+": "+s.getName();
	}
	
	try{
	    RequestDispatcher rd =
		request.getRequestDispatcher("trade.jsp?message=" + message);
	    rd.forward(request, response);
	}
	catch(ServletException e){
	    System.out.print(e.getMessage());
	}
	catch(IOException e){
	    System.out.print(e.getMessage());
	}
    }
} 
