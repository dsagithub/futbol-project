var API_BASE_URL = "http://localhost:8080/futbol-api/club/";
var pass;
var user;

$(document).ready(function(e){
getList();
user="admin";
pass="admin";
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
			
			var htmlString = '<table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>Id Campeonato</th><th>Id Equipo</th><th>Nombre</th></tr></thead><tbody>';
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
			$('#clubshow').html(htmlString);	
		},
		error : function(jqXHR, options, error) {}
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
			
			var htmlString = '<table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>Id Campeonato</th><th>Id Equipo</th><th>Nombre</th></tr></thead><tbody>';
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

function showClub(id){
	var nombre = $('#nombre').text();
	console.log(nombre);
	var codehtml = '<form><label>Nombre</label><input id="newname" name="newname" class="form-control" value="'+ nombre +'" required/></form>';
			BootstrapDialog.show({
            title: 'Edicion de Club',
            message: codehtml,
            buttons: [ {
                label: 'Editar',
                cssClass: 'btn-primary',
                action: function(dialogItself){
                    editarClub(id),
                    dialogItself.close(),
                    getList(),
                    getClub(id);                    
                }
            }, {
                label: 'Eliminar',
                cssClass: 'btn-warning',
                action: function(dialogItself){
                	deleteClub(id),
                	dialogItself.close(),
                    getList(),
                    limpiarclub();  
                }
            }, {
                label: 'Cerrar',
                action: function(dialogItself){
                    dialogItself.close();
                }
            }]
        });	
        }
        
        function limpiarclub(){
        	var htmlString = 'No hay un club seleccionado';	
			$('#clubshow').html(htmlString);
        }

function showAddClub(){

	var codehtml = '<form><label>Nombre</label><input id="addname" name="addname" class="form-control" required/></form>';

	BootstrapDialog.show({
            title: 'Añadir nuevo club',
            message: codehtml,
            buttons: [ {
                label: 'Crear',
                cssClass: 'btn-primary',
                action: function(dialogItself){
                    addClub(),                    
                    dialogItself.close(),
                    getList();
                }
            },  {
                label: 'Cerrar',
                action: function(dialogItself){
                    dialogItself.close();
                }
            }]
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
			console.log(response.links[0].uri);
			var name = getNameCampeonato();
			var htmlString = 'Nombre: <div id="nombre" name="nombre" value="'+response.nombre+'">' + response.nombre + '</div>';		
			$('#equiposhow').html(htmlString);
		},
		error : function(jqXHR, options, error) {}
		});

}

function getNameCampeonato(){
	return name;
}