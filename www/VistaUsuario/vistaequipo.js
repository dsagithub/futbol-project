var API_BASE_URL = "http://localhost:8080/futbol-api/";

$(document).ready(function(e){
var user = $.cookie('usuario');
var pass =  $.cookie('password');
var Linkequipo = $.cookie('Linkequipo')
var htmlString = '<ul class="nav navbar-nav navbar-right navbar-user"><li class="dropdown user-dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"> <i class="fa fa-user"></i>'+user;
 htmlString += '<b class="caret"></b></a><ul class="dropdown-menu"> <li class="divider"></li> <li> <a href="perfilusuario.html">Ver perfil</a></li><li onClick="javascript:deletecookie()"><a><i class="fa fa-power-off" ></i> Salir</a></li></ul></li></ul>';				
$('#usuario').html(htmlString);		
getListJugadores();
getPartidosList();
});

$("#button_search").click(function(e){
	e.preventDefault();
	var msg = $('#query').val();
	console.log(msg);
	getListJugadores(msg);
});

$("#next").click(function(e){
	e.preventDefault();
	var msg = $('#next').val();
	console.log(msg);
});

function getListJugadores(search) {
	var Linkequipo = $.cookie('Linkequipo')
	var usuario = $.cookie('usuario');
	var pass =  $.cookie('password');
	var url;
	if (search!=null){
		url = Linkequipo + '/jugadores?offset=0&length=5&pattern='+search; 
		console.log("search");
	}else{
		url = Linkequipo + '/jugadores?offset=0&length=5'; 
		console.log("no search")
	}
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(usuario+':'+pass));

		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			var htmlString = '<table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>Dni</th><th>Nombre</th><th>Apellidos</th></tr></thead><tbody>';	
			$.each(response.jugadores, function(i,v){
				var jugador = v;
				console.log(response.jugadores);
				htmlString += '<td>'+jugador.dni+'</td><td>'+jugador.nombre+'</td><td>'+jugador.apellidos+'</td></tr>';
			})		
			var next = "";
			var prev = "";
			$.each(response.links, function(i,v){
				var links = v;
				console.log(links);				
				if (links.rel=="next"){
					next = "'"+links.uri+"'";
				}
				else if (links.rel=="prev"){
					prev = "'"+links.uri+"'";
				}				
			})
			if (prev!=""){
				htmlString += '</tbody></table><ul class="pager"><li class="pull-left" onClick="javascript:getListjURL('+prev+')"><a>Previous</a></li>';
			}else{
				htmlString += '</tbody></table><ul class="pager"><li class="hide pull-left" onClick="javascript:getListjURL('+prev+')"><a>Previous</a></li>';
			}
			if (next!=""){
				htmlString += '<li class="pull-right" onClick="javascript:getListjURL('+next+')"><a type="submit" id="next" name="next" >Next</a></li></ul>';
			}else{
				htmlString += '<li class="hide pull-right"><a onClick="javascript:getListjURL("'+next+'")">Next</a></li></ul>';
			}			
			$('#listshow').html(htmlString);
		},
		error : function(jqXHR, options, error) {}
		});
}
	
function getListjURL(url){
	var user = $.cookie('usuario');
	var pass =  $.cookie('password');
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
			var htmlString = '<table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>Dni</th><th>Nombre</th><th>Apellidos</th></tr></thead><tbody>';
			$.each(response.jugadores, function(i,v){
				var jugador = v;
				htmlString += '<td>'+jugador.dni+'</td><td>'+jugador.nombre+'</td><td>'+jugador.apellidos+'</td></tr>';				
			})
			var next = "";
			var prev = "";
			$.each(response.links, function(i,v){
				var links = v;
				if (links.rel=="next"){
					next = "'"+links.uri+"'";
					console.log(next);
				}
				else if (links.rel=="prev"){
					prev = "'"+links.uri+"'";
				}				
			})
			if (prev!=""){
				htmlString += '</tbody></table><ul class="pager"><li class="pull-left" onClick="javascript:getListjURL('+prev+')"><a>Previous</a></li>';
			}else{
				htmlString += '</tbody></table><ul class="pager"><li class="hide pull-left" onClick="javascript:getListjURL('+prev+')"><a>Previous</a></li>';
			}
			if (next!=""){
				htmlString += '<li class="pull-right" onClick="javascript:getListjURL('+next+')"><a type="submit" id="next" name="next" >Next</a></li></ul>';
			}else{
				htmlString += '<li class="hide pull-right"><a onClick="javascript:getListjURL("'+next+'")">Next</a></li></ul>';
			}			
			$('#listshow').html(htmlString);
		},
		error : function(jqXHR, options, error) {}
		});
		
	
}
function deletecookie(){
	console.log("dentro delete cookie");
	$.removeCookie('usuario');
	$.removeCookie('password');
	var usuario = null;
	var password = null;
 window.location.href="../index.html"

}

