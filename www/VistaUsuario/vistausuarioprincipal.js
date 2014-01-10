var API_BASE_URL = "http://localhost:8080/futbol-api";

$(document).ready(function(e){
	//e.preventDefault();
	getClubsList();
});





function getClubsList() {
	console.log("dentro funcion")
	var url = API_BASE_URL + '/club?offset=0&length=5';
	
	$.ajax({
		url : url,
		type : 'GET',
		headers : {
			"Accept" : "application/vnd.futbol.api.club.collection+json"
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
			
			var clubs = response.clubs;
			var htmlString = "";
			

			$.each(clubs, function(i,v){
				var club = v;
				var i=0;
				
				if (i==0){
					console.log("dentro funcion crear lista");
					console.log(club);
					
				htmlString += '<div class="panel panel-primary"style="width: 400px"> <div class="panel-heading"><h3 class="panel-title">Club con identificador: '+club.idClub;
				htmlString += '</h3> </div> <div class="panel-body">Nombre del club: '+club.nombre;
				htmlString += '</div><a href="http://localhost:8080/futbol/VistaUsuario/club.html" class="btn btn-success" id="verequipos">Visita la ficha de este club!</a><br/></div>'
					
        
          
          
          
    
					i++;
       
				}

					if(i==5){
						htmlString += '</div>';
					}
				})

			$('#clubsshow').html(htmlString);

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