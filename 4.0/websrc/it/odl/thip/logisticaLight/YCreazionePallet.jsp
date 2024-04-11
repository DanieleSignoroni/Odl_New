<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"
                      "file:///K:/Thip/5.0.0/websrcsvil/dtd/xhtml1-transitional.dtd">
<html>
<!-- WIZGEN Therm 2.0.0 as Form - multiBrowserGen = true -->
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
  BODataCollector YUdsPalletTermBODC = null; 
  List errors = new ArrayList(); 
  WebJSTypeList jsList = new WebJSTypeList(); 
  WebForm YUdsPalletTermForm =  
     new com.thera.thermfw.web.WebForm(request, response, "YUdsPalletTermForm", "YUdsPalletTerm", null, "it.odl.thip.logisticaLight.web.YCreazionePalletFormActionAdapter", false, false, false, false, true, true, null, 1, true, "it/odl/thip/logisticaLight/YCreazionePallet.js"); 
  YUdsPalletTermForm.setServletEnvironment(se); 
  YUdsPalletTermForm.setJSTypeList(jsList); 
  YUdsPalletTermForm.setHeader(null); 
  YUdsPalletTermForm.setFooter(null); 
  YUdsPalletTermForm.setDeniedAttributeModeStr("hideNone"); 
  int mode = YUdsPalletTermForm.getMode(); 
  String key = YUdsPalletTermForm.getKey(); 
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
        YUdsPalletTermForm.outTraceInfo(getClass().getName()); 
        String collectorName = YUdsPalletTermForm.findBODataCollectorName(); 
                YUdsPalletTermBODC = (BODataCollector)Factory.createObject(collectorName); 
        if (YUdsPalletTermBODC instanceof WebDataCollector) 
            ((WebDataCollector)YUdsPalletTermBODC).setServletEnvironment(se); 
        YUdsPalletTermBODC.initialize("YUdsPalletTerm", true, 1); 
        YUdsPalletTermForm.setBODataCollector(YUdsPalletTermBODC); 
        int rcBODC = YUdsPalletTermForm.initSecurityServices(); 
        mode = YUdsPalletTermForm.getMode(); 
        if (rcBODC == BODataCollector.OK) 
        { 
           requestIsValid = true; 
           YUdsPalletTermForm.write(out); 
           if(mode != WebForm.NEW) 
              rcBODC = YUdsPalletTermBODC.retrieve(key); 
           if(rcBODC == BODataCollector.OK) 
           { 
              YUdsPalletTermForm.writeHeadElements(out); 
           // fine blocco XXX  
           // a completamento blocco di codice YYY a fine body con catch e gestione errori 
%> 

<meta charset="utf-8">
<meta content="IE=edge" http-equiv="X-UA-Compatible">
<meta content="width=device-width, initial-scale=1" name="viewport">
<% 
  WebLink link_0 =  
   new com.thera.thermfw.web.WebLink(); 
 link_0.setHttpServletRequest(request); 
 link_0.setHRefAttribute("it/thera/thip/logisticaLight/css/extra/dataTables.bootstrap.min.css"); 
 link_0.setRelAttribute("stylesheet"); 
 link_0.setTypeAttribute("text/css"); 
  link_0.write(out); 
%>
<!--<link href="it/thera/thip/logisticaLight/css/extra/dataTables.bootstrap.min.css" rel="stylesheet" type="text/css">-->
<!-- DataTables Responsive CSS -->
<% 
  WebLink link_1 =  
   new com.thera.thermfw.web.WebLink(); 
 link_1.setHttpServletRequest(request); 
 link_1.setHRefAttribute("it/thera/thip/logisticaLight/css/extra/responsive.dataTables.min.css"); 
 link_1.setRelAttribute("stylesheet"); 
 link_1.setTypeAttribute("text/css"); 
  link_1.write(out); 
%>
<!--<link href="it/thera/thip/logisticaLight/css/extra/responsive.dataTables.min.css" rel="stylesheet" type="text/css">-->
<% 
  WebLink link_2 =  
   new com.thera.thermfw.web.WebLink(); 
 link_2.setHttpServletRequest(request); 
 link_2.setHRefAttribute("it/thera/thip/logisticaLight/css/extra/buttons.dataTables.min.css"); 
 link_2.setRelAttribute("stylesheet"); 
 link_2.setTypeAttribute("text/css"); 
  link_2.write(out); 
%>
<!--<link href="it/thera/thip/logisticaLight/css/extra/buttons.dataTables.min.css" rel="stylesheet" type="text/css">-->
<% 
  WebLink link_3 =  
   new com.thera.thermfw.web.WebLink(); 
 link_3.setHttpServletRequest(request); 
 link_3.setHRefAttribute("it/thera/thip/logisticaLight/css/extra/datatables.min.css"); 
 link_3.setRelAttribute("stylesheet"); 
 link_3.setTypeAttribute("text/css"); 
  link_3.write(out); 
%>
<!--<link href="it/thera/thip/logisticaLight/css/extra/datatables.min.css" rel="stylesheet" type="text/css">-->
<!-- Bootstrap Core CSS -->
<% 
  WebLink link_4 =  
   new com.thera.thermfw.web.WebLink(); 
 link_4.setHttpServletRequest(request); 
 link_4.setHRefAttribute("it/thera/thip/logisticaLight/css/bootstrap/dist/css/bootstrap.min.css"); 
 link_4.setRelAttribute("stylesheet"); 
 link_4.setTypeAttribute("text/css"); 
  link_4.write(out); 
%>
<!--<link href="it/thera/thip/logisticaLight/css/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet" type="text/css">-->
<!-- Custom CSS -->
<% 
  WebLink link_5 =  
   new com.thera.thermfw.web.WebLink(); 
 link_5.setHttpServletRequest(request); 
 link_5.setHRefAttribute("it/thera/thip/logisticaLight/css/sb-admin-2.css"); 
 link_5.setRelAttribute("stylesheet"); 
 link_5.setTypeAttribute("text/css"); 
  link_5.write(out); 
%>
<!--<link href="it/thera/thip/logisticaLight/css/sb-admin-2.css" rel="stylesheet" type="text/css">-->
<!-- Theme style -->
<% 
  WebLink link_6 =  
   new com.thera.thermfw.web.WebLink(); 
 link_6.setHttpServletRequest(request); 
 link_6.setHRefAttribute("it/thera/thip/logisticaLight/css/AdminLTE.min.css"); 
 link_6.setRelAttribute("stylesheet"); 
 link_6.setTypeAttribute("text/css"); 
  link_6.write(out); 
%>
<!--<link href="it/thera/thip/logisticaLight/css/AdminLTE.min.css" rel="stylesheet" type="text/css">-->
<!-- JS -->
<% 
  WebScript script_0 =  
   new com.thera.thermfw.web.WebScript(); 
 script_0.setRequest(request); 
 script_0.setSrcAttribute("it/thera/thip/logisticaLight/ResponsiveSpedizioneComponenti.js"); 
 script_0.setLanguageAttribute(null); 
  script_0.write(out); 
%>
<!--<script src="it/thera/thip/logisticaLight/ResponsiveSpedizioneComponenti.js"></script>-->
<% 
  WebScript script_1 =  
   new com.thera.thermfw.web.WebScript(); 
 script_1.setRequest(request); 
 script_1.setSrcAttribute("it/thera/thip/logisticaLight/js/extra/jquery.min.js"); 
 script_1.setLanguageAttribute(null); 
  script_1.write(out); 
%>
<!--<script src="it/thera/thip/logisticaLight/js/extra/jquery.min.js"></script>-->
<% 
  WebScript script_2 =  
   new com.thera.thermfw.web.WebScript(); 
 script_2.setRequest(request); 
 script_2.setSrcAttribute("it/thera/thip/logisticaLight/js/extra/bootstrap.min.js"); 
 script_2.setLanguageAttribute(null); 
  script_2.write(out); 
%>
<!--<script src="it/thera/thip/logisticaLight/js/extra/bootstrap.min.js"></script>-->
<% 
  WebScript script_3 =  
   new com.thera.thermfw.web.WebScript(); 
 script_3.setRequest(request); 
 script_3.setSrcAttribute("it/thera/thip/logisticaLight/UtilsUbicazioniBarcode.js"); 
 script_3.setLanguageAttribute(null); 
  script_3.write(out); 
%>
<!--<script src="it/thera/thip/logisticaLight/UtilsUbicazioniBarcode.js"></script>-->
<!-- Custom Fonts -->
<% 
  WebLink link_7 =  
   new com.thera.thermfw.web.WebLink(); 
 link_7.setHttpServletRequest(request); 
 link_7.setHRefAttribute("it/thera/thip/logisticaLight/css/font-awesome/css/font-awesome.min.css"); 
 link_7.setRelAttribute("stylesheet"); 
 link_7.setTypeAttribute("text/css"); 
  link_7.write(out); 
%>
<!--<link href="it/thera/thip/logisticaLight/css/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">-->
<style type="text/css">
#sfondo {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	z-index: -1;
}

.container {
	padding-left: 0px;
	padding-right: 0px
}

form {
	background: rgb(232, 232, 232);
	height: 50%;
}

body {
	background-color: #FFFFFF;
	height: 100%;
	width: 100%;
	margin: 0;
	padding: 0;
}

a {
	color: #FFFFFF;
	text-decoration: none;
	display: inline-block;
	padding: 8px 16px;
}

a:hover {
	background-color: #ddd;
	color: black;
}

.previous {
	background-color: #f1f1f1;
	color: black;
}

.next {
	background-color: #f1f1f1;
	color: white;
}

.round {
	border-radius: 50%;
}

.icon-bar {
	width: 100%;
	/* Full-width */
	background-color: #555;
	/* Dark-grey background */
	overflow: auto;
	/* Overflow due to float */
}

.icon-bar a {
	float: left;
	/* Float links side by side */
	text-align: center;
	/* Center-align text */
	width: 20%;
	/* Equal width (5 icons with 20% width each = 100%) */
	padding: 12px 0;
	/* Some top and bottom padding */
	transition: all 0.3s ease;
	/* Add transition for hover effects */
	color: white;
	/* White text color */
	font-size: 36px;
	/* Increased font size */
}

.icon-bar a:hover {
	background-color: #000;
	/* Add a hover color */
}

.active {
	background-color: #4CAF50;
	/* Add an active/current color */
}

.panel-heading {
	font-size: 11pt;
}

td {
	/* 			border: 1px solid black; */
	padding: 3px;
}

.btn {
	background-color: #666666;
	color: #FFFFFF;
	font-family: Arial, Helvetica, sans-serif;
	padding: 0px;
	font-size: 15pt;
	cursor: pointer;
	font-weight: bolder;
	border-radius: 5px;
	height: fit-content;
	width: fit-content;
}
</style>
</head>
<body bottommargin="0" leftmargin="0" onbeforeunload="<%=YUdsPalletTermForm.getBodyOnBeforeUnload()%>" onload="<%=YUdsPalletTermForm.getBodyOnLoad()%>" onunload="<%=YUdsPalletTermForm.getBodyOnUnload()%>" rightmargin="0" topmargin="0"><%
   YUdsPalletTermForm.writeBodyStartElements(out); 
%> 

	<div class="navbar-fixed-top" id="menubarResp">
		<!--FIX 38988-->
		<div class="container">
			<nav class="navbar navbar-default" style="margin-bottom: 0px">
				<div class="container-fluid">
					<div class="navbar-header">
						<button class="navbar-toggle collapsed" data-target="#navbar2" data-toggle="collapse" type="button">
							<span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
						</button>
						<a class="navbar-brand" href="javascript:tornaAlPaginaMenu()"><img src="it/thera/thip/cs/images/LogoPanteraHeader.gif"> </a>
					</div>
					<div class="navbar-collapse collapse" id="navbar2">
						<ul class="nav navbar-nav navbar-right" style="font-size: 11pt">
							<li><a href="javascript:tornaAlPaginaMenu()">Home</a>
							<li><a href="it/thera/thip/logisticaLight/Login2.jsp">Esci</a>
						</ul>
					</div>
					<!--/.nav-collapse -->
				</div>
				<!--/.container-fluid -->
			</nav>
		</div>
		<div class="container">
			<h1 class="alert bg-aqua" style="padding: 0px; margin: 0px;">
				<!-- <a onclick="tornaAlPaginaMenu()"><i class="fa fa-backward"></i></a>     <a onclick="tornaAlPaginaMenu()"><i class="fa fa-home"></i></a>     <a onclick="javascript:goNext()"><i class="fa fa-forward"></i></a> -->
				<a onclick="tornaAlPaginaMenu()"><i class="fa fa-backward"></i></a> <a onclick="tornaAlPaginaMenu()"><i class="fa fa-home"></i></a> <a onclick="tornaAlPaginaMenu()"><i class="fa fa-forward"></i></a>
			</h1>
		</div>
	</div>
	<table width="100%" height="100%" cellspacing="0" cellpadding="0">
<tr>
<td style="height:0" valign="top">
<% String hdr = YUdsPalletTermForm.getCompleteHeader();
 if (hdr != null) { 
   request.setAttribute("dataCollector", YUdsPalletTermBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= hdr %>" flush="true"/> 
<% } %> 
</td>
</tr>

<tr>
<td valign="top" height="100%">
<form action="<%=YUdsPalletTermForm.getServlet()%>" id="form" method="post" name="form" style="height:100%"><%
  YUdsPalletTermForm.writeFormStartElements(out); 
%>

		<div class="container" id="tab1" style="margin-top:5rem;">
			<table cellpadding="2" cellspacing="0" height="25%" id="emptyborder">
				<tr valign="top">
					<td><br></td>
				</tr>
				<tr valign="top">
					<td><br></td>
				</tr>
				<tr valign="top">
					<td><br></td>
				</tr>
				<tr valign="top">
					<td><br></td>
				</tr>
				<tr valign="top">
					<td><br></td>
				</tr>
				<tr style="display:flex;">
					<td><a onclick="creaPallet()"><i aria-hidden="true" class="fa fa-light fa-arrow-right fa-3x" style="color:black;"></i></a></td>
					<td><a onclick="stampaPallet()"><i aria-hidden="true" class="fa fa-light fa-print fa-3x" style="color:black;"></i></a></td>
					<td><a onclick="misurePallet()"><i aria-hidden="true" class="fa fa-light fa-pencil fa-3x" style="color:black;"></i></a></td>
				</tr>
				<tr style="display:flex;">
                    <td valign="top">
                      <%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "YUdsPalletTerm", "IdPallet", null); 
   label.setParent(YUdsPalletTermForm); 
%><label class="<%=label.getClassType()%>" for="TipoPallet"><%label.write(out);%></label><%}%>
                    </td>
                    <td valign="top">
                      <% 
  WebSearchComboBox YUdsPalletTermTipoPallet =  
     new com.thera.thermfw.web.WebSearchComboBox("YUdsPalletTerm", "TipoPallet", null, 2, null, false, null); 
  YUdsPalletTermTipoPallet.setParent(YUdsPalletTermForm); 
  YUdsPalletTermTipoPallet.write(out); 
