package controllers.reports;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Report;
import utils.DBUtil;
import validators.ReportValidator;

/**
 * Servlet implementation class ReportsCreateServlet
 */
@WebServlet("/reports/create")
public class ReportsCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsCreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */

    //DBに日報をINSERTする。
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //トークンの取得
        String _token = (String)request.getParameter("_token");

      //トークン確認とEMの召喚
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();


        // ログイン者から受け取った、レポート内容の設置
        // 更新対象をnew
        Report r = new Report();

        // ログイン中の社員を取得 : Employee
        r.setEmployee((Employee)request.getSession().getAttribute("login_employee"));

        // 日報のデータが空でも、現在日時を取得する。
        Date report_date = new Date(System.currentTimeMillis());
        String rd_str = request.getParameter("report_date");
        if(rd_str != null && !rd_str.equals("")) {
            // str : 値に変換
            report_date = Date.valueOf(request.getParameter("report_date"));


        // 取得内容をセットしていきます : リクエストスコープに
        r.setReport_date(report_date);
        r.setTitle(request.getParameter("title"));
        r.setContent(request.getParameter("content"));

        // 作成日と更新日をセット
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        r.setCreated_at(currentTime);
        r.setUpdated_at(currentTime);

        //エラーチェック
        // エラーがあれば、エラー情報をnew.jspへ投げる
        List<String> errors = ReportValidator.validate(r);
        if(errors.size() > 0) {
            em.close();

            request.setAttribute("_token", request.getSession().getId());
            request.setAttribute("report", r);
            request.setAttribute("errors", errors);

            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/reports/new.jsp");
            rd.forward(request, response);

            // ここまでセットされたものを、DBにコミット
        }else{
            em.getTransaction().begin();
            em.persist(r);
            em.getTransaction().commit();
            em.close();
            request.getSession().setAttribute("flush", "登録が完了しました。");

            //一覧へ戻す
            response.sendRedirect(request.getContextPath() + "/reports/index");
        }
        }
        }
    }

}
