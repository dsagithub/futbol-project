var API_BASE_URL = "http://localhost:8080/futbol-api/club/";
var API_BASE_URL_EQUIPOS = "http://localhost:8080/futbol-api/campeonato/";
var pass;
var user;
var idClub;
var idEq;

$(document).ready(function(e){
	user="admin";
	pass="admin";
	getList();
});

$("#button_search").click(function(e){
	e.preventDefault();
	var msg = $('#query').val();
	console.log(msg);
	getList(msg);
});

$("#next").click(function(e){
	e.preventDefault();
	var msg = $('#next').val();
	console.log(msg);
	getList(msg);
});

function getList(search) {
	var url;
	if (search!=null){
		url = API_BASE_URL + '?offset=0&length=5&pattern='+search; 
		console.log("search")
	}else{
		url = API_BASE_URL + '?offset=0&length=5'; 
		console.log("no search")
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
				console.log(links);				
				if (links.rel=="next"){
					next = "'"+links.uri+"'";
					console.log(next);
				}
				else if (links.rel=="prev"){
					prev = "'"+links.uri+"'";
					console.log(prev);
				}				
			})
			if (prev!=""){
				htmlString += '</tbody></table><ul class="pager"><li class="pull-left" onClick="javascript:getListURL('+prev+')"><a>Previous</a></li>';
			}else{
				htmlString += '</tbody></table><ul class="pager"><li class="hide pull-left" onClick="javascript:getListURL('+prev+')"><a>Previous</a></li>';
			}
			if (next!=""){
				htmlString += '<li class="pull-right" onClick="javascript:getListURL('+next+')"><a type="submit" id="next" name="next" >Next</a></li></ul>';
			}else{
				htmlString += '<li class="hide pull-right"><a onClick="javascript:getListURL("'+next+'")">Next</a></li></ul>';
			}			
			$('#listshow').html(htmlString);
		},
		error : function(jqXHR, options, error) {}
	});
}

function getListURL(url) {
	console.log(url);
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
				console.log(links);				
				if (links.rel=="next"){
					next = "'"+links.uri+"'";
					console.log(next);
				}
				else if (links.rel=="prev"){
					prev = "'"+links.uri+"'";
					console.log(prev);
				}				
			})
			if (prev!=""){
				htmlString += '</tbody></table><ul class="pager"><li class="pull-left" onClick="javascript:getListURL('+prev+')"><a>Previous</a></li>';
			}else{
				htmlString += '</tbody></table><ul class="pager"><li class="hide pull-left" onClick="javascript:getListURL('+prev+')"><a>Previous</a></li>';
			}
			if (next!=""){
				htmlString += '<li class="pull-right" onClick="javascript:getListURL('+next+')"><a type="submit" id="next" name="next" >Next</a></li></ul>';
			}else{
				htmlString += '<li class="hide pull-right"><a onClick="javascript:getListURL("'+next+'")">Next</a></li></ul>';
			}			
			$('#listshow').html(htmlString);
		},
		error : function(jqXHR, options, error) {}
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
				console.log(links);				
				if (links.rel=="next"){
					next = "'"+links.uri+"'";
					console.log(next);
				}
				else if (links.rel=="prev"){
					prev = "'"+links.uri+"'";
					console.log(prev);
				}				
			})
			if (prev!=""){
				htmlString += '</tbody></table><ul class="pager"><li class="pull-left" onClick="javascript:getEquipoURL('+prev+')"><a>Previous</a></li>';
			}else{
				htmlString += '</tbody></table><ul class="pager"><li class="hide pull-left" onClick="javascript:getEquipoURL('+prev+')"><a>Previous</a></li>';
			}
			if (next!=""){
				htmlString += '<li class="pull-right" onClick="javascript:getEquipoURL('+next+')"><a type="submit" id="next" name="next" >Next</a></li></ul>';
			}else{
				htmlString += '<li class="hide pull-right"><a onClick="javascript:getEquipoURL("'+next+'")">Next</a></li></ul>';
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
				error : function(jqXHR, options, error) {}
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
			error : function(jqXHR, options, error) {}
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
				console.log(equipo.links[0].uri);
			})
			var next = "";
			var prev = "";
			$.each(response.links, function(i,v){
				var links = v;
				console.log(links);				
				if (links.rel=="next"){
					next = "'"+links.uri+"'";
					console.log(next);
				}
				else if (links.rel=="prev"){
					prev = "'"+links.uri+"'";
					console.log(prev);
				}				
			})
			if (prev!=""){
				htmlString += '</tbody></table><ul class="pager"><li class="pull-left" onClick="javascript:getEquipoURL('+prev+')"><a>Previous</a></li>';
			}else{
				htmlString += '</tbody></table><ul class="pager"><li class="hide pull-left" onClick="javascript:getEquipoURL('+prev+')"><a>Previous</a></li>';
			}
			if (next!=""){
				htmlString += '<li class="pull-right" onClick="javascript:getEquipoURL('+next+')"><a type="submit" id="next" name="next" >Next</a></li></ul>';
			}else{
				htmlString += '<li class="hide pull-right"><a onClick="javascript:getEquipoURL("'+next+'")">Next</a></li></ul>';
			}			
			$('#clubshow').html(htmlString);	
		},
		error : function(jqXHR, options, error) {}
	});

}