%>
<!--<span class="searchcombobox" id="TipoPallet"></span>-->
                    </td>
                  </tr>
				<tr style="display:flex;" valign="top">
					<td><%{  WebLabelCompound label = new com.thera.thermfw.web.WebLabelCompound(null, null, "YUdsPalletTerm", "BarcodeScatola", null); 
   label.setParent(YUdsPalletTermForm); 
%><label class="<%=label.getClassType()%>" for="BarcodeScatola"><%label.write(out);%></label><%}%></td>
					<td><% 
  WebTextInput YUdsPalletTermBarcodeScatola =  
     new com.thera.thermfw.web.WebTextInput("YUdsPalletTerm", "BarcodeScatola"); 
  YUdsPalletTermBarcodeScatola.setParent(YUdsPalletTermForm); 
%>
<input class="<%=YUdsPalletTermBarcodeScatola.getClassType()%>" id="<%=YUdsPalletTermBarcodeScatola.getId()%>" maxlength="<%=YUdsPalletTermBarcodeScatola.getMaxLength()%>" name="<%=YUdsPalletTermBarcodeScatola.getName()%>" size="<%=YUdsPalletTermBarcodeScatola.getSize()%>" style="width:200px"><% 
  YUdsPalletTermBarcodeScatola.write(out); 
