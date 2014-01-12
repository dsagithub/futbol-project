var API_BASE_URL = "http://localhost:8080/futbol-api";

$(document).ready(function(e){
	//e.preventDefault();
	getJugadoresList();
	getCampeonatoList()
});

function getEquiposList() {

	console.log("funcion getJugadoresList")
	var url = API_BASE_URL + '/club/1/e/1/jugadores?offset=2&length=3';
	
	$.ajax({
		url : url,
		type : 'GET',
		headers : {
			"Accept" : "application/vnd.futbol.api.jugadores.collection+json"
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
			
			var jugadores = response.jugadores;
			var htmlString = "";
			

			$.each(equipos, function(i,v){
				var jugador = v;
				var i=0;
				
				if (i==0){
					console.log("dentro funcion crear lista jugadores");
					console.log(jugador);
					
				htmlString += '<div class="panel panel-primary"style="width: 300px"> <div class="panel-heading"><h3 class="panel-title">Dni Jugador: '+jugador.Dni;
				htmlString += '</h3> </div> <div class="panel-body">Nombre del jugador: '+jugador.nombre;
				htmlString += '</h3> </div> <div class="panel-body">Apellidos del jugador: '+jugador.apellidos;
				htmlString += '<br/>Equipo en el que juega: '+jugador.idequipo;
				htmlString +='</div><a href="#" class="btn btn-success" id="verequipos">Visita la ficha de este jugador!</a><br/></div>'
			i++;
    }
if(i==5){
		htmlString += '</div>';
					}
				})

			$('#jugadoresshow').html(htmlString);

		},
		error : function(jqXHR, options, error) {
			//callbackError(jqXHR, options, error);
		}
	});
}

function getCampeonatosList() {

	console.log("funcion getCampeonatosList")
	var url = API_BASE_URL + '/campeonato/{idCampeonato}/calendario?offset=0&length=5';
	
	$.ajax({
		url : url,
		type : 'GET',
		headers : {
			"Accept" : "application/vnd.futbol.api.jugadores.collection+json"
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
			
			var jugadores = response.jugadores;
			var htmlString = "";
			

			$.each(equipos, function(i,v){
				var jugador = v;
				var i=0;
				
				if (i==0){
					console.log("dentro funcion crear lista jugadores");
					console.log(jugador);
					
				htmlString += '<div class="panel panel-primary"style="width: 300px"> <div class="panel-heading"><h3 class="panel-title">Dni Jugador: '+jugador.Dni;
				htmlString += '</h3> </div> <div class="panel-body">Nombre del jugador: '+jugador.nombre;
				htmlString += '</h3> </div> <div class="panel-body">Apellidos del jugador: '+jugador.apellidos;
				htmlString += '<br/>Equipo en el que juega: '+jugador.idequipo;
				htmlString +='</div><a href="#" class="btn btn-success" id="verequipos">Visita la ficha de este jugador!</a><br/></div>'
			i++;
    }
if(i==5){
		htmlString += '</div>';
					}
				})

			$('#jugadoresshow').html(htmlString);

		},
		error : function(jqXHR, options, error) {
			//callbackError(jqXHR, options, error);
		}
	});
}
