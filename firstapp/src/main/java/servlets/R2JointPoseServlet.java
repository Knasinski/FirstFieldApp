package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import field.sample.amtapp1.domain.controller_servers.RobotControllerServerImpl;

/**
 * Servlet implementation class R2JointPoseServlet
 */
@WebServlet("/R2JointPoseServlet")
public class R2JointPoseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public R2JointPoseServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean stopme = false;
		response.setContentType("text/event-stream");
		response.setCharacterEncoding("UTF-8");

		PrintWriter writer = response.getWriter();

		while (!stopme) {
			ArrayList<RobotControllerServerImpl> r = field.sample.amtapp1.domain.service.ControllerServiceImpl.RcList;
			String n = r.get(1).controllerName;
			String p = r.get(1).getRobotJas();
			String s = String.format("%-20s%s", n, p);

			writer.write("data: "+ s +"\n\n");
			writer.flush();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		writer.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
