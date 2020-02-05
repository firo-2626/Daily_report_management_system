package controllers.reports;

import java.io.IOException;
import java.sql.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Report;

/**
 * Servlet implementation class ReportsNewServlet
 */
@WebServlet("/reports/new")
public class ReportsNewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsNewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //トークンのセット
        request.setAttribute("_token", request.getSession().getId());

        //事前に本日の日付を格納
        Report r = new Report();
        r.setReport_date(new Date(System.currentTimeMillis()));
        request.setAttribute("report", r);

        //new.jspへビューを投げる
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/new.jsp");
        rd.forward(request, response);
    }

}
