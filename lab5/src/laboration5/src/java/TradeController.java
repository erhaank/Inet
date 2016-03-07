import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import bean.*;


public class TradeController extends HttpServlet{

	DatabaseAccessor db = new DatabaseAccessor();
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
	
	String message = "";

	if(request.getParameter("action").equals("addSecurity")){
	    message = db.addSecurity(request.getParameter("security"));
	}
	
	if(request.getParameter("action").equals("addOrder")){
		// Kod för att lägga en köp eller säljorder
	    // samt eventuellt skapa en trade
		Order order = new Order();
		order.setSecurity(request.getParameter("security"));
		order.setType(request.getParameter("buyOrSell"));
		order.setPrice(request.getParameter("price"));
		order.setAmount(request.getParameter("amount"));
		order.setUid(request.getSession().getId());
	    manageOrder(order);
	    message = "addOrder";
	}
	    
	if(request.getParameter("action").equals("viewTrades")){
	    // Kod för att lägga en köp eller säljorder
	    message = "viewTrades";
	}

	if(request.getParameter("action").equals("DBTEST")){
		ArrayList<Security> securities = db.getSecurities();
		//message = "<b>Securities:</b>";
		for (Security s : securities) {
			if (s.getName().equals(request.getParameter("testText"))) {
				request.setAttribute("security", s);
				RequestDispatcher dispatcher = request.getRequestDispatcher("trade.jsp");
        		dispatcher.forward(request, response);
			}
			//message += "<br>"+s.getId()+": "+s.getName();
		}
		/*message += "<br><br><b>Orders:</b>";
		for (Order o : db.getOrders())
			message += "<br>"+o.getId()+": "+o.getName() + "(" + o.getPrice() + "), "+o.getAmount();
	*/
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

    private void manageOrder(Order order) {
    	ArrayList<Order> orders = db.getOrders();
    	if (order.getType().equals("S")) { // Sell
    		ArrayList<Order> matches = new ArrayList<Order>();
    		for (Order other : orders) {
    			if (!other.getType.equals(order.getType()) && other.getSecurity().equals(order.getSecurity()))
    				matches.add(other);
    		}
    		//Now matches include all of the orders that want to buy from the same security
    		int buy = order.getAmount();
    		for (Order m : matches) {
    			if ()
    		}
    	} else { // Buy
    		ArrayList<Order>
    	}
    }
} 
