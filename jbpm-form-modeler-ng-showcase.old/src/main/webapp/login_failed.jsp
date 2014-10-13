<%
  request.getSession().invalidate();
  String redirectURL = request.getContextPath()  +"/org.jbpm.formModeler.ng.jBPMFormModeler/jBPM.html?message=Login failed: Invalid UserName or Password";
  response.sendRedirect(redirectURL);
%>