function showAddEquipo(idclub){


	var url2 = API_BASE_URL_EQUIPOS + '/?offset=0&length=500';
	console.log(url2);
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
			htmlString += '<form><label>Nombre</label><input id="addname" name="addname" class="form-control" required/><br><p><label>Campeonato: </label><br><select id="campeonatonew">';
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
						dialogItself.close(),
						getEquipos(idclub);
					}
				},  {
					label: 'Cerrar',
					action: function(dialogItself){
						dialogItself.close();
					}
				}]
			});
		},
		error : function(jqXHR, options, error) {}
	});		
}

function addEquipo(idcamp){
	var url = API_BASE_URL + idClub + "/e";
	var nombre = $('#addname').val();
	var datos = '{"nombre":"'+nombre+'","campeonato":"'+idcamp+'"}';
	console.log(datos);
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
		},
		error : function(jqXHR, options, error) {}
	});
}

function getEquipo(url){

	console.log(url);
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
	console.log(url2);
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
			$("#campeonatoedit").select2();
			htmlString += '<form><label>Nombre</label><input id="editname" class="form-control" value="'+ nombre +'" required/><br><p><label>Campeonato: </label><br><select id="campeonatoedit">';
			$.each(response.campeonatos, function(i,v){
				var campeonato = v;
				htmlString += '<option value="'+campeonato.idcampeonatos+'">'+campeonato.nombre+'</option>';
			})
			htmlString +=  '</select></form>';

			BootstrapDialog.show({
				title: 'Edicion de Equipo',
				message: htmlString,
				buttons: [ {
					label: 'Administrar Jugadores',
					cssClass: 'btn-success',
					action: function(){
						showAdmJug();
						//getList(),
						//getClub(id);                    
					}
				},{
					label: 'Editar',
					cssClass: 'btn-primary',
					action: function(dialogItself){
						//editarClub(),
						editEquipo(idEquip),
						dialogItself.close(),
						getList();
						//getClub(id);                    
					}
				}, {
					label: 'Eliminar',
					cssClass: 'btn-warning',
					action: function(dialogItself){
						console.log(idEquip),
						deleteEquipo(idEquip),
						dialogItself.close(),
						getEquipos(idClub);						  
					}
				}, {
					label: 'Cerrar',
					action: function(dialogItself){
						dialogItself.close();
					}
				}]
			});	
		},
		error : function(jqXHR, options, error) {}
	});		


		},
		error : function(jqXHR, options, error) {}
	});

}

function editEquipo(id){
var url = API_BASE_URL + idClub + "/e/" + id;
	var datos = '{"nombre":"'+$("#editname").val()+'","campeonato":"'+$("#campeonatoedit").val()+'"}';
	console.log(datos);
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
		},
		error : function(jqXHR, options, error) {}
		});
}

function deleteEquipo(id){
	var url;
	url = API_BASE_URL + idClub + '/e/' + id;
	console.log(url);
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
			var response = $.parseJSON(jqxhr.responseText);
		},
		error : function(jqXHR, options, error) {}
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
				htmlString += '<tr onClick="javascript:delJug('+jug.dni+');"><td>'+jug.dni+'</td><td>'+jug.nombre+'</td><td>'+jug.apellidos+'</td></tr>'
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
		error : function(jqXHR, options, error) {}
		});			
}

function delJug(dni){

	BootstrapDialog.confirm('Estas seguro que deseas eliminar el jugador seleccionado?', function(result){
		if(result) {
                //DELTE FUNCTION
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
                		//var response = $.parseJSON(jqxhr.responseText);
                		dialogInstanceshowAdmJug.close();
                		showAdmJug();
                	},
                	error : function(jqXHR, options, error) {}
                });
            }
        });
	
}

function showAddJug(){


	var codehtml = '<form><label>Nombre</label><input id="addjugname" class="form-control" required/><br><label>Apellidos</label><input id="addjugape" class="form-control" required/><br><label>DNI</label><input id="addjugdni" class="form-control" required/><br></form>';

	BootstrapDialog.show({
            title: 'Añadir nuevo jugador',
            message: codehtml,
            buttons: [ {
                label: 'Crear',
                cssClass: 'btn-primary',
                action: function(dialogItself){
                    addJug(),                    
                    dialogItself.close(),
                    showAdmJug();
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
	console.log(datos);
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
		},
		error : function(jqXHR, options, error) {}
		});
}