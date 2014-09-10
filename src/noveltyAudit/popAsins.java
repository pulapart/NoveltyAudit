package noveltyAudit;

import java.io.IOException;
import java.io.PrintWriter;

//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.getDetails.getAttributes;



/**
 * Servlet implementation class popAsins
 */
@WebServlet("/popAsins")
public class popAsins extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public popAsins() {
        super();
        // TODO Auto-generated constructor stub
    }

		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	
			response.setContentType("text/html");
	        PrintWriter out = response.getWriter();
	       
	        
			
	        HttpSession session = request.getSession();
			String merchid = request.getParameter("merchid");
			merchid = merchid.trim();
	        String mplace = request.getParameter("mplace");
	        String sz = request.getParameter("size");
	        sz = sz.trim();
	        
	        String titlekeywords = request.getParameter("titlekeywords"); 	        
	        titlekeywords = titlekeywords.trim().replace(" ", "_");
	        
	        String genkeywords = request.getParameter("genkeywords"); 	        
	        genkeywords = genkeywords.trim().replace(" ", "_");
	        
	        if(merchid.equals("") || mplace.equals("") || sz.equals("") ){
	        //    throw new ServletException("Mandatory fields can't be null or empty");
	            response.sendRedirect("noveltyaudit.html");
	        }
	        
	        session.setAttribute("merchid", merchid);
	        session.setAttribute("mplace", mplace);
	        int size = Integer.parseInt(sz);
	        try {
	        	       	
	        	merchid = merchid.trim();
	        	String[] asins = getAttributes.fetchSampleasins(merchid, mplace, titlekeywords, genkeywords, size);
	        		
	        	if ( asins == null ) throw new Exception ("no asins");
	        		
	        	String[][] attribs = getAttributes.fetchUrls(asins, mplace); 
	        	
	        	String[] imageurls = new String[attribs.length];
	        	String[] titles = new String[attribs.length];
	        	
	        	for (int i = 0; i < attribs.length; i++) {
	        		imageurls[i] = attribs[i][1];
	        		titles[i] = attribs[i][0];
	        	}
	        	
	      
	        	
	            out.write("<html><head></head><body>");
	        	 
	            out.write("<title>Sample Size</title>");
	       	 
	            String site = "";
	           	 
	           	 
	            if (mplace.equals("US")) { site = "http://www.amazon.com"; } 
	     	    else if (mplace.equals("UK")) {site = "http://www.amazon.co.uk" ;}
	     	    else if (mplace.equals("CA")) { site = "http://www.amazon.ca" ;}
	     	    else if (mplace.equals("DE")) { site = "http://www.amazon.de" ;}
	     	    else if (mplace.equals("FR")) { site = "http://www.amazon.fr"; }
	     	    else if (mplace.equals("ES")) { site = "http://www.amazon.es" ;  }
	     	    else if (mplace.equals("JP")) { site = "http://www.amazon.co.jp" ;}
	     	   else if (mplace.equals("IT")) { site = "http://www.amazon.it" ;}
	           
	           	 String dpurl = "";
	         
	           	 
	           out.write("<table border = '1' CELLPADDING='0' CELLSPACING='1'   bordercolor='black'>"); 
	        	
	           for (int i = 0; i < imageurls.length; i=i+4) {
			    	 
	        	if (imageurls[i] == null) break; 
	        	
	        	//   dpurl = site+"/dp/"+asins[i];
	        	//   out.write("<img src = "+ imageurls[i] +">");
	        	//   out.write(asins[i]);
	        			out.write("<tr>"); 
	        			
	        		if (imageurls[i] != null) {
	        				dpurl = site+"/dp/"+asins[i];
	        				out.write("<td height='300' width='450' align='center' valign='middle'> <font color='Black'> <b><img style='max-width: 200px; height: auto;' src="+imageurls[i]+" > </b></font><br><a href="+dpurl+" target='_blank'>"+titles[i]+"</a><br></td>");
	        				
	        			}
	        			
	        			if (imageurls[i+1] != null) {
	        				 dpurl = site+"/dp/"+asins[i+1];
	        				out.write("<td height='300' width='450' align='center' valign='middle'> <font color='Black'> <b><img style='max-width: 200px; height: auto;' src="+imageurls[i+1]+" > </b></font><br><a href="+dpurl+" target='_blank'>"+titles[i+1]+"</a><br></td>");
	        				
	        				}
	        			
	        			if (imageurls[i+2] != null) {
	        				dpurl = site+"/dp/"+asins[i+2];
	        				out.write("<td height='300' width='450' align='center' valign='middle'> <font color='Black'> <b><img style='max-width: 200px; height: auto;' src="+imageurls[i+2]+" > </b></font><br><a href="+dpurl+" target='_blank'>"+titles[i+2]+"</a></br></td>");
	        				
	        				}
	        			
	        			if (imageurls[i+3] != null) {
	        				dpurl = site+"/dp/"+asins[i+3]; 					
	        				out.write("<td height='300' width='450' align='center' valign='middle'> <font color='Black'> <b><img style='max-width: 200px; height: auto;'  src="+imageurls[i+3]+" > </b></font><br><a href="+dpurl+" target='_blank'>"+titles[i+3]+"</a><br></td>");
	        				
	        			}
	        				        				
	        			out.write("<tr>"); 	 
	         		}
	         		
	         		out.write("</table> </Font>");
	         		out.write("</body></html>");
	     	        	 
	    	    
	        } catch (Exception e) {
			
	        	if (e.getMessage().equals("no asins")) {
	        		
	        		out.write("<html><head></head><body>");
    				out.write("<br>");out.write("<br>");out.write("<br>");
    				out.write("<Center>Couldnt fetch sample size for given input <br> <br>  check if input Merchant Id and Marketplace are correct <br> <br> else try different keywords as input</Center>");
    				out.write("<br>");out.write("<br>");out.write("<br>");
    				out.write("</body></html>"); 	
	        		
	        	}
	        	
			}
					
		}
		
		

}
