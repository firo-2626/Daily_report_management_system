package validators;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import models.Employee;
import utils.DBUtil;

// 社員データの入力,重複チェック
public class EmployeeValidator {

    // エラーチェックリスト
    public static List<String> validate(Employee e, Boolean code_duplicate_check_flag, Boolean password_check_flag) {
        List<String> errors = new ArrayList<String>();

        // 社員番号エラーの取得
        String code_error = _validateCode(e.getCode(), code_duplicate_check_flag);
        if(!code_error.equals("")) {
            errors.add(code_error);
        }

        // 社員名エラーの取得
        String name_error = _validateName(e.getName());
        if(!name_error.equals("")) {
            errors.add(name_error);

        }

        // パスワードエラーの取得
        String password_error = _validatePassword(e.getPassword(), password_check_flag);
        if(!password_error.equals("")) {
            errors.add(password_error);
        }

        return errors;
    }

    // 社員番号のエラーチェック
    private static String _validateCode(String code, Boolean code_duplicate_check_flag) {
        // 必須入力チェック
        if(code == null || code.equals("")) {
            return "社員番号を入力してください。";
        }

        // 社員番号ダブりチェック
        if(code_duplicate_check_flag) {
            EntityManager em = DBUtil.createEntityManager();
            long employees_count = (long)em.createNamedQuery("checkRegisteredCode", Long.class)
                    .setParameter("code", code)
                    .getSingleResult();

            em.close();

            // 既に登録された社員番号を発見した時
            if(employees_count > 0) {
                return "入力された社員番号の情報はすでに存在しています。";
            }
        }

        return "";
    }

    // 社員名の必須入力チェック
    private static String _validateName(String name) {

        //入力が空の場合
        if(name == null || name.equals("")) {
            return "氏名を入力してください。";
        }

        return "";
    }

 // パスワードの必須入力チェック
    private static String _validatePassword(String password, Boolean password_check_flag) {

        // パスワード変更時のみ実行
        if(password_check_flag && (password == null || password.equals(""))) {
            return "パスワードを入力してください。";
        }

        return "";

    }

}
