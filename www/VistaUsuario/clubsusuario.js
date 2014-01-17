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

//Para listar los clubs
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
			request.setRequestHeader("Authorization", "Basic "+ btoa('admin:admin'));
		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			var htmlString = '<table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>Id</th><th>Nombre</th></tr></thead><tbody>';

			$.each(response.clubs, function(i,v){
				var club = v;
				htmlString += '<tr onClick="javascript:getEquipos('+club.idClub+');javascript:getNoticiasList('+club.idClub+')"><td>'+club.idClub+'</td><td>'+club.nombre+'</td></tr>'
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
			request.setRequestHeader("Authorization", "Basic "+ btoa('admin:admin'));
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
			request.setRequestHeader("Authorization", "Basic "+ btoa('admin:admin'));
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
			request.setRequestHeader("Authorization", "Basic "+ btoa('admin:admin'));
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


        
        function limpiarclub(){
        	var htmlString = 'No hay un club seleccionado';	
			$('#clubshow').html(htmlString);
        }



function getEquipo(url){

	console.log("Has clickado en un equipo");

	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
			beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa('admin:admin'));
		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			console.log(response.links[0].uri);
			var linkequipo = response.links[0].uri;
			createcookie(linkequipo);
			//var name = getNameCampeonato();
			
		},
		error : function(jqXHR, options, error) {}
		});

}
function createcookie(linkequipo) {
console.log("dentro funcion cookie equipo");
	console.log(linkequipo);
	
	$.cookie('Linkequipo', linkequipo);
      
                
}

function getNoticiasList(idclub) {

	console.log("dentro funcion noticias")
	console.log(idclub);
	var url = API_BASE_URL +idclub+'/noticias?offset=0&length=20';
	
	$.ajax({
		url : url,
		type : 'GET',
		headers : {
			"Accept" : "application/vnd.futbol.api.noticia.collection+json"
		},
		crossDomain : true,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa('admin:admin'));
		},
		
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			var links = response.links;
			$.each(links, function(i,v){
				var link = v;
				console.log(v.uri);			
			});
			
			var noticias = response.noticias;
			var htmlString = "";
			

			$.each(noticias, function(i,v){
				var noticia = v;
				var i=0;
				
				if (i==0){
					console.log("dentro funcion crear lista noticias");
					console.log(noticia);
					
				htmlString += '<div class="panel panel-default"style="width: 650px"> <div class="panel-heading"> <span class="label pull-right label-default">'+noticia.lastModified;
				htmlString +='</span><h5>'+noticia.titulo;
				htmlString += '</h5> </div> <div class="panel-body"><h5>'+noticia.content;
				htmlString += '</h5><br/>'+noticia.media;
				htmlString +='</div><br/></div>'
			i++;
    }
if(i==5){
		htmlString += '</div>';
					}
				})

			$('#noticiasshow').html(htmlString);

		},
		error : function(jqXHR, options, error) {
			//callbackError(jqXHR, options, error);
		}
	});
}