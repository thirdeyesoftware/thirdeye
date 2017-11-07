<%
int rand = (int)(Math.random() * 6 + 2);
%>


<csactiondict>
			<script><!--
CSInit[CSInit.length] = new Array(CSILoad,/*CMP*/'button',/*URL*/'/images/inside/insideNavButtonsOFF_01.jpg',/*URL*/'/images/inside/insideNavButtonsON_01.jpg',/*URL*/'','');
CSInit[CSInit.length] = new Array(CSILoad,/*CMP*/'button2',/*URL*/'/images/inside/insideNavButtonsOFF_02.jpg',/*URL*/'/images/inside/insideNavButtonsON_02.jpg',/*URL*/'','');
CSInit[CSInit.length] = new Array(CSILoad,/*CMP*/'button3',/*URL*/'/images/inside/insideNavButtonsOFF_03.jpg',/*URL*/'/images/inside/insideNavButtonsON_03.jpg',/*URL*/'','');
CSInit[CSInit.length] = new Array(CSILoad,/*CMP*/'button4',/*URL*/'/images/inside/insideNavButtonsOFF_04.jpg',/*URL*/'/images/inside/insideNavButtonsON_04.jpg',/*URL*/'','');
CSInit[CSInit.length] = new Array(CSILoad,/*CMP*/'button5',/*URL*/'/images/inside/insideNavButtonsOFF_05.jpg',/*URL*/'/images/inside/insideNavButtonsON_05.jpg',/*URL*/'','');
CSInit[CSInit.length] = new Array(CSILoad,/*CMP*/'button6',/*URL*/'/images/inside/insideNavButtonsOFF_06.jpg',/*URL*/'/images/inside/insideNavButtonsON_06.jpg',/*URL*/'','');

// --></script>
		</csactiondict>
					
					<a href="#"><img src="/images/inside/inside_02.jpg" width="162" height="29" border="0"></a><br>
					<img src="/images/inside/inside_04.jpg" width="162" height="31" border="0"><br>
					<csobj w="162" h="18" t="Button" ht="/images/inside/insideNavButtonsON_01.jpg"><a href="/jsp/technology.jsp" onmouseover="return CSIShow(/*CMP*/'button',1)" onmouseout="return CSIShow(/*CMP*/'button',0)" onclick="return CSButtonReturn()"><img src="/images/inside/insideNavButtonsOFF_01.jpg" width="162" height="18" name="button" border="0"></a></csobj><br>
					<csobj w="162" h="21" t="Button" ht="/images/inside/insideNavButtonsON_02.jpg"><a href="/jsp/services.jsp" onmouseover="return CSIShow(/*CMP*/'button2',1)" onmouseout="return CSIShow(/*CMP*/'button2',0)" onclick="return CSButtonReturn()"><img src="/images/inside/insideNavButtonsOFF_02.jpg" width="162" height="21" name="button2" border="0"></a></csobj><br>
					<csobj w="162" h="19" t="Button" ht="/images/inside/insideNavButtonsON_03.jpg"><a href="/jsp/products.jsp" onmouseover="return CSIShow(/*CMP*/'button3',1)" onmouseout="return CSIShow(/*CMP*/'button3',0)" onclick="return CSButtonReturn()"><img src="/images/inside/insideNavButtonsOFF_03.jpg" width="162" height="19" name="button3" border="0"></a></csobj><br>
					<csobj w="162" h="20" t="Button" ht="/images/inside/insideNavButtonsON_04.jpg"><a href="/jsp/contact.jsp" onmouseover="return CSIShow(/*CMP*/'button4',1)" onmouseout="return CSIShow(/*CMP*/'button4',0)" onclick="return CSButtonReturn()"><img src="/images/inside/insideNavButtonsOFF_04.jpg" width="162" height="20" name="button4" border="0"></a></csobj><br>
					<csobj w="162" h="20" t="Button" ht="/images/inside/insideNavButtonsON_05.jpg"><a href="/jsp/referLocation.jsp" onmouseover="return CSIShow(/*CMP*/'button5',1)" onmouseout="return CSIShow(/*CMP*/'button5',0)" onclick="return CSButtonReturn()"><img src="/images/inside/insideNavButtonsOFF_05.jpg" width="162" height="20" name="button5" border="0"></a></csobj><br>
					<csobj w="162" h="19" t="Button" ht="/images/inside/insideNavButtonsON_06.jpg"><a href="https://www.iobeam.com/user/signup.jsp" onmouseover="return CSIShow(/*CMP*/'button6',1)" onmouseout="return CSIShow(/*CMP*/'button6',0)" onclick="return CSButtonReturn()"><img src="/images/inside/insideNavButtonsOFF_06.jpg" width="162" height="19" name="button6" border="0"></a></csobj><br>
					<img src="/images/inside/inside_07.jpg" width="162" height="34" border="0"><br>
<% if (UserSessionHelper.getUserSession(request.getSession()) == null) { %>
<a href="/index.jsp"><img src="/images/inside/inside_08.jpg" width="162" height="29" border="0"></a><br>
<% } %>
					<img src="/images/inside/inside_09.jpg" width="162" height="22" border="0"><br>
					<a href="/jsp/referLocation.jsp"><img src="/images/inside/insideNavPhoto0<%=rand%>.jpg" width="162" height="189" border="0"></a><BR>
					

