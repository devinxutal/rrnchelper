<%@ page language="java" pageEncoding="GBK" contentType="text/html; charset=UTF-8"%>
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
    <form action="/start.do">
      <button type="submit" ${I.autoWork? "disabled='disabled'":""}>��ʼ�ղ�</button>
    </form>
    <form action="/stop.do">
      <button type="submit" ${I.autoWork? "":"disabled='disabled'"}>ֹͣ�ղ�</button>
    </form>
  </body>
</html>
