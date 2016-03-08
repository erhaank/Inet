import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import bean.*;
import java.util.Random;
import java.lang.StringBuilder;


public class TradeController extends HttpServlet {

	DatabaseAccessor db = new DatabaseAccessor();
	Random r = new Random();
	Securities securities = new Securities();
	Trades trades;
    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
		
		if (request.getParameter("action") != null) {

			if(request.getParameter("action").equals("addSecurity")){
			    db.addSecurity(request.getParameter("security"));
			}
			
			if(request.getParameter("action").equals("addOrder")){
			    // Kod för att lägga en köp eller säljorder
			    // samt eventuellt skapa en trade
				String uid = request.getSession().getId();
				Order o = new Order();
				o.setSecurity(request.getParameter("security"));
				o.setType(request.getParameter("buyOrSell"));
				o.setPrice(Integer.parseInt(request.getParameter("price")));
				o.setAmount(Integer.parseInt(request.getParameter("amount")));
				o.setUid(uid);
			    db.addOrder(o);

			    db.tryTrade(uid, request.getParameter("buyOrSell"));
			}


			    
			if(request.getParameter("action").equals("viewTrades")){
			    // Kod för att visa genomförda orders
			    updateTrades(request, request.getParameter("security"));
			}
		}

		updateSecurities(request);

	    RequestDispatcher rd =
		request.getRequestDispatcher("trade.jsp");
	    rd.forward(request, response);
    }

    private void updateSecurities(HttpServletRequest request) {
    	securities = new Securities();
    	for (Security s : db.getSecurities()) {
    		securities.addSecurity(s);
    	}
    	request.setAttribute("securities", securities);
    }

    private void updateTrades(HttpServletRequest request, String security) {
    	trades = new Trades();
    	for (Trade t : db.getTrades()) {
    		if (t.getName().equals(security))
    			trades.addTrade(t);
    	}
    	request.setAttribute("trades", trades);
    }

} 
