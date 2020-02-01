package controllers.login;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import utils.DBUtil;
import utils.EncryptUtil;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */

    // ログイン画面を表示
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // トークンとエラーの設置
        request.setAttribute("_token", request.getSession().getId());
        request.setAttribute("hasError", false);

        // flushがある時は、設置して削除してください。
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        // login.jspへビューを投げる
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
        rd.forward(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */

    // ログイン処理を実行
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 認証結果を格納する変数
        Boolean check_result = false;

        // コードとパスワードの変数を用意
        String code = request.getParameter("code");
        String plain_pass = request.getParameter("password");

        Employee e = null;

        // コードもパスも空で無い時、EM召喚
        if(code != null && !code.equals("") && plain_pass != null && !plain_pass.equals("")) {
            EntityManager em = DBUtil.createEntityManager();

            // パスワードの暗号化
            String password = EncryptUtil.getPasswordEncrypt(
                    plain_pass,
                    (String)this.getServletContext().getAttribute("salt")
                    );

            // 社員番号とパスワードが正しいかチェック
            try {
                e = em.createNamedQuery("checkLoginCodeAndPassword", Employee.class)
                        .setParameter("code", code)
                        .setParameter("pass", password)
                        // 1件セット
                        .getSingleResult();

                // 結果がない時に例外を投げる
            } catch (NoResultException ex) {}

            em.close();

            // データが空で無ければ、認証結果の確認を行う。
            if(e != null) {
                check_result = true;
            }
        }

        // 認証できなかったらログイン画面に戻る
        if(!check_result) {
            request.setAttribute("_token", request.getSession().getId());
            request.setAttribute("hasError", true);
            request.setAttribute("code", code);

            //login.jspへビューを投げる
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/login/login.jsp");
            rd.forward(request, response);
        } else {
            // 認証出来たらログイン状態にしてトップページへリダイレクト
            request.getSession().setAttribute("login_employee", e);

            request.getSession().setAttribute("flush", "ログインしました。");
            response.sendRedirect(request.getContextPath() + "/");
        }

    }

}
