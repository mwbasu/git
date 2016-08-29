package org.appr;
import org.appr.EmpLogin;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class EmpFeedbackServlet
 */
@WebServlet("/EmpFeedbackServlet")
public class EmpFeedbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EmpFeedbackServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		String eid=request.getParameter("id");//from html
		
		if(eid==null)
		{
			response.sendError(300, "invalid page");
		}
		else if(eid!=null){
		Object obj=request.getAttribute("flist");		//from forward
		ArrayList<Feedback> fdlist=(ArrayList<Feedback>)obj;
		}
		int q=0;
		String x=request.getParameter("c");
		//out.println(x);
		/*out.println(eid);
		/*for(Feedback f:fdlist){
			out.println(q);						
			q+=1;
		}*/
		/*Enumeration<String> name=request.getParameterNames();
		for(String n:name){
			
		}*/
		/*String fer="qid";
		String m1=Integer.toString(q);
		fer+=m1;
		String s=request.getParameter(fer);
		out.println(s);*/
		
		int i=Integer.parseInt(x);
			for(int y=1;y<=i;y++)
			{
				String fr="feedback_rating";
				//String fr1="feedback_rating1"
				String count=Integer.toString(q);
				String qid="qid";
				fr+=count;
				qid+=count;
			int self_f=Integer.parseInt(request.getParameter(fr));
			//int manager_f=Integer.parseInt(request.getParameter(fr1));
			//String s=request.getParameter("feedback_rating"+q);	
			try {
				AppraisalDAO ad=new AppraisalDAO();
				String qidf=request.getParameter(qid);
				
				if (self_f!=0){
					ad.register(eid,"A" ,qidf, self_f);
					
				}
				//response.sendRedirect("EmpFeedback.jsp");
				
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
				response.sendError(999,"err in class loading");
			} catch (SQLException e) {
				
				e.printStackTrace();
				response.sendError(998,"err in SQL "+e.getMessage()+"Error code "+e.getErrorCode());
			}
				//out.println(s);
			q+=1;
			
		}
			//out.println("<center><h4>..Updation Done..</h4></center>");
			response.sendRedirect("update.jsp");
			out.close();
		
		//EmployeeFeedback ef = null;
		//int i=0;
		//ef.setSelf_f((Integer.parseInt(request.getParameter("feedback_rating"+i))));
		//Employee e2=(Employee)request.getSession().getAttribute("emp");
		//out.println("<html><body>");
		//out.println("<h2>"+eid+"</h2>");
		//out.println("</body></html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
	}

}
