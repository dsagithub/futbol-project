var API_BASE_URL = "http://147.83.7.157:8080/futbol-api/campeonato/";
var pass;
var user;
var idCampeonato;
var nombreCampeonato;
var seljornada;

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

function getList(search) {
	var url;
	if (search!=null){
		url = API_BASE_URL + '?offset=0&length=5&pattern='+search; 
	}else{
		url = API_BASE_URL + '?offset=0&length=5'; 
	}
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
			var htmlString = '<table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>Id</th><th>Nombre</th></tr></thead><tbody>';

			$.each(response.campeonatos, function(i,v){
				var campeonato = v;
				htmlString += '<tr onClick="javascript:getCampeonato('+campeonato.idcampeonatos+');"><td>'+campeonato.idcampeonatos+'</td><td>'+campeonato.nombre+'</td></tr>'
			})
			var next = "";
			var prev = "";
			$.each(response.links, function(i,v){
				var links = v;			
				if (links.rel=="next"){
					next = "'"+links.uri+"'";
				}
				else if (links.rel=="prev"){
					prev = "'"+links.uri+"'";
				}				
			})
			if (prev!=""){
				htmlString += '</tbody></table><ul class="pager"><li class="pull-left" onClick="javascript:getListURL('+prev+')"><a>Anterior</a></li>';
			}else{
				htmlString += '</tbody></table><ul class="pager"><li class="hide pull-left" onClick="javascript:getListURL('+prev+')"><a>Anterior</a></li>';
			}
			if (next!=""){
				htmlString += '<li class="pull-right" onClick="javascript:getListURL('+next+')"><a type="submit" id="next" name="next" >Siguiente</a></li></ul>';
			}else{
				htmlString += '<li class="hide pull-right"><a onClick="javascript:getListURL("'+next+'")">Siguiente</a></li></ul>';
			}			
			$('#listshow').html(htmlString);
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}

function getListURL(url) {
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
			var htmlString = '<table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>Id</th><th>Nombre</th></tr></thead><tbody>';

			$.each(response.campeonatos, function(i,v){
				var campeonato = v;
				htmlString += '<tr onClick="javascript:getCampeonato('+campeonato.idcampeonatos+');"><td>'+campeonato.idcampeonatos+'</td><td id="nombre">'+campeonato.nombre+'</td></tr>'
			})
			var next = "";
			var prev = "";
			$.each(response.links, function(i,v){
				var links = v;		
				if (links.rel=="next"){
					next = "'"+links.uri+"'";
				}
				else if (links.rel=="prev"){
					prev = "'"+links.uri+"'";
				}				
			})
			if (prev!=""){
				htmlString += '</tbody></table><ul class="pager"><li class="pull-left" onClick="javascript:getListURL('+prev+')"><a>Anterior</a></li>';
			}else{
				htmlString += '</tbody></table><ul class="pager"><li class="hide pull-left" onClick="javascript:getListURL('+prev+')"><a>Anterior</a></li>';
			}
			if (next!=""){
				htmlString += '<li class="pull-right" onClick="javascript:getListURL('+next+')"><a type="submit" id="next" name="next" >Siguiente</a></li></ul>';
			}else{
				htmlString += '<li class="hide pull-right"><a onClick="javascript:getListURL("'+next+'")">Siguiente</a></li></ul>';
			}			
			$('#listshow').html(htmlString);
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}

function getCampeonato(idcamp){
	idCampeonato = idcamp;
	var url = API_BASE_URL + idcamp;
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
			var namehtmlcampe = "Información del campeonato " + response.nombre;
			nombreCampeonato = response.nombre;
			var neq = numequiposcompeti(idcamp);
			showPartidos(neq);
			var npa = numpartidocompeti(idcamp);
			var htmlString = "Numeros de equipos inscritos: "+neq+"<br><p>Numero de Partidos: "+npa;
			htmlString += '<button type="button" class="btn btn-primary pull-right" onClick="javascript:showEdit('+idcamp+');">Editar</button>';
			$('#campename').html(namehtmlcampe);
			$('#campeshow').html(htmlString);	
			
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}

