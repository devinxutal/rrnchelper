<%@ page language="java" pageEncoding="GBK" contentType="text/html; charset=UTF-8"%>
<%@ page import="rrnchelper.model.User" %>
<%@ page import="rrnchelper.db.dao.UserDao" %>
<%@ page import="java.util.List" %>
<%@ page isELIgnored="false" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- The HTML 4.01 Transitional DOCTYPE declaration-->
<!-- above set at the top of the file will set     -->
<!-- the browser's rendering engine into           -->
<!-- "Quirks Mode". Replacing this declaration     -->
<!-- with a "Standards Mode" doctype is supported, -->
<!-- but may lead to some differences in layout.   -->

<html>
  <head>
    <title>����ũ���Զ��ղ˳���</title>
  </head>

  <body>
  
  
  	<%
        List<User> users = UserDao.findAll();
        for(int i = 0; i< users.size(); i++){
        	User user = users.get(i);
        	String username = user.getUsername();
        	Boolean autoWork = user.isAutoWork();
        	String addr = user.getFarmAddress();
   	%>
   			<a href="<%=addr%>" > Go to <%=username%>'s farm</a>
		    <form action="/start.do">
		      <input type="hidden" name="username" value="<%=username%>" />
		      <button type="submit" <%=autoWork? "disabled":"" %>>��ʼ�ղ�[<%=username %>]</button>
		    </form>
		    <form action="/stop.do">
		      <input type="hidden" name="username" value="<%=username%>" />
		      <button type="submit" <%=autoWork? "":"disabled" %>>ֹͣ�ղ�[<%=username %>]</button>
		    </form>
		    <br>
		    <br>
		    <br>
	<%
		}
	%>
  </body>
</html>
