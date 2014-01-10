var API_BASE_URL = "http://localhost:8080/futbol-api";

$(document).ready(function(e){
	//e.preventDefault();
	getEquiposList();
	getNoticiasList()
});





function getEquiposList() {

	console.log("dentro funcion")
	var url = API_BASE_URL + '/club/1/e?offset=0&length=5';
	
	$.ajax({
		url : url,
		type : 'GET',
		headers : {
			"Accept" : "application/vnd.futbol.api.equipo.collection+json"
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
			
			var equipos = response.equipos;
			var htmlString = "";
			

			$.each(equipos, function(i,v){
				var equipo = v;
				var i=0;
				
				if (i==0){
					console.log("dentro funcion crear lista");
					console.log(equipo);
					
				htmlString += '<div class="panel panel-primary"style="width: 300px"> <div class="panel-heading"><h3 class="panel-title">Equipo con identificador: '+equipo.idEquipo;
				htmlString += '</h3> </div> <div class="panel-body">Nombre del equipo: '+equipo.nombre;
				htmlString += '<br/>Campeonato en el que juega: '+equipo.campeonato;
				htmlString +='</div><a href="#" class="btn btn-success" id="verequipos">Visita la ficha de este equipo!</a><br/></div>'
			i++;
    }
if(i==5){
		htmlString += '</div>';
					}
				})

			$('#equiposshow').html(htmlString);

		},
		error : function(jqXHR, options, error) {
			//callbackError(jqXHR, options, error);
		}
	});
}

function getNoticiasList() {

	console.log("dentro funcion noticias")
	var url = API_BASE_URL + '/club/1/noticias?offset=0&length=5';
	
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
					
				htmlString += '<div class="panel panel-primary"style="width: 600px"> <div class="panel-heading"> <span class="label pull-right label-info">'+noticia.lastModified;
				htmlString +='</span><h3 class="panel-title">'+noticia.titulo;
				htmlString += '</h3> </div> <div class="panel-body">'+noticia.content;
				htmlString += '<br/>'+noticia.media;
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
function muestra_oculta(id){
if (document.getElementById){ //se obtiene el id
var el = document.getElementById(id); //se define la variable "el" igual a nuestro div
el.style.display = (el.style.display == 'none') ? 'block' : 'none'; //damos un atributo display:none que oculta el div
}
}
window.onload = function(){/*hace que se cargue la función lo que predetermina que div estará oculto hasta llamar a la función nuevamente*/
muestra_oculta(el);/* "contenido_a_mostrar" es el nombre de la etiqueta DIV que deseamos mostrar */
}