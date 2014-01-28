var host = '147.83.7.157';
var API_BASE_URL = "http://"+host+":8080/futbol-api/campeonato/";
var API_BASE_URL_USER = "http://"+host+":8080/futbol-api/users/";
var pass;
var user;
var idCampeonato;
var nombreCampeonato;
var seljornada;
var idPartido;

$(document).ready(function(e){
	gestioncookie();
	getList();
});

function gestioncookie(){
	user=$.cookie('usuario');
	pass=$.cookie('password');
	if(user!="admin"){
		window.location="../index.html";
	}else{
		var usuario = user;
		var htmlString = '<ul class="nav navbar-nav navbar-right navbar-user"><li class="dropdown user-dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"> <i class="fa fa-user"></i>&nbsp;'+usuario;
		htmlString += '<b class="caret"></b></a><ul class="dropdown-menu"><a href="#"><li onClick="javascript:cerrarsesion()"><i class="fa fa-power-off"></i>&nbsp; Salir</a></li></a></ul></li></ul>';
		$('#usuario').html(htmlString);	
	}
}
function cerrarsesion(){
	alertify.alert("Estás a punto de cerrar sesión. Continuar?", function (e) {
		if (e) {
      window.location="../index.html";
  }
});
}

function getList(){
	var url = API_BASE_URL + "?offset=0&length=999";
	$.ajax({
		url : url,
		async: false, 
		type : 'GET',
		crossDomain : true,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			var htmlString = '<label>Campeonato: &nbsp;</label><select onchange="javascript:selCampe();" id="campeshow">';
			$.each(response.campeonatos, function(i,v){
				htmlString += '<option value="'+v.idcampeonatos+'">'+v.nombre+'</option>';
			})
			htmlString +=  '</select>&nbsp;<div id="jorshow"></div>';			
			$('#show').html(htmlString);
			$("#campeshow").prop("selectedIndex", -1);	
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}

function selCampe(){
	var htmlString= '';
	var idcamp = $('#campeshow').val();
	idCampeonato = idcamp;
	var neq = numequiposcompeti(idcamp);
	if (neq!=0){	
		htmlString += '<label>Jornada: &nbsp;</label><select onchange="javascript:showPart();" id="jornadashow">';
		for (i=0; i<neq*2; ++i){
			var jornada = i+1;
			htmlString += '<option value="'+jornada+'">'+jornada+'</option>';
		}
		htmlString +=  '</select>&nbsp;<div id="divart"></div>';
		$('#jorshow').html(htmlString);	
		$("#jornadashow").prop("selectedIndex", -1);
	}else{
		htmlString += '<br>No hay equipos en esta competición';
		$('#jorshow').html(htmlString);	
	}
}

function numequiposcompeti(idcamp){
	var url = API_BASE_URL + idcamp + "/equipos?offset=0&length=100";
	var contador = 0;
	$.ajax({
		url : url,
		async: false, 
		type : 'GET',
		crossDomain : true,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			$.each(response.equipos, function(i,v){
				contador++;
			})			
		},
		error : function(jqXHR, options, error) {}
	});
	return contador;
}

function showPart(){
	var jornada = $('#jornadashow').val();
	var url = API_BASE_URL+idCampeonato+"/calendario?offset=0&length=99&shjornada="+jornada;
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			var contador = 0;
			var htmlString = '<label>Partido: &nbsp;</label><select onchange="javascript:showTODO();" id="partidoshow">';
			$.each(response.calendarios, function(i,v){
				htmlString += '<option value="'+v.idPartido+'">'+v.equipoA+' - '+v.equipoB+' : '+v.fecha+'</option>';
				contador++;
			})
			htmlString +=  '</select>&nbsp;</div></div></div></div><div id="divart2"></div>';
			if (contador!=0){
				$('#divart').html(htmlString);
				$("#partidoshow").prop("selectedIndex", -1);
			}else{
				var htmlString2 = '<br>No hay partidos asignados a esa jornada.';
				$('#divart').html(htmlString2);
			}
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});

}

function showTODO(){
	var idpart = $('#partidoshow').val();
	idPartido = idpart;
	var htmlString = '<br><br><div class="row"><div class="col-lg-6"><div class="panel panel-success"><div class="panel-heading"><h3 class="panel-title">Retransmisión</h3></div><div class="panel-body" id="showretra">Panel content</div><div class="panel-footer"><div id="showAddRetra"></div></div></div></div>';
	htmlString += '<div class="col-lg-6"><div class="panel panel-info"><div class="panel-heading"><h3 class="panel-title">Comentarios</h3></div><div class="panel-body" id="showcoments">Panel content</div><div class="panel-footer"><div id="showAddcoment"></div></div></div></div></div>';
	$('#divart2').html(htmlString);
	showRetra();
	var htmlRETRA = '<form role="form" action="javascript:addRet()"><div class="form-group"><label>Tiempo:</label><input type="number" min="1" max="125" style="width:100px;" class="form-control" id="addTiempo" required /><br><label>Texto:</label><input class="form-control" maxlength="35" id="addTexto" required /><br><button type="button submit" class="btn btn-success pull-right" >Enviar</button></div></form>';
	$('#showAddRetra').html(htmlRETRA);
	showCom();
	var htmlCOMENT = '<form role="form" action="javascript:addCom()"><div class="form-group"><label>Comentario:</label><input class="form-control" maxlength="35" id="addComent" required /><br><button type="button submit" class="btn btn-primary pull-right">Enviar</button></div></form>';
	$('#showAddcoment').html(htmlCOMENT);
}