function getPartidosList(){
	var linkequipo = $.cookie('Linkequipo')
	var user = $.cookie('usuario');
	var pass =  $.cookie('password');
		url = linkequipo;
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
			beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+":"+pass));
		},

		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			var campeonatoid = response.campeonato;
			var linkequipo = $.cookie('Linkequipo')
			var user = $.cookie('usuario');
			var pass =  $.cookie('password');
			var url2 = API_BASE_URL + 'campeonato/'+ campeonatoid +'/calendario?offset=0&length=5';
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
				var htmlString = '<table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>Fecha</th><th>Hora</th><th>Jornada</th><th>EquipoA</th><th>EquipoB</th><th>Campeonato</th></tr></thead><tbody>';

				$.each(response.calendarios, function(i,v){
					var calendario = v;
					var linkself="'"+calendario.links[0].uri+"'";
					htmlString += '<tr onClick="javascript:getRetransmision('+linkself+');"><td>'+calendario.fecha+'</td><td>'+calendario.hora+'</td><td>'+calendario.jornada+'</td><td>'+calendario.equipoA+'</td><td>'+calendario.equipoB+'</td><td>'+calendario.nomCampeonato+'</td></tr>'
					console.log(htmlString);
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
					htmlString += '</tbody></table><ul class="pager"><li class="pull-left" onClick="javascript:getListpartidoURL('+prev+','+campeonatoid+')"><a>Previous</a></li>';
				}else{
					htmlString += '</tbody></table><ul class="pager"><li class="hide pull-left" onClick="javascript:getListpartidoURL('+prev+','+campeonatoid+')"><a>Previous</a></li>';
				}
				if (next!=""){
					htmlString += '<li class="pull-right" onClick="javascript:getListpartidoURL('+next+','+campeonatoid+')"><a type="submit" id="next" name="next" >Next</a></li></ul>';
				}else{
					htmlString += '<li class="hide pull-right"><a onClick="javascript:getListpartidoURL('+next+','+campeonatoid+')">Next</a></li></ul>';
				}			
				$('#partidosshow').html(htmlString);
			},
			error : function(jqXHR, options, error) {}
			});	
			
			
			
		},
		error : function(jqXHR, options, error) {}
		});
		
}


function getListpartidoURL(url,campeonatoid){
	console.log("boton url");
	console.log(url);
	var user = $.cookie('usuario');
	var pass =  $.cookie('password');
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
			beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+":"+pass));
		},

		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			var linkequipo = $.cookie('Linkequipo')
			var user = $.cookie('usuario');
			var pass =  $.cookie('password');
			var url2 = API_BASE_URL + 'campeonato/'+ campeonatoid +'/calendario?offset=0&length=5';
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
				var htmlString = '<table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>Fecha</th><th>Hora</th><th>Jornada</th><th>EquipoA</th><th>EquipoB</th><th>Campeonato</th></tr></thead><tbody>';

				$.each(response.calendarios, function(i,v){
					var calendario = v;
					var linkself="'"+calendario.links[0].uri+"'";
					htmlString += '<tr onClick="javascript:getRetransmision('+linkself+');"><td>'+calendario.fecha+'</td><td>'+calendario.hora+'</td><td>'+calendario.jornada+'</td><td>'+calendario.equipoA+'</td><td>'+calendario.equipoB+'</td><td>'+calendario.nomCampeonato+'</td></tr>'
					console.log(htmlString);
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
					htmlString += '</tbody></table><ul class="pager"><li class="pull-left" onClick="javascript:getListpartidoURL('+prev+','+campeonatoid+')"><a>Previous</a></li>';
				}else{
					htmlString += '</tbody></table><ul class="pager"><li class="hide pull-left" onClick="javascript:getListpartidoURL('+prev+','+campeonatoid+')"><a>Previous</a></li>';
				}
				if (next!=""){
					htmlString += '<li class="pull-right" onClick="javascript:getListpartidoURL('+next+','+campeonatoid+')"><a type="submit" id="next" name="next" >Next</a></li></ul>';
				}else{
					htmlString += '<li class="hide pull-right"><a onClick="javascript:getListpartidoURL('+next+','+campeonatoid+')">Next</a></li></ul>';
				}			
				$('#partidosshow').html(htmlString);
			},
			error : function(jqXHR, options, error) {}
			});	
			
			
			
		},
		error : function(jqXHR, options, error) {}
		});
	
}

function getRetransmision(url){
	console.log("Has clickado en un partido");
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
			console.log(response.links[0].uri);
			var linkRetransmision = response.links[0].uri;
			createcookie(linkRetransmision);
			//var name = getNameCampeonato();
			
		},
		error : function(jqXHR, options, error) {}
		});

}

function createcookie(linkRetransmision) {
	console.log("dentro funcion cookie retra");
		console.log(linkRetransmision);
		var user = $.cookie('usuario');

		var pass =  $.cookie('password');
		$.cookie('LinkRetransmision', linkRetransmision);
		$.cookie('usuario', user);
		$.cookie('password', pass);
		window.location.href="http://localhost:8080/futbol/VistaUsuario/Retransmision.html"
	      
	                
	}