function showPartidos(neq){
	var htmlString = '';
	if (neq!=0){
		htmlString += '<label>Selecciona la jornada: &nbsp;</label><select id="jornadashow">';
		for (i=0; i<neq*2; ++i){
			var jornada = i+1;
			htmlString += '<option value="'+jornada+'">'+jornada+'</option>';
		}
		htmlString +=  '</select>&nbsp;<button class="fa fa-search" onClick="javascript:showJornada();"></button><div id="divart"></div>';
		$('#jorshow').html(htmlString);	
		$('#jornadashow').val(1);
		showJornada();
	}else{
		htmlString += 'Este campeonato no tiene equipos inscritos aún.';
		$('#jorshow').html(htmlString);
	}

}
function showJornada(){
	var jornada = $('#jornadashow').val();
	seljornada = jornada;
	var url = API_BASE_URL + idCampeonato + "/calendario?offset=0&length=100&shjornada="+jornada;
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
			var contador=0;
			var htmlString = '<br>';
			$.each(response.calendarios, function(i,v){
				if(contador==0){
					htmlString += '<div class="table-responsive"><table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th class="header">Partido</th><th class="header">Fecha y Hora</th></tr></thead><tbody>';
				}
				htmlString += '<tr><td class="text-center" style="vertical-align:middle;">'+v.equipoA+' - '+v.equipoB+'</td><td class="text-center" style="vertical-align:middle;">'+v.fecha+'&nbsp;<br>'+v.hora+'</td><td class="text-center" style="vertical-align:middle;" onClick="showdelPart('+v.idPartido+')">X</td></tr>';
				contador++;
			})			
			if(contador==0){
				htmlString += '<br>La jornada '+jornada+' no tiene partidos aún.';
			}
			htmlString += '</tbody></table></div><br><button class="btn btn-primary pull-right" onClick="javascript:showAddPar();">+ Añadir Partido</button>';
			$('#divart').html(htmlString);
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});	
}

function showdelPart(idpart){
	BootstrapDialog.confirm('Estas seguro que deseas eliminar el partido seleccionado?', function(result){
		if(result) {
                var url = API_BASE_URL + idCampeonato + '/calendario/' + idpart;
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
                		showJornada();
                		alertify.log("Notification", "error", 5);
                		alertify.success("Operación completada correctamente");
                	},
                	error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
                	alertify.error("No se ha podido completar la acción");}
                });
            }
        });
}


