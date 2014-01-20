var API_BASE_URL = "http://localhost:8080/futbol-api";
var user;
var pass;
var datos;

$(document).ready(function(e){
	user='admin';
	pass='admin';
	//var idclub = $.cookie('identificadorClub');
	//var idequipo = $.cookie('identificadorEquipo');
	getRetransmisiones();
	getComments();
});

function sendComment(){
	console.log("BOTON");
	
	var text = $('#text').val();
if (text == ""){
	console.log("Comentario vacio");
	BootstrapDialog.confirm('No hay mensaje en el comentario');

}
else {
	
	var url = API_BASE_URL + '/campeonato/2/calendario/1/comentarios'; 

	datos= '{"texto":"'+text+'","idUsuario":"1","tiempo":"12","media":""}';
	
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		data: datos,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa('admin:admin'));
			request.setRequestHeader("Content-Type", "application/vnd.futbol.api.comentario+json");
			request.setRequestHeader("Accept", "application/vnd.futbol.api.comentario+json");

		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			BootstrapDialog.confirm('Comentario enviado');

		},
		error : function(jqXHR, options, error) {}
		});
	
	
}

getComments();

}
function getRetransmisiones() {

	console.log("dentro funcion retra1")
	var url = API_BASE_URL + '/campeonato/1/calendario/1/retra?offset=0&length=5';
	//var url = API_BASE_URL + '/campeonato/'+ idclub +'/calendario/' + idequipo +'/retra?offset=0&length=5';

	$.ajax({
		url : url,
		type : 'GET',
		//headers : {
		//	"Accept" : "application/vnd.futbol.api.retra.collection+json"
		//},
		crossDomain : true,
		beforeSend: function (request)
		{
			console.log("holajeshjk");
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));

		},
		
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			var links = response.links;
			console.log(links);
			$.each(links, function(i,v){
				var link = v;
				console.log(v.uri);		
			});
			
			
			var retrans = response.retrans;
			var htmlString = "";
			

			$.each(retrans, function(i,v){				var retra = v;
				var i=0;
				
				if (i==0){
					console.log("dentro funcion crear lista retra");
					console.log(retra);
					
				
				htmlString += '<div class="panel panel-default"> <div class="panel-heading"><h3 class="panel-title"> Minuto: '+retra.tiempo +
				'<h3 class="panel-right">' +retra.texto;	
				if  (retra.media == null){
					htmlString += '</div>';					
				}
				else{
					htmlString += '</h3> </div> <div class="panel-body"> tiempo: ' +retra.tiempo;
					htmlString += '</div>';					
					}
				htmlString += '</div>';
				htmlString +='</div><br/></div>'
			i++;
    }
if(i==5){
		htmlString += '</div>';
					}
				})

			$('#retrashow').html(htmlString);

		},
		error : function(jqXHR, options, error) {
			//callbackError(jqXHR, options, error);
		}
	});
}

function getComments() {

	console.log("funcion getRetras")
	var url = API_BASE_URL + '/campeonato/2/calendario/1/comentarios?offset=0&length=5'; 
	console.log(url)

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
		var links = response.links;
		$.each(links, function(i,v){
			var link = v;
			console.log(v.uri);			
		});
		
		var comentarios = response.comentarios;
		var htmlString = "";
		

		$.each(comentarios, function(i,v){
			var comentario = v;
			var i=0;
			
			if (i==0){
				console.log("dentro funcion comentario");
				console.log(comentario);
				
			//getuser(comentario.idUsuario);
			htmlString += '<div class="panel panel-default"> <div class="panel-heading"><h3 class="panel-title"> Usuario: '+comentario.idUsuario;	
			htmlString += '</h3> </div> <div class="panel-body"> Comentario: '+comentario.texto;
			if  (comentario.media == null){
				htmlString += '</div>';
			}
			else {
			htmlString += '</h3> </div> <div class="panel-body">'+comentario.media;
			htmlString += '</div>';
			}
			htmlString += '</div>';

			
			
		i++;
}

			
if(i==5){
	htmlString += '</div>';
				}
			})

		$('#commshow').html(htmlString);

	},
	error : function(jqXHR, options, error) {
		//callbackError(jqXHR, options, error);
	}
});
}