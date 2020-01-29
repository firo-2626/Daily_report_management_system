<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- エラーがあれば、エラーを表示 -->
<c:if test="${errors != null}">
    <div id="flush_error">
        入力内容にエラーがあります。 <br />

        <!-- エラーのリスト表示 -->
        <c:forEach var="error" items="${errors }">
            ・<c:out value="${error }"/><br/>
        </c:forEach>
    </div>
</c:if>

<!-- 社員番号ボックス  -->
<label for="code">社員番号</label><br/>
<input type="text" name="code" value="${employee.code}"/>
<br/><br/>

<!-- 氏名ボックス  -->
<label for="name">氏名</label><br/>
<input type="text" name="name" value="${employee.name}"/>
<br/><br/>

<!-- パスワードボックス  -->
<label for="password">パスワード</label><br/>
<input type="text" name="password" />
<br/><br/>

<!-- 権限レベルの選択 -->
<label for="admin_flag">権限</label><br/>
<select name="admin_flag">
    <option value="0"<c:if test="${employee.admin_flag == 0 }"> selected</c:if>>一般</option>
    <option value="1"<c:if test="${employee.admin_flag == 1 }"> selected</c:if>>管理者</option>
</select>
<br/><br/>

<!-- トークンを取り入れた、投稿ボタン -->
<input type="hidden" name="_token" value="${_token}" />
<button type="submit">投稿</button>