function showAddPar(){
	var url = API_BASE_URL + idCampeonato + "/equipos?offset=0&length=100";
	var codehtml = '<div class="col-lg-6"><br><label>Local: &nbsp;</label><select id="eqlocal">';
	var htmlString =  '';
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
				htmlString += '<option value="'+v.idEquipo+'">'+v.nombre+'</option>'
			})			
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});

	codehtml += ''+htmlString+'</select><br><br><label>Visitante: &nbsp;</label><select id="eqvisit">'+htmlString+'</select>';
	codehtml += '</div><div class="col-lg-6"><label>Fecha: &nbsp;</label><input type="text" class="form-control" id="datetimepicker6" data-format="DD/MM/YYYY" /><script type="text/javascript">$(function () {$("#datetimepicker6").datetimepicker({pickTime: false});});</script>';
	codehtml += '<label>Hora: &nbsp;</label><input type="text" class="form-control" id="datetimepicker7" /><script type="text/javascript">$(function () {$("#datetimepicker7").datetimepicker({pickDate: false,icons: {time: "fa fa-clock-o",date: "fa fa-calendar",up: "fa fa-arrow-up",down: "fa fa-arrow-down"}});});</script></div><br><br><br><br><br><br>';

	BootstrapDialog.show({
		title: 'Añadir Partido',
		message: codehtml,
		buttons: [ {
			label: 'Añadir',
			cssClass: 'btn-primary',
			action: function(dialogItself){
				addPartido(),
				dialogItself.close();                    
			}
		},  {
			label: 'Cerrar',
			action: function(dialogItself){
				dialogItself.close();
			}
		}]
	});
}
function addPartido(){
	var equipoA = $('#eqlocal').val();
	var equipoB = $('#eqvisit').val();
	var fecha = $('#datetimepicker6').val();
	var hora = $('#datetimepicker7').val();
	var datos = '{"idEquipoA":"'+equipoA+'","idEquipoB":"'+equipoB+'","jornada":"'+seljornada+'","fecha":"'+fecha+'","hora":"'+hora+'"}';
	var url = API_BASE_URL + idCampeonato + "/calendario";
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		data: datos,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
			request.setRequestHeader("Content-Type", "application/vnd.futbol.api.calendario+json");
			request.setRequestHeader("Accept", "application/vnd.futbol.api.calendario+json");

		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			showJornada();
			alertify.log("Notification", "error", 5);
			alertify.success("Operación completada correctamente");
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
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

function numpartidocompeti(idcamp){
	var url = API_BASE_URL + idcamp + "/calendario?offset=0&length=9999";
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
			
			$.each(response.calendarios, function(i,v){
				contador++;
			})			
		},
		error : function(jqXHR, options, error) {}
	});
	return contador;

}
function showEdit(idcamp){
	var codehtml = '<form><label>Nombre</label><input id="edname" name="edname" class="form-control" value="'+nombreCampeonato+'" required/></form>';

	BootstrapDialog.show({
		title: 'Editar campeonato',
		message: codehtml,
		buttons: [ {
			label: 'Editar',
			cssClass: 'btn-primary',
			action: function(dialogItself){
				editCampe(),
				dialogItself.close();                    
			}
		},{
			label: 'Eliminar',
			cssClass: 'btn-danger',
			action: function(dialogItself){
				delCampe(),
				getList(),
				dialogItself.close();                    
			}
		},  {
			label: 'Cerrar',
			action: function(dialogItself){
				dialogItself.close();
			}
		}]
	});

}

function editCampe(){
	var nombre = $('#edname').val();
	var url;
	url = API_BASE_URL + idCampeonato + '/';
	var datos = '{"nombre":"'+nombre+'"}';
	$.ajax({
		url : url,
		type : 'PUT',
		crossDomain : true,
		data: datos,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
			request.setRequestHeader("Content-Type", "application/vnd.futbol.api.campeonatos+json");
			request.setRequestHeader("Accept", "application/vnd.futbol.api.campeonatos+json");

		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			getList();
			getCampeonato(idCampeonato);
			alertify.log("Notification", "error", 5);
			alertify.success("Operación completada correctamente");
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}
function delCampe(){
	var url;
	url = API_BASE_URL + idCampeonato + '/';
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
			alertify.log("Notification", "error", 5);
			alertify.success("Operación completada correctamente");
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});

}
function showAddCampe(){

	var codehtml = '<form><label>Nombre</label><input id="addname" name="addname" class="form-control" required/></form>';

	BootstrapDialog.show({
		title: 'Añadir nuevo Campeonato',
		message: codehtml,
		buttons: [ {
			label: 'Crear',
			cssClass: 'btn-primary',
			action: function(dialogItself){
				addCampe(),                    
				dialogItself.close();                    
			}
		},  {
			label: 'Cerrar',
			action: function(dialogItself){
				dialogItself.close();
			}
		}]
	});
}

function addCampe(){
	var url = API_BASE_URL;
	var nombre = $('#addname').val();
	var datos = '{"nombre":"'+nombre+'"}';
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		data: datos,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
			request.setRequestHeader("Content-Type", "application/vnd.futbol.api.campeonatos+json");
			request.setRequestHeader("Accept", "application/vnd.futbol.api.campeonatos+json");

		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			getList();
			alertify.log("Notification", "error", 5);
			alertify.success("Operación completada correctamente");
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}