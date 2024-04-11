<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"
                      "file:///K:/Thip/5.0.0/websrcsvil/dtd/xhtml1-transitional.dtd">
<html>
<!-- WIZGEN Therm 2.0.0 as Batch form - multiBrowserGen = true -->
<%=WebGenerator.writeRuntimeInfo()%>
<head>
<%@ page contentType="text/html; charset=Cp1252"%>
<%@ page import= " 
  java.sql.*, 
  java.util.*, 
  java.lang.reflect.*, 
  javax.naming.*, 
  com.thera.thermfw.common.*, 
  com.thera.thermfw.type.*, 
  com.thera.thermfw.web.*, 
  com.thera.thermfw.security.*, 
  com.thera.thermfw.base.*, 
  com.thera.thermfw.ad.*, 
  com.thera.thermfw.persist.*, 
  com.thera.thermfw.gui.cnr.*, 
  com.thera.thermfw.setting.*, 
  com.thera.thermfw.collector.*, 
  com.thera.thermfw.batch.web.*, 
  com.thera.thermfw.batch.*, 
  com.thera.thermfw.pref.* 
"%> 
<%
  ServletEnvironment se = (ServletEnvironment)Factory.createObject("com.thera.thermfw.web.ServletEnvironment"); 
  BODataCollector YEtichettUdsVenBODC = null; 
  List errors = new ArrayList(); 
  WebJSTypeList jsList = new WebJSTypeList(); 
  WebForm YEtichettUdsVenForm =  
     new com.thera.thermfw.web.WebFormForBatchForm(request, response, "YEtichettUdsVenForm", "YEtichettUdsVen", "Arial,10", "com.thera.thermfw.batch.web.BatchFormActionAdapter", false, false, true, true, true, true, null, 1, false, null); 
  YEtichettUdsVenForm.setServletEnvironment(se); 
  YEtichettUdsVenForm.setJSTypeList(jsList); 
  YEtichettUdsVenForm.setHeader("it.thera.thip.cs.Header.jsp"); 
  YEtichettUdsVenForm.setFooter("it.thera.thip.cs.Footer.jsp"); 
  ((WebFormForBatchForm)  YEtichettUdsVenForm).setGenerateSSDEnabled(true); 
  YEtichettUdsVenForm.setWebFormModifierClass("it.softre.thip.uds.vendite.web.YEtichetteUdsVenBatchFormModifier"); 
  YEtichettUdsVenForm.setDeniedAttributeModeStr("hideNone"); 
  int mode = YEtichettUdsVenForm.getMode(); 
  String key = YEtichettUdsVenForm.getKey(); 
  String errorMessage; 
  boolean requestIsValid = false; 
  boolean leftIsKey = false; 
  boolean conflitPresent = false; 
  String leftClass = ""; 
  try 
  {
     se.initialize(request, response); 
     if(se.begin()) 
     { 
        YEtichettUdsVenForm.outTraceInfo(getClass().getName()); 
        String collectorName = YEtichettUdsVenForm.findBODataCollectorName(); 
				 YEtichettUdsVenBODC = (BODataCollector)Factory.createObject(collectorName); 
        if (YEtichettUdsVenBODC instanceof WebDataCollector) 
            ((WebDataCollector)YEtichettUdsVenBODC).setServletEnvironment(se); 
        YEtichettUdsVenBODC.initialize("YEtichettUdsVen", true, 1); 
        int rcBODC; 
        if (YEtichettUdsVenBODC.getBo() instanceof BatchRunnable) 
          rcBODC = YEtichettUdsVenBODC.initSecurityServices("RUN", mode, true, false, true); 
        else 
          rcBODC = YEtichettUdsVenBODC.initSecurityServices(mode, true, true, true); 
        if (rcBODC == BODataCollector.OK) 
        { 
           requestIsValid = true; 
           YEtichettUdsVenForm.write(out); 
           if(mode != WebForm.NEW) 
              rcBODC = YEtichettUdsVenBODC.retrieve(key); 
           if(rcBODC == BODataCollector.OK) 
           { 
              YEtichettUdsVenForm.setBODataCollector(YEtichettUdsVenBODC); 
              YEtichettUdsVenForm.writeHeadElements(out); 
           // fine blocco XXX  
           // a completamento blocco di codice YYY a fine body con catch e gestione errori 
%> 
<% 
  WebMenuBar menuBar = new com.thera.thermfw.web.WebMenuBar("HM_Array1", "150", "#000000","#000000","#A5B6CE","#E4EAEF","#FFFFFF","#000000"); 
  menuBar.setParent(YEtichettUdsVenForm); 
   request.setAttribute("menuBar", menuBar); 
%> 
<jsp:include page="/it/thera/thip/cs/PrintRunnableMenu.jsp" flush="true"> 
<jsp:param name="partRequest" value="menuBar"/> 
</jsp:include> 
<% 
  menuBar.write(out); 
  menuBar.writeChildren(out); 
%> 
<% 
  WebToolBar myToolBarTB = new com.thera.thermfw.web.WebToolBar("myToolBar", "24", "24", "16", "16", "#f7fbfd","#C8D6E1"); 
  myToolBarTB.setParent(YEtichettUdsVenForm); 
   request.setAttribute("toolBar", myToolBarTB); 
%> 
<jsp:include page="/it/thera/thip/cs/PrintRunnableMenu.jsp" flush="true"> 
<jsp:param name="partRequest" value="toolBar"/> 
</jsp:include> 
<% 
   myToolBarTB.write(out); 
%> 
</head>
<body bottommargin="0" leftmargin="0" onbeforeunload="<%=YEtichettUdsVenForm.getBodyOnBeforeUnload()%>" onload="<%=YEtichettUdsVenForm.getBodyOnLoad()%>" onunload="<%=YEtichettUdsVenForm.getBodyOnUnload()%>" rightmargin="0" topmargin="0"><%
   YEtichettUdsVenForm.writeBodyStartElements(out); 
%> 


<table width="100%" height="100%" cellspacing="0" cellpadding="0">
<tr>
<td style="height:0" valign="top">
<% String hdr = YEtichettUdsVenForm.getCompleteHeader();
 if (hdr != null) { 
   request.setAttribute("dataCollector", YEtichettUdsVenBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= hdr %>" flush="true"/> 
<% } %> 
</td>
</tr>

<tr>
<td valign="top" height="100%">
<form action="<%=YEtichettUdsVenForm.getServlet()%>" method="post" name="YEtichettUdsVenForm" style="height:100%"><%
  YEtichettUdsVenForm.writeFormStartElements(out); 
%>

	<table cellpadding="2" cellspacing="0" height="100%" id="emptyborder" width="100%">
		<tr><td style="height:0"><% menuBar.writeElements(out); %> 
</td></tr>
		<tr><td style="height:0"><% myToolBarTB.writeChildren(out); %> 
</td></tr>
							<tr>
								<td><input id="thReportId" name="thReportId" type="hidden">
								<% 
  WebTextInput YEtichettUdsVenChiaviSelezionati =  
     new com.thera.thermfw.web.WebTextInput("YEtichettUdsVen", "ChiaviSelezionati"); 
  YEtichettUdsVenChiaviSelezionati.setParent(YEtichettUdsVenForm); 
%>
<input class="<%=YEtichettUdsVenChiaviSelezionati.getClassType()%>" id="<%=YEtichettUdsVenChiaviSelezionati.getId()%>" maxlength="<%=YEtichettUdsVenChiaviSelezionati.getMaxLength()%>" name="<%=YEtichettUdsVenChiaviSelezionati.getName()%>" size="<%=YEtichettUdsVenChiaviSelezionati.getSize()%>" type="hidden"><% 
  YEtichettUdsVenChiaviSelezionati.write(out); 
%>
</td>
							</tr>
		<tr><td style="height:0"><% 
  WebErrorList errorList = new com.thera.thermfw.web.WebErrorList(); 
  errorList.setParent(YEtichettUdsVenForm); 
  errorList.write(out); 
%>
<!--<span class="errorlist"></span>--></td></tr>
	</table>

<%
  YEtichettUdsVenForm.writeFormEndElements(out); 
%>
</form></td>
</tr>

<tr>
<td style="height:0">
<% String ftr = YEtichettUdsVenForm.getCompleteFooter();
 if (ftr != null) { 
   request.setAttribute("dataCollector", YEtichettUdsVenBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= ftr %>" flush="true"/> 
<% } %> 
</td>
</tr>
</table>



<%
           // blocco YYY  
           // a completamento blocco di codice XXX in head 
              YEtichettUdsVenForm.writeBodyEndElements(out); 
           } 
           else 
              errors.addAll(0, YEtichettUdsVenBODC.getErrorList().getErrors()); 
        } 
        else 
           errors.addAll(0, YEtichettUdsVenBODC.getErrorList().getErrors()); 
           if(YEtichettUdsVenBODC.getConflict() != null) 
                conflitPresent = true; 
     } 
     else 
        errors.add(new ErrorMessage("BAS0000010")); 
  } 
  catch(NamingException e) { 
     errorMessage = e.getMessage(); 
     errors.add(new ErrorMessage("CBS000025", errorMessage));  } 
  catch(SQLException e) {
     errorMessage = e.getMessage(); 
     errors.add(new ErrorMessage("BAS0000071", errorMessage));  } 
  catch(Throwable e) {
     e.printStackTrace(Trace.excStream);
  }
  finally 
  {
     if(YEtichettUdsVenBODC != null && !YEtichettUdsVenBODC.close(false)) 
        errors.addAll(0, YEtichettUdsVenBODC.getErrorList().getErrors()); 
     try 
     { 
        se.end(); 
     }
     catch(IllegalArgumentException e) { 
        e.printStackTrace(Trace.excStream); 
     } 
     catch(SQLException e) { 
        e.printStackTrace(Trace.excStream); 
     } 
  } 
  if(!errors.isEmpty())
  { 
      if(!conflitPresent)
  { 
     request.setAttribute("ErrorMessages", errors); 
     String errorPage = YEtichettUdsVenForm.getErrorPage(); 
%> 
     <jsp:include page="<%=errorPage%>" flush="true"/> 
<% 
  } 
  else 
  { 
     request.setAttribute("ConflictMessages", YEtichettUdsVenBODC.getConflict()); 
     request.setAttribute("ErrorMessages", errors); 
     String conflictPage = YEtichettUdsVenForm.getConflictPage(); 
%> 
     <jsp:include page="<%=conflictPage%>" flush="true"/> 
<% 
   } 
   } 
%> 
</body>
<% 
  WebScript script_0 =  
   new com.thera.thermfw.web.WebScript(); 
 script_0.setRequest(request); 
 script_0.setSrcAttribute("com/thera/thermfw/batch/PrintBatchRunnable.js"); 
 script_0.setLanguageAttribute("JavaScript1.2"); 
  script_0.write(out); 
%>
<!--<script language="JavaScript1.2" src="com/thera/thermfw/batch/PrintBatchRunnable.js" type="text/javascript"></script>-->
</html>
