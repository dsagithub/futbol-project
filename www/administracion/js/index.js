var API_BASE_URL = "http://147.83.7.157:8080/futbol-api";

$(document).ready(function(e){
	gestioncookie();
	getClubs();
	getUsers();
	getCampe();
});

function gestioncookie(){
	user=$.cookie('usuario');
	pass=$.cookie('password');
	if(user!="admin"){
		window.location="../index.html";
	}else{
		var usuario = user;
		var htmlString = '<ul class="nav navbar-nav navbar-right navbar-user"><li class="dropdown user-dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"> <i class="fa fa-user"></i>&nbsp;'+usuario;
		htmlString += '<b class="caret"></b></a><ul class="dropdown-menu"><a href="#"><li onClick="javascript:cerrarsesion()"><i class="fa fa-power-off"></i>&nbsp; Salir</a></li></a></ul></li></ul>';
		$('#usuario').html(htmlString);	
	}
}
function cerrarsesion(){
	alertify.alert("Estás a punto de cerrar sesión. Continuar?", function (e) {
		if (e) {
      window.location="../index.html";
  }
});
}

function getClubs() {
	var url = API_BASE_URL + '/club?offset=0&length=9999'; 
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
			
			var contador = 0;
			$.each(response.clubs, function(i,v){
				contador++;
			})
			var htmlString = ''+contador;
			$('#numclubs').html(htmlString);
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}
function getUsers() {
	var url = API_BASE_URL + '/users?offset=0&length=9999'; 
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
			
			var contador = 0;
			$.each(response.users, function(i,v){
				contador++;
			})
			var htmlString = ''+contador;
			$('#numusers').html(htmlString);
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}
function getCampe() {
	var url = API_BASE_URL + '/campeonato/?offset=0&length=9999'; 
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
			
			var contador = 0;
			$.each(response.campeonatos, function(i,v){
				contador++;
			})
			var htmlString = ''+contador;
			$('#numcampe').html(htmlString);
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}