var API_BASE_URL = "http://localhost:8080/futbol-api/campeonato/";
var pass;
var user;
var idCampeonato;
var nombreCampeonato;

$(document).ready(function(e){
	user="admin";
	pass="admin";
	getList();
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

			$.each(response.campeonatos, function(i,v){
				var campeonato = v;
				htmlString += '<tr onClick="javascript:getCampeonato('+campeonato.idcampeonatos+');"><td>'+campeonato.idcampeonatos+'</td><td>'+campeonato.nombre+'</td></tr>'
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

			$.each(response.campeonatos, function(i,v){
				var campeonato = v;
				htmlString += '<tr onClick="javascript:getCampeonato('+campeonato.idcampeonatos+');"><td>'+campeonato.idcampeonatos+'</td><td id="nombre">'+campeonato.nombre+'</td></tr>'
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
		error : function(jqXHR, options, error) {}
	});
}

function showPartidos(neq){
var htmlString = '<label>Selecciona la jornada: </label><br><select id="campeonatonew">';
for (i=0; i<neq*2; ++i){
	var jornada = i+1;
	htmlString += '<option value="'+jornada+'">'+jornada+'</option>'
}
htmlString +=  '</select>';
$('#jorshow').html(htmlString);	
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
		console.log("DENTRO");
		var codehtml = '<form><label>Nombre</label><input id="edname" name="edname" class="form-control" value="'+nombreCampeonato+'" required/></form>';

	BootstrapDialog.show({
            title: 'Editar campeonato',
            message: codehtml,
            buttons: [ {
                label: 'Editar',
                cssClass: 'btn-primary',
                action: function(dialogItself){
                    editCampe(),
                    getList(),
                    getCampeonato(idCampeonato),
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
	console.log(nombre);
	var url;
	url = API_BASE_URL + idCampeonato + '/';
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
			request.setRequestHeader("Content-Type", "application/vnd.futbol.api.campeonatos+json");
			request.setRequestHeader("Accept", "application/vnd.futbol.api.campeonatos+json");

		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
		},
		error : function(jqXHR, options, error) {}
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
			//var response = $.parseJSON(jqxhr.responseText);
		},
		error : function(jqXHR, options, error) {}
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
		},
		error : function(jqXHR, options, error) {}
		});
}