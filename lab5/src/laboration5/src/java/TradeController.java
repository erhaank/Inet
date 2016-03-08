import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import bean.*;
import java.util.Random;
import java.lang.StringBuilder;


public class TradeController extends HttpServlet{

	DatabaseAccessor db = new DatabaseAccessor();
	Random r = new Random();
    
    public void doGet(HttpServletRequest request, HttpServletResponse response){
		
		String message = "";

		if(request.getParameter("action").equals("addSecurity")){
		    message = db.addSecurity(request.getParameter("security"));
		    //TODO: securities ska bestå av de som läggs till här fixa i jsp!!
		    //Databaseaccessor ska skickas till trade.jsp
		}
		
		if(request.getParameter("action").equals("addOrder")){
		    // Kod för att lägga en köp eller säljorder
		    // samt eventuellt skapa en trade
			StringBuilder sb = new StringBuilder();
			//TODO: gör om till namn istället
			String uid = "" + r.nextInt(1000);
		    sb.append(db.addOrder(request.getParameter("security"), request.getParameter("buyOrSell"), 
		    	Integer.parseInt(request.getParameter("price")), Integer.parseInt(request.getParameter("amount")),
		    	uid));
		    //TODO: check if there is already a user with r.nextInt(1000)


		    sb.append(db.tryTrade(uid, request.getParameter("buyOrSell")));
		    message = sb.toString();
		}


		    
		if(request.getParameter("action").equals("viewTrades")){
		    // Kod för att visa genomförda orders
		    //TODO: ändra till trades och inte orders!!
		    //TODO: göra till tabell istället
		    ArrayList<Order> orders = db.getOrders();
		    StringBuilder sb = new StringBuilder();
		    for(Order o : orders) {
		    	if(o.getName().equals(request.getParameter("security"))) {
		    		sb.append("<br>" + "Id: " + o.getId() + " Name: " + o.getName() + " UId: " + o.getUid()
		    			+ " Type: " + o.getType() + " Price: " + o.getPrice() + " Amount: " + o.getAmount());
		    	}
		    }
		    message = sb.toString();
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