function showCom(){
	var htmlString = '';
	var url = API_BASE_URL + idCampeonato + '/calendario/' + idPartido + '/comentarios?offset=0&length=100';
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			htmlString += '<div class="scroll"><div class="table-responsive"><table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th class="header text-center" style="vertical-align:middle;"><span class="fa fa-bullhorn"></span></th><th class="header text-center" style="vertical-align:middle;"><span class="fa fa-comment"></span></th></tr></thead><tbody>';	
			var contador = 0;
			$.each(response.comentarios, function(i,v){
				htmlString += '<tr><td class="text-center" style="vertical-align:middle;">'+v.idUsuario+'</td><td class="text-center" style="vertical-align:middle;">'+v.texto+'</td><td class="text-center" style="vertical-align:middle;" onClick="delCom('+v.idComentario+')">X</td></tr>';
				contador++;
			})
			htmlString += '</tbody></table></div></div>';
			if (contador != 0){
				$('#showcoments').html(htmlString);
			}else{
				var htmlString2 = 'Sin contenido aún.';
				$('#showcoments').html(htmlString2);
			}
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}
function delCom(idcom){
	BootstrapDialog.confirm('Estas seguro que deseas eliminar el comentario seleccionado?', function(result){
		if(result) {
			var url = API_BASE_URL + idCampeonato + '/calendario/' + idPartido + '/comentarios/' + idcom;
			$.ajax({
				url : url,
				type : 'DELETE',
				crossDomain : true,
				beforeSend: function (request)
				{
					request.withCredentials = true;
					request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
				},
				success : function(data, status, jqxhr) {
					showTODO();
					alertify.log("Notification", "error", 5);
					alertify.success("Operación completada correctamente");
				},
				error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
				alertify.error("No se ha podido completar la acción");}
			});
		}
	});
}

function showRetra(){
	var htmlString = '';
	var url = API_BASE_URL + idCampeonato + '/calendario/' + idPartido + '/retra?offset=0&length=100';
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			var contador = 0;
			htmlString += '<div class="scroll"><div class="table-responsive"><table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th class="header text-center" style="vertical-align:middle;" ><span class="fa fa-bullhorn"></span></th><th class="header text-center" style="vertical-align:middle;"><span class="fa fa-comment"></span></th></tr></thead><tbody>';	
			$.each(response.retrans, function(i,v){
				htmlString += '<tr><td class="text-center" style="vertical-align:middle;">'+v.tiempo+'</td><td class="text-center" style="vertical-align:middle;">'+v.texto+'</td><td class="text-center" style="vertical-align:middle;" onClick="delRet('+v.id+')">X</td></tr>';
				contador++;
			})
			htmlString += '</tbody></table></div></div>';
			if (contador != 0){
				$('#showretra').html(htmlString);
			}else{
				var htmlString2 = 'Sin contenido aún.';
				$('#showretra').html(htmlString2);
			}
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}

function delRet(idret){
	BootstrapDialog.confirm('Estas seguro que deseas eliminar la retransmisión seleccionada?', function(result){
		if(result) {
			var url = API_BASE_URL + idCampeonato + '/calendario/' + idPartido + '/retra/' + idret;
			$.ajax({
				url : url,
				type : 'DELETE',
				crossDomain : true,
				beforeSend: function (request)
				{
					request.withCredentials = true;
					request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
				},
				success : function(data, status, jqxhr) {
					showTODO();
					alertify.log("Notification", "error", 5);
					alertify.success("Operación completada correctamente");
				},
				error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
				alertify.error("No se ha podido completar la acción");}
			});
		}
	});
}

function addRet(){
	var tiempo = $('#addTiempo').val();
	var texto = $('#addTexto').val();
	var url = API_BASE_URL + idCampeonato + '/calendario/' + idPartido + '/retra';
	var datos = '{"texto": "'+texto+'","tiempo": "'+tiempo+'"}';
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		data: datos,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
			request.setRequestHeader("Content-Type", "application/vnd.futbol.api.retra+json");
			request.setRequestHeader("Accept", "application/vnd.futbol.api.retra+json");
		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			showTODO();
			alertify.log("Notification", "error", 5);
			alertify.success("Operación completada correctamente");
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}
function addCom(){
	var comentario = $('#addComent').val();
	var url2 = API_BASE_URL + idCampeonato + '/calendario/' + idPartido + '/comentarios';
	var url = API_BASE_URL_USER + "/" + user;
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			var datos = '{"texto":"'+comentario+'","idUsuario":"'+response.idusuario+'","tiempo":"","media":""}';	
			$.ajax({
				url : url2,
				type : 'POST',
				crossDomain : true,
				data: datos,
				beforeSend: function (request)
				{
					request.withCredentials = true;
					request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
					request.setRequestHeader("Content-Type", "application/vnd.futbol.api.comentario+json");
					request.setRequestHeader("Accept", "application/vnd.futbol.api.comentario+json");
				},
				success : function(data, status, jqxhr) {
					var response2 = $.parseJSON(jqxhr.responseText);
					showTODO();
					alertify.log("Notification", "error", 5);
					alertify.success("Operación completada correctamente");
				},
				error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
				alertify.error("No se ha podido completar la acción");}
			});

		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}