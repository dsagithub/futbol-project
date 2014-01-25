var API_BASE_URL = "http://localhost:8080/futbol-api/club/";



$(document).ready(function(e){
getList();


	var usuario = $.cookie('usuario');
	var pass = $.cookie('password');




var htmlString = '<ul class="nav navbar-nav navbar-right navbar-user"><li class="dropdown user-dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"> <i class="fa fa-user"></i>'+usuario;
 htmlString += '<b class="caret"></b></a><ul class="dropdown-menu"> <li class="divider"></li> <li><a href="http://localhost:8080/futbol/VistaUsuario/perfilusuario.html">Ver perfil</a></li><li><a href="http://localhost:8080/futbol/index.html"><i class="fa fa-power-off"></i> Salir</a></li></ul></li></ul>';
					
$('#usuario').html(htmlString);		 
						
			
				
});

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

//Para listar los clubs
function getList(search) {
	var usuario = $.cookie('usuario');
	var pass = $.cookie('password');
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
			request.setRequestHeader("Authorization", "Basic "+ btoa(usuario+":"+pass)); //btoa('alicia:alicia'))
			//(username + ":" + password)); };
		
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
							
				if (links.rel=="next"){
					next = "'"+links.uri+"'";
				
				}
				else if (links.rel=="prev"){
					prev = "'"+links.uri+"'";
				
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
	var usuario = $.cookie('usuario');
var pass = $.cookie('password');

	
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(usuario+":"+pass));
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
	var usuario = $.cookie('usuario');
var pass = $.cookie('password');
	var url;
	url = API_BASE_URL + id + '/e?offset=0&length=5';
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
			beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(usuario+":"+pass));
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
							
				if (links.rel=="next"){
					next = "'"+links.uri+"'";
					
				}
				else if (links.rel=="prev"){
					prev = "'"+links.uri+"'";
					
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
	var usuario = $.cookie('usuario');
var pass = $.cookie('password');
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
			beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+btoa(usuario+":"+pass));
		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			
			var htmlString = '<table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>Id Campeonato</th><th>Id Equipo</th><th>Nombre</th></tr></thead><tbody>';
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

	
	var usuario = $.cookie('usuario');
var pass = $.cookie('password');

	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
			beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(usuario+":"+pass));
		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			
			var linkequipo = response.links[0].uri;
			createcookie(linkequipo);
			//var name = getNameCampeonato();
			
		},
		error : function(jqXHR, options, error) {}
		});

}
function createcookie(linkequipo) {

	
	$.cookie('Linkequipo', linkequipo);
	window.location.href="http://localhost:8080/futbol/VistaUsuario/vistaequipo.html"
      
                
}

function getNoticiasList(idclub) {
	var usuario = $.cookie('usuario');
var pass = $.cookie('password');




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
			request.setRequestHeader("Authorization", "Basic "+ btoa(usuario+":"+pass));
		},
		
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			var links = response.links;
			$.each(links, function(i,v){
				var link = v;
					
			});
			
			var noticias = response.noticias;
			var htmlString = "";
			

			$.each(noticias, function(i,v){
				var noticia = v;
				var i=0;
				
				if (i==0){
					
					
				htmlString += '<center> <div class="panel panel-success"style="width: 650px"> <div class="panel-heading"> <span class="label pull-right label-default">'+noticia.lastModified;
				htmlString +='</span><h5><B>'+noticia.titulo;
				htmlString += '</B></h5> </div> <div class="panel-body"><h5>'+noticia.content;
				htmlString += '</h5><br/>'//+noticia.media;
				htmlString +='</div><br/></div></center> '
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