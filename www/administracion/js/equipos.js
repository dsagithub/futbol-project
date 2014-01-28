var host = '147.83.7.157';
var API_BASE_URL = "http://"+host+":8080/futbol-api/club/";
var API_BASE_URL_EQUIPOS = "http://"+host+":8080/futbol-api/campeonato/";
var pass;
var user;
var idClub;
var idEq;

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

$("#button_search").click(function(e){
	e.preventDefault();
	var msg = $('#query').val();
	getList(msg);
});

$("#next").click(function(e){
	e.preventDefault();
	var msg = $('#next').val();
	getList(msg);
});

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

			$.each(response.clubs, function(i,v){
				var club = v;
				htmlString += '<tr onClick="javascript:getEquipos('+club.idClub+');"><td>'+club.idClub+'</td><td>'+club.nombre+'</td></tr>'
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

			$.each(response.clubs, function(i,v){
				var club = v;
				htmlString += '<tr onClick="javascript:getEquipos('+club.idClub+');"><td>'+club.idClub+'</td><td>'+club.nombre+'</td></tr>'
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

function getEquipos(id){
	var url;
	idClub = id;
	url = API_BASE_URL + id + '/e?offset=0&length=5';
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
			var htmlString = '<button type="button" class="btn btn-primary btn-xs pull-right" onClick="javascript:showAddEquipo('+id+');">+ Añadir Equipo</button><p><br><table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>Id Campeonato</th><th>Id Equipo</th><th>Nombre</th></tr></thead><tbody>';
			$.each(response.equipos, function(i,v){
				var equipo = v;
				var linkself="'"+equipo.links[0].uri+"'";
				htmlString += '<tr onClick="javascript:getEquipo('+linkself+');"><td>'+equipo.campeonato+'</td><td>'+equipo.idEquipo+'</td><td>'+equipo.nombre+'</td></tr>'
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
				htmlString += '</tbody></table><ul class="pager"><li class="pull-left" onClick="javascript:getEquipoURL('+prev+')"><a>Anterior</a></li>';
			}else{
				htmlString += '</tbody></table><ul class="pager"><li class="hide pull-left" onClick="javascript:getEquipoURL('+prev+')"><a>Anterior</a></li>';
			}
			if (next!=""){
				htmlString += '<li class="pull-right" onClick="javascript:getEquipoURL('+next+')"><a type="submit" id="next" name="next" >Siguiente</a></li></ul>';
			}else{
				htmlString += '<li class="hide pull-right"><a onClick="javascript:getEquipoURL("'+next+'")">Siguiente</a></li></ul>';
			}
			url = API_BASE_URL + id + '/';
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
					var namehtmlclub = "Información de Equipos del Club " + response.nombre;
					$('#nameclubshow').html(namehtmlclub);	
					$('#clubshow').html(htmlString);	
				},
				error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
				alertify.error("No se ha podido completar la acción");}
			});

		},
		error : function(jqXHR, options, error) {						url = API_BASE_URL + id + '/';
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

				var namehtmlclub = "Información de Equipos del Club " + response.nombre;

				$('#nameclubshow').html(namehtmlclub);
				var htmlString = 'Este Club no tiene equipos añadidos<button type="button" class="btn btn-primary btn-xs pull-right" onClick="javascript:showAddEquipo('+id+');">+ Añadir Equipo</button><p><br>';	
				$('#clubshow').html(htmlString);
			},
			error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
			alertify.error("No se ha podido completar la acción");}
		});}
	});
}
function getEquipoURL(url){
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
			
			var htmlString = '<button type="button" class="btn btn-primary btn-xs pull-right" onClick="javascript:showAddEquipo('+idClub+');">+ Añadir Equipo</button><p><br><table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>Id Campeonato</th><th>Id Equipo</th><th>Nombre</th></tr></thead><tbody>';
			$.each(response.equipos, function(i,v){
				var equipo = v;
				var linkself="'"+equipo.links[0].uri+"'";
				htmlString += '<tr onClick="javascript:getEquipo('+linkself+');"><td>'+equipo.campeonato+'</td><td>'+equipo.idEquipo+'</td><td>'+equipo.nombre+'</td></tr>';
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
				htmlString += '</tbody></table><ul class="pager"><li class="pull-left" onClick="javascript:getEquipoURL('+prev+')"><a>Anterior</a></li>';
			}else{
				htmlString += '</tbody></table><ul class="pager"><li class="hide pull-left" onClick="javascript:getEquipoURL('+prev+')"><a>Anterior</a></li>';
			}
			if (next!=""){
				htmlString += '<li class="pull-right" onClick="javascript:getEquipoURL('+next+')"><a type="submit" id="next" name="next" >Siguiente</a></li></ul>';
			}else{
				htmlString += '<li class="hide pull-right"><a onClick="javascript:getEquipoURL("'+next+'")">Siguiente</a></li></ul>';
			}			
			$('#clubshow').html(htmlString);	
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}

function showAddEquipo(idclub){
	var url2 = API_BASE_URL_EQUIPOS + '/?offset=0&length=500';
	var htmlString = '';
	$.ajax({
		url : url2,
		type : 'GET',
		crossDomain : true,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));

		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			htmlString += '<form><label>Nombre</label><input  maxlength="35" id="addname" name="addname" class="form-control" required/><br><p><label>Campeonato: </label><br><select id="campeonatonew">';
			$.each(response.campeonatos, function(i,v){
				var campeonato = v;
				htmlString += '<option value="'+campeonato.idcampeonatos+'">'+campeonato.nombre+'</option>';
			})
			htmlString +=  '</select></form>';
			BootstrapDialog.show({
				title: 'Añadir nuevo equipo',
				message: htmlString,
				buttons: [ {
					label: 'Crear',
					cssClass: 'btn-primary',
					action: function(dialogItself){
						addEquipo($("#campeonatonew").val()),                   
						dialogItself.close();						
					}
				},  {
					label: 'Cerrar',
					action: function(dialogItself){
						dialogItself.close();
					}
				}]
			});
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});		
}

function addEquipo(idcamp){
	var url = API_BASE_URL + idClub + "/e";
	var nombre = $('#addname').val();
	var datos = '{"nombre":"'+nombre+'","campeonato":"'+idcamp+'"}';
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		data: datos,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
			request.setRequestHeader("Content-Type", "application/vnd.futbol.api.equipo+json");
			request.setRequestHeader("Accept", "application/vnd.futbol.api.equipo+json");
		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			getEquipos(idClub);
			alertify.log("Notification", "error", 5);
			alertify.success("Operación completada correctamente");
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}

function getEquipo(url){
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
			var nombre = response.nombre;
			var idEquip = response.idEquipo;
			idEq = idEquip;
			var url2 = API_BASE_URL_EQUIPOS + '?offset=0&length=50';
			var htmlString = '';
			$.ajax({
				url : url2,
				type : 'GET',
				crossDomain : true,
				beforeSend: function (request)
				{
					request.withCredentials = true;
					request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));

				},
				success : function(data, status, jqxhr) {
					var response = $.parseJSON(jqxhr.responseText);

					htmlString += '<form><label>Nombre</label><input id="editname"  maxlength="35" class="form-control" value="'+ nombre +'" required/><br><p><label>Campeonato: </label><br><select id="campeonatoedit">';
					$.each(response.campeonatos, function(i,v){
						var campeonato = v;
						htmlString += '<option value="'+campeonato.idcampeonatos+'">'+campeonato.nombre+'</option>';
					})
					htmlString +=  '</select>';
					$("#campeonatoedit").select2();
					htmlString += '</form>';
					BootstrapDialog.show({
						title: 'Edicion de Equipo',
						message: htmlString,
						buttons: [ {
							label: 'Administrar Jugadores',
							cssClass: 'btn-success',
							action: function(){
								showAdmJug();                  
							}
						},{
							label: 'Editar',
							cssClass: 'btn-primary',
							action: function(dialogItself){
						editEquipo(idEquip),
						dialogItself.close();              
					}
				}, {
					label: 'Eliminar',
					cssClass: 'btn-warning',
					action: function(dialogItself){
						deleteEquipo(idEquip),
						dialogItself.close();					  
					}
				}, {
					label: 'Cerrar',
					action: function(dialogItself){
						dialogItself.close();
					}
				}]
			});	
				},
				error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
				alertify.error("No se ha podido completar la acción");}
			});	
},
error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
alertify.error("No se ha podido completar la acción");}
});
}

function editEquipo(id){
	var url = API_BASE_URL + idClub + "/e/" + id;
	var datos = '{"nombre":"'+$("#editname").val()+'","campeonato":"'+$("#campeonatoedit").val()+'"}';
	$.ajax({
		url : url,
		type : 'PUT',
		crossDomain : true,
		data: datos,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
			request.setRequestHeader("Content-Type", "application/vnd.futbol.api.equipo+json");
			request.setRequestHeader("Accept", "application/vnd.futbol.api.equipo+json");

		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			alertify.log("Notification", "error", 5);
			alertify.success("Operación completada correctamente");
			getEquipos(idClub);
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}

function deleteEquipo(id){
	var url;
	url = API_BASE_URL + idClub + '/e/' + id;
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
			getEquipos(idClub);
			alertify.log("Notification", "error", 5);
			alertify.success("Operación completada correctamente");
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}
var dialogInstanceshowAdmJug;
function showAdmJug(){

	var API_BASE_URL_JUGADORES = API_BASE_URL + idClub + "/e/" + idEq + "/jugadores";
	var url = API_BASE_URL_JUGADORES + "?offset=0&length=15";
	
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
			var htmlString = '<table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>DNI</th><th>Nombre</th><th>Apellidos</th></tr></thead><tbody>';
			$.each(response.jugadores, function(i,v){
				var jug = v;
				htmlString += '<tr onClick="javascript:delJug('+jug.dni+');"><td>'+jug.dni+'</td><td>'+jug.nombre+'</td><td>'+jug.apellidos+'</td></tr>';
			})
			htmlString += '</tbody></table>';
			dialogInstanceshowAdmJug = new BootstrapDialog({
				title: 'Lista de Jugadores',
				message: htmlString,
				buttons: [ {
					label: 'Añadir Jugador',
					cssClass: 'btn-primary',
					action: function(dialogItself){
						dialogItself.close(),
						showAddJug();						             
					}
				}, {
					label: 'Cerrar',
					action: function(dialogItself){
						dialogItself.close();
					}
				}]
			});
			dialogInstanceshowAdmJug.open();
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});			
}

function delJug(dni){

	BootstrapDialog.confirm('Estas seguro que deseas eliminar el jugador seleccionado?', function(result){
		if(result) {
                var url  = API_BASE_URL + idClub + "/e/" + idEq + "/jugadores/" + dni;
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
                		dialogInstanceshowAdmJug.close();
                		alertify.log("Notification", "error", 5);
                		alertify.success("Operación completada correctamente");
                		showAdmJug();
                	},
                	error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
                	alertify.error("No se ha podido completar la acción");}
                });
            }
        });

}

function showAddJug(){
	var codehtml = '<form><label>Nombre</label><input id="addjugname" maxlength="28" class="form-control" required/><br><label>Apellidos</label><input id="addjugape" maxlength="28" class="form-control" required/><br><label>DNI</label><input type="number" id="addjugdni" class="form-control" required/><br></form>';
	BootstrapDialog.show({
		title: 'Añadir nuevo jugador',
		message: codehtml,
		buttons: [ {
			label: 'Crear',
			cssClass: 'btn-primary submit',
			action: function(dialogItself){
				addJug(),                  
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

function addJug(){
	var url  = API_BASE_URL + idClub + "/e/" + idEq + "/jugadores/";
	var nombre = $('#addjugname').val();
	var apellido = $('#addjugape').val();
	var dni = $('#addjugdni').val();
	var datos = '{"dni":"'+dni+'","nombre":"'+nombre+'","apellidos":"'+apellido+'"}';
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		data: datos,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
			request.setRequestHeader("Content-Type", "application/vnd.futbol.api.jugadores+json");
			request.setRequestHeader("Accept", "application/vnd.futbol.api.jugadores+json");
		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			alertify.log("Notification", "error", 5);
			alertify.success("Operación completada correctamente");
			showAdmJug();
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");
		showAdmJug();}
	});
}