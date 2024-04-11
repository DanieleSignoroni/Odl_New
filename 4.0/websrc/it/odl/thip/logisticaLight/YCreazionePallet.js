function YUdsPalletTermOL() {
	document.getElementById("BarcodeScatola").addEventListener("keypress", function(event) {
		if (event.key === "Enter") { // Check if Enter key is pressed
			var barcode = event.target.value;
			var f = document.getElementById('GrigliaDocumentiFrame').contentWindow;
			var url = "/" + webAppPath + "/" + servletPath + "/it.odl.thip.logisticaLight.web.YControlloBarcodeScatolaUds";
			url += "?BarcodeScatola=" + barcode;
			setLocationOnWindow(f, url);
		}
	});
	document.getElementById("form").addEventListener("submit", function(event) {
		event.preventDefault();
	});
	document.getElementById('IdPallet').style.width = ''; //resize del websearchcombbox
}

function manageSparataScatola(barcode) {
	var table = document.getElementById("barcodeTable").getElementsByTagName('tbody')[0];
	var newRow = table.insertRow();

	var cell2 = newRow.insertCell(0);
	cell2.textContent = barcode;

	var cell1 = newRow.insertCell(1);
	var deleteButton = document.createElement("button");
	deleteButton.innerHTML = '<i class="fa fa-trash"></i>';
	deleteButton.setAttribute('type', 'button'); // Set type to button to prevent form submission
	deleteButton.setAttribute('id', barcode);
	deleteButton.setAttribute("onclick", "deleteRow('" + String(barcode) + "')");
	cell1.appendChild(deleteButton);
}

function deleteRow(event) {
	var button = document.getElementById(String(event));
	var row = button.closest('tr');
	if (row != null && row != undefined) {
		row.remove();
	}
}

function onSuccessPallet(idNewPallet) {
	document.getElementById('idNewPallet').value = idNewPallet; //popolo il campo per la stampa
	var table = document.getElementById("barcodeTable");
	var tbody = table.getElementsByTagName("tbody")[0];
	var rows = tbody.getElementsByTagName("tr");
	for (var i = 0; i < rows.length; i++) {
		table.deleteRow(i);
	}
	document.getElementById('msgOnSuccess').innerHTML = 'Creato il pallet : ' + idNewPallet;
	document.getElementById('IdPallet').value = '';
}

function creaPallet() {
	var table = document.getElementById("barcodeTable");
	var tbody = table.getElementsByTagName("tbody")[0];
	var rows = tbody.getElementsByTagName("tr");
	var data = [];
	var tipoPallet = document.getElementById('IdPallet').value;
	// Iterate through each row and collect data
	for (var i = 0; i < rows.length; i++) {
		var cells = rows[i].getElementsByTagName("td");
		var barcode = cells[0].textContent;
		data.push(barcode);
	}

	if (data.length === 0) {
		alert("Non e' stata sparata nessuna riga, per creare il pallet occorre sparare almeno una scatola!");
		return;
	}
	if (tipoPallet == '') {
		alert("E' necessario scegliere un tipo pallet dalla combo!");
		return;
	}
	var f = document.getElementById('GrigliaDocumentiFrame').contentWindow;
	var url = "/" + webAppPath + "/" + servletPath + "/it.odl.thip.logisticaLight.web.YCreazionePalletFormActionAdapter";
	url += "?thAction=CREA_PALLET&Barcodes=" + encodeURI(data);
	url += "&IdPallet=" + tipoPallet;
	setLocationOnWindow(f, url);
}

function stampaPallet() {
	let idNewPallet = document.getElementById('idNewPallet').value;
	if (idNewPallet != undefined && idNewPallet != "") {
		var f = document.getElementById('GrigliaDocumentiFrame').contentWindow;
		var url = "/" + webAppPath + "/" + servletPath + "/it.odl.thip.logisticaLight.web.YCreazionePalletFormActionAdapter";
		url += "?thAction=STAMPA_PALLET&IdPallet=" + encodeURI(idNewPallet);
		setLocationOnWindow(f, url);
	} else {
		window.alert('Non e possibile stampare il pallet, va prima creato!');
	}
}

function misurePallet() {
	var idNewPallet = document.getElementById('idNewPallet').value;
	if (idNewPallet != null && idNewPallet != undefined && idNewPallet != '') {
		document.getElementById('tab1').style.display = 'none';
		document.getElementById('tabMisure').style.display = 'revert';
	} else {
		alert('Prima di inserire le misure va creato il pallet!');
	}
}

function confermaMisure() {
	var idNewPallet = document.getElementById('idNewPallet').value;
	var altezza = document.getElementById('Altezza').value;
	var f = document.getElementById('GrigliaDocumentiFrame').contentWindow;
	var url = "/" + webAppPath + "/" + servletPath + "/it.odl.thip.logisticaLight.web.YCreazionePalletFormActionAdapter";
	url += "?thAction=CONFERMA_MISURE&IdPallet=" + encodeURI(idNewPallet);
	if(altezza.includes(',')){
		altezza = altezza.replace(/,/g, ".");
	}
	url += "&Altezza="+altezza;
	setLocationOnWindow(f, url);
}

function onSuccessConfermaMisure(){
	window.location.reload();
}