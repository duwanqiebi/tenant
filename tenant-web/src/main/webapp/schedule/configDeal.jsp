<%@page import="java.util.Properties"%>
<%@ page import="com.dwqb.tenant.schedule.zk.ZKManager" %>
<%@ page import="com.dwqb.tenant.schedule.ConsoleManager" %>

<%@ page contentType="text/html; charset=GB2312" %>
<%

    Properties p = new Properties();
    p.setProperty(ZKManager.keys.zkConnectString.toString(),request.getParameter(ZKManager.keys.zkConnectString.toString()));
    p.setProperty(ZKManager.keys.rootPath.toString(),request.getParameter(ZKManager.keys.rootPath.toString()));
    p.setProperty(ZKManager.keys.userName.toString(),request.getParameter(ZKManager.keys.userName.toString()));
    p.setProperty(ZKManager.keys.password.toString(),request.getParameter(ZKManager.keys.password.toString()));
    p.setProperty(ZKManager.keys.zkSessionTimeout.toString(),request.getParameter(ZKManager.keys.zkSessionTimeout.toString()));
    try{
        ConsoleManager.saveConfigInfo(p);
    }catch(Exception e){
        e.printStackTrace();
        response.sendRedirect("config.jsp?error=" + e.getMessage());
    }
%>
<script>
    if(parent != null){
        alert("index.jsp?manager=true");
        parent.location.href ="index.jsp?manager=true";
    }else{
        alert("index.jsp?manager=true");
        window.location.href ="index.jsp?manager=true";
    }
</script>