%>
</td>
				</tr>
				<tr>
					<td colspan="2">
						<table class="table table-striped" id="barcodeTable">
  							<tbody>
  						 <!-- Table body will be dynamically populated -->
  							</tbody>
						</table>
					</td>
				</tr>
				<tr>
					<td><input id="idNewPallet" name="idNewPallet" type="hidden"></td>
				</tr>
				<tr>
					<td>
						<p id="msgOnSuccess"></p>
					</td>
				</tr>
				<tr height="100%">
					<td colspan="2" height="100%" valign="top"><iframe class="FrameBackgroundPrlVenDoc" frameborder="0" id="GrigliaDocumentiFrame" marginheight="0" marginwidth="0" name="GrigliaDocumentiFrame" src> </iframe></td>
				</tr>
			</table>
		</div>
		<div class="container" id="tabMisure" style="margin-top:5rem;display:none;">
			<table cellpadding="2" cellspacing="0" height="25%" id="emptyborder">
				<tr valign="top">
					<td><br></td>
				</tr>
				<tr valign="top">
					<td><br></td>
				</tr>
				<tr valign="top">
					<td><br></td>
				</tr>
				<tr style="display:flex;">
					<td><a onclick="confermaMisure()"><i aria-hidden="true" class="fa fa-light fa-arrow-right fa-3x" style="color:black;"></i></a></td>
				</tr>
				<tr style="display:flex;" valign="top">
					<td><label>Altezza</label></td>
					<td><% 
  WebTextInput YUdsPalletTermAltezza =  
     new com.thera.thermfw.web.WebTextInput("YUdsPalletTerm", "Altezza"); 
  YUdsPalletTermAltezza.setParent(YUdsPalletTermForm); 
