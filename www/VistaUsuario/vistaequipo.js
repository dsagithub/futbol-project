var API_BASE_URL = "http://localhost:8080/futbol-api/";
var user;
var pass;

$(document).ready(function(e){
var usuario = $.cookie('usuario');
var pass =  $.cookie('password');
var Linkequipo = $.cookie('Linkequipo')


var htmlString = '<ul class="nav navbar-nav navbar-right navbar-user"><li class="dropdown user-dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"><i class="fa fa-user"></i>'+usuario;
 htmlString += '<b class="caret"></b></a><ul class="dropdown-menu"><li><a href="http://localhost:8080/futbol/index.html"><i class="fa fa-power-off"></i> Salir</a></li></ul></li></ul>';
					
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
	getList(msg);
});

function getListJugadores(search) {
	var usuario = $.cookie('usuario');
	var pass =  $.cookie('password');
	console.log("funcion getListJugadores")
	var url;
	if (search!=null){
		url = API_BASE_URL + 'club/1/e/1/jugadores?offset=0&length=5&pattern='+search; 
		console.log("search")
	}else{
		url = API_BASE_URL + 'club/1/e/1/jugadores?offset=0&length=5'; 
		console.log(url);
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
				htmlString += '<td>'+jugador.dni+'</td><td>'+jugador.nombre+'</td><td>'+jugador.apellidos+'</td></tr>'
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
	
	
function getPartidosList(){
	
	var usuario = $.cookie('usuario');
	var pass =  $.cookie('password');
		console.log("funcion GetPArtidos")
		var url;
		/*if (search!=null){
			url = API_BASE_URL + '?offset=0&length=5&pattern='+search; 
			console.log("search")
		}else{
			url = API_BASE_URL + '/campeonato/2/calendario?offset=0&length=5'; 
			console.log(url);
			console.log("no search")
		}*/
		url = API_BASE_URL + '/campeonato/1/calendario?offset=0&length=5'; 
		console.log(url);
		
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
				var htmlString = '<table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>Fecha</th><th>Hora</th><th>Jornada</th><th>EquipoA</th><th>EquipoB</th></tr></thead><tbody>';

				$.each(response.calendarios, function(i,v){
					var calendario = v;
					htmlString += '<td>'+calendario.fecha+'</td><td>'+calendario.hora+'</td><td>'+calendario.jornada+'</td><td>'+calendario.idEquipoA+'</td><td>'+calendario.idEquipoB+'</td></tr>'
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
				$('#partidosshow').html(htmlString);
			},
			error : function(jqXHR, options, error) {}
			});		
}