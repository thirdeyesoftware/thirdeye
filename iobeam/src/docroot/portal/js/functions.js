		<csscriptdict>
			<script><!--
CSInit = new Array;
function CSScriptInit() {
if(typeof(skipPage) != "undefined") { if(skipPage) return; }
idxArray = new Array;
for(var i=0;i<CSInit.length;i++)
	idxArray[i] = i;
CSAction2(CSInit, idxArray);}
CSAg = window.navigator.userAgent; CSBVers = parseInt(CSAg.charAt(CSAg.indexOf("/")+1),10);
function IsIE() { return CSAg.indexOf("MSIE") > 0;}
function CSIEStyl(s) { return document.all.tags("div")[s].style; }
function CSNSStyl(s) { return CSFindElement(s,0); }
function CSFindElement(n,ly) { if (CSBVers < 4) return document[n];
	var curDoc = ly ? ly.document : document; var elem = curDoc[n];
	if (!elem) { for (var i=0;i<curDoc.layers.length;i++) {
		elem = CSFindElement(n,curDoc.layers[i]); if (elem) return elem; }}
	return elem;
}

function CSClickReturn () {
	var bAgent = window.navigator.userAgent; 
	var bAppName = window.navigator.appName;
	if ((bAppName.indexOf("Explorer") >= 0) && (bAgent.indexOf("Mozilla/3") >= 0) && (bAgent.indexOf("Mac") >= 0))
		return true; // dont follow link
	else return false; // dont follow link
}

function CSButtonReturn () {
	var bAgent = window.navigator.userAgent; 
	var bAppName = window.navigator.appName;
	if ((bAppName.indexOf("Explorer") >= 0) && (bAgent.indexOf("Mozilla/3") >= 0) && (bAgent.indexOf("Mac") >= 0))
		return false; // follow link
	else return true; // follow link
}

CSIm = new Object();
function CSIShow(n,i) {
	if (document.images) {
		if (CSIm[n]) {
			var img = (!IsIE()) ? CSFindElement(n,0) : document[n];
			if (img && typeof(CSIm[n][i].src) != "undefined") {img.src = CSIm[n][i].src;}
			if(i != 0)
				self.status = CSIm[n][3];
			else
				self.status = " ";
			return true;
		}
	}
	return false;
}
function CSILoad(action) {
	im = action[1];
	if (document.images) {
		CSIm[im] = new Object();
		for (var i=2;i<5;i++) {
			if (action[i] != '') { CSIm[im][i-2] = new Image(); CSIm[im][i-2].src = action[i]; }
			else CSIm[im][i-2] = 0;
		}
		CSIm[im][3] = action[5];
	}
}
CSStopExecution = false;
function CSAction(array) { 
	return CSAction2(CSAct, array);
}
function CSAction2(fct, array) { 
	var result;
	for (var i=0;i<array.length;i++) {
		if(CSStopExecution) return false; 
		var actArray = fct[array[i]];
		if (actArray == null) return false;
		var tempArray = new Array;
		for(var j=1;j<actArray.length;j++) {
			if((actArray[j] != null) && (typeof(actArray[j]) == "object") && (actArray[j].length == 2)) {
				if(actArray[j][0] == "VAR") {
					tempArray[j] = CSStateArray[actArray[j][1]];
				}
				else {
					if(actArray[j][0] == "ACT") {
						tempArray[j] = CSAction(new Array(new String(actArray[j][1])));
					}
				else
					tempArray[j] = actArray[j];
				}
			}
			else
				tempArray[j] = actArray[j];
		}			
		result = actArray[0](tempArray);
	}
	return result;
}
CSAct = new Object;


// --></script>
		</csscriptdict>