%>
<input class="<%=YUdsPalletTermAltezza.getClassType()%>" id="<%=YUdsPalletTermAltezza.getId()%>" maxlength="<%=YUdsPalletTermAltezza.getMaxLength()%>" name="<%=YUdsPalletTermAltezza.getName()%>" size="<%=YUdsPalletTermAltezza.getSize()%>" style="width:200px"><% 
  YUdsPalletTermAltezza.write(out); 
%>
</td>
				</tr>
			</table>
		</div>
	<%
  YUdsPalletTermForm.writeFormEndElements(out); 
%>
</form></td>
</tr>

<tr>
<td style="height:0">
<% String ftr = YUdsPalletTermForm.getCompleteFooter();
 if (ftr != null) { 
   request.setAttribute("dataCollector", YUdsPalletTermBODC); 
   request.setAttribute("servletEnvironment", se); %>
  <jsp:include page="<%= ftr %>" flush="true"/> 
<% } %> 
</td>
</tr>
</table>


<%
           // blocco YYY  
           // a completamento blocco di codice XXX in head 
              YUdsPalletTermForm.writeBodyEndElements(out); 
           } 
           else 
              errors.addAll(0, YUdsPalletTermBODC.getErrorList().getErrors()); 
        } 
        else 
           errors.addAll(0, YUdsPalletTermBODC.getErrorList().getErrors()); 
           if(YUdsPalletTermBODC.getConflict() != null) 
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
     if(YUdsPalletTermBODC != null && !YUdsPalletTermBODC.close(false)) 
        errors.addAll(0, YUdsPalletTermBODC.getErrorList().getErrors()); 
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
     String errorPage = YUdsPalletTermForm.getErrorPage(); 
%> 
     <jsp:include page="<%=errorPage%>" flush="true"/> 
<% 
  } 
  else 
  { 
     request.setAttribute("ConflictMessages", YUdsPalletTermBODC.getConflict()); 
     request.setAttribute("ErrorMessages", errors); 
     String conflictPage = YUdsPalletTermForm.getConflictPage(); 
%> 
     <jsp:include page="<%=conflictPage%>" flush="true"/> 
<% 
   } 
   } 
%> 
</body>
</html>
