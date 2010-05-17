<%@ page language="java" pageEncoding="GBK" contentType="text/html; charset=UTF-8"%>
<%@ page import="rrnchelper.model.User" %>
<%@ page import="javax.cache.Cache" %>
<%@ page import="javax.cache.CacheManager" %>
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
  </head>

  <%
    boolean autoWorkFlag = false;
    Cache cache = CacheManager.getInstance().getCache("cache");
    if (cache != null) {
        User I = (User) cache.get("I");
        if (I != null && I.isAutoWork()) {
            autoWorkFlag = true;
        }
    }
   %>
  <body>
    <form action="/start.do">
      <button type="submit" <%=autoWorkFlag?"disabled":"" %>>开始收菜</button>
    </form>
    <form action="/stop.do">
      <button type="submit" <%=autoWorkFlag?"":"disabled" %> }>停止收菜</button>
    </form>
  </body>
</html>
