<%
  request.getSession().invalidate();
  String redirectURL = request.getContextPath()  +"/org.jbpm.formModeler.ng.jBPMFormModeler/jBPM.html?message=Login failed: Not Authorized";
  response.sendRedirect(redirectURL);
%>