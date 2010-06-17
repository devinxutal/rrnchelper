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
    <title>人人农场自动收菜程序</title>
  	<link type="text/css" href="/css/main.css" rel="stylesheet">
  </head>

  <body>  
  	<%
        List<User> users = UserDao.findAll();
        for(int i = 0; i< users.size(); i++){
        	User user = users.get(i);
        	String username = user.getUsername();
        	Boolean autoWork = user.isAutoWork();
        	String addr = user.getFarmAddress();
        	String formAction = autoWork?"/stop.do":"/start.do";
        	String actionStr =  autoWork?"停止":"启动";
   	%>
   		<div class="section_title"><%=username%></div>
   		<div class="section">
		    <form action="<%=formAction%>">
		      <input type="hidden" name="username" value="<%=username%>" />
		      <button class="button" type="submit"><%=actionStr%></button>
		    </form>
		    <a class="navlink" href="<%=addr%>" >去农场看看吧</a>
		    <a class="navlink" href="<%=addr%>" >查看偷菜记录</a>
		    <a class="navlink" href="<%=addr%>" >查看喂食记录</a>
		</div>
	<%
		}
	%>
  </body>
</html>
