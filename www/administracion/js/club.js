var API_BASE_URL = "http://localhost:8080/futbol-api/club/";
var user;
var pass;

$(document).ready(function(e){

	//var usuario = $.cookie('usuario');
	//var pass = $.cookie('password');
user='admin';
pass='admin';
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
				htmlString += '<tr onClick="javascript:getClub('+club.idClub+');"><td>'+club.idClub+'</td><td>'+club.nombre+'</td></tr>'
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
				htmlString += '<tr onClick="javascript:getClub('+club.idClub+');"><td>'+club.idClub+'</td><td>'+club.nombre+'</td></tr>'
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

function getClub(id){
	var url;
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
			console.log(response.links[0].uri);
			var htmlString = 'Nombre: <div id="nombre" name="nombre" value="'+response.nombre+'">' + response.nombre + '</div><button type="button" class="btn btn-primary pull-right" onClick="javascript:showClub('+id+')">Editar</button>';		
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

function deleteClub(id){
	var url;
	url = API_BASE_URL + id + '/';
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

function editarClub(id){
	var url;
	url = API_BASE_URL + id + '/';
	var nombre = $('#newname').val();
	var datos = '{"nombre":"'+nombre+'"}';
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
			request.setRequestHeader("Content-Type", "application/vnd.futbol.api.club+json");
			request.setRequestHeader("Accept", "application/vnd.futbol.api.club+json");

		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
		},
		error : function(jqXHR, options, error) {}
		});
}

function addClub(){
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
			request.setRequestHeader("Content-Type", "application/vnd.futbol.api.club+json");
			request.setRequestHeader("Accept", "application/vnd.futbol.api.club+json");

		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
		},
		error : function(jqXHR, options, error) {}
		});
}