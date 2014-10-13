<%
    String queryString = request.getQueryString();
    String redirectURL = "org.jbpm.formModeler.ng.jBPMFormModeler/jBPM.html?" + ( queryString == null ? "" : queryString );
    response.sendRedirect( redirectURL );
%>
