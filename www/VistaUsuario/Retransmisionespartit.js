var API_BASE_URL = "http://localhost:8080/futbol-api";

var datos;

$(document).ready(function(e){
	
	var user = $.cookie('usuario');
	var pass =  $.cookie('password');
	var Linkequipo = $.cookie('Linkequipo')
	
	var htmlString = '<ul class="nav navbar-nav navbar-right navbar-user"><li class="dropdown user-dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"> <i class="fa fa-user"></i>'+user;
	 htmlString += '<b class="caret"></b></a><ul class="dropdown-menu"> <li class="divider"></li> <li> <a href="perfilusuario.html">Ver perfil</a></li><li onClick="javascript:deletecookie()"><a><i class="fa fa-power-off" ></i> Salir</a></li></ul></li></ul>';
						
	$('#usuario').html(htmlString);		

var linkretra = $.cookie('LinkRetransmision');
	getRetransmisiones();
	getComments();
});

function sendComment(){
	
	var user = $.cookie('usuario');
	var pass =  $.cookie('password');
	var linkretra = $.cookie('LinkRetransmision');
	
	var text = $('#text').val();
if (text == ""){
	console.log("Comentario vacio");
	BootstrapDialog.confirm('No hay mensaje en el comentario');

}
else {
	var user = $.cookie('usuario');
	var pass =  $.cookie('password');
	var linkretra = $.cookie('LinkRetransmision');
	var url2 = API_BASE_URL + "/users/" + user;
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
			var user = $.cookie('usuario');
			var pass =  $.cookie('password');
			var response = $.parseJSON(jqxhr.responseText);
			var iduser = response.idusuario;
			var linkretra = $.cookie('LinkRetransmision');
			var url = linkretra + '/comentarios'; 
			var text = $('#text').val();
	var datos= '{"texto":"'+text+'","idUsuario":' + iduser +',"tiempo":"","media":""}';
	
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
			
			
	
		},
		error : function(jqXHR, options, error) {
					BootstrapDialog.confirm('Comentario NO enviado');

		
		}
	});
	
	
}

getComments();

setTimeout ("redireccionar()", 2000);



}
function redireccionar()
{
	window.location.href="http://localhost:8080/futbol/VistaUsuario/Retransmision.html"

}

function getRetransmisiones() {
	var user = $.cookie('usuario');
	var pass =  $.cookie('password');
	var linkretra = $.cookie('LinkRetransmision');
	var url = linkretra + '/retra?offset=0&length=5';

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
			
			var retrans = response.retrans;
			var htmlString = "";
			

			$.each(retrans, function(i,v){				
				var retra = v;
				var i=0;
				
				if (i==0){
					console.log("dentro funcion crear lista retra");
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

				
				if (prev!=""){
					htmlString += '</tbody></table><ul class="pager"><li class="pull-left" onClick="javascript:getListretraURL('+prev+')"><a>Anterior</a></li>';
				}else{
					htmlString += '</tbody></table><ul class="pager"><li class="hide pull-left" onClick="javascript:getListretraURL('+prev+')"><a>Anterior</a></li>';
				}
				if (next!=""){
					htmlString += '<li class="pull-right" onClick="javascript:getListretraURL('+next+')"><a type="submit" id="next" name="next" >Siguiente</a></li></ul>';
				}else{
					htmlString += '<li class="hide pull-right"><a onClick="javascript:getListretraURL("'+next+'")">Siguiente</a></li></ul>';
				}
			$('#retrashow').html(htmlString);

		},
		error : function(jqXHR, options, error) {
		}
	});
}


function getListretraURL(url)
{
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
			var links = response.links;
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
			
			var retrans = response.retrans;
			var htmlString = "";
			

			$.each(retrans, function(i,v){				var retra = v;
				var i=0;
				
				if (i==0){
					console.log("dentro funcion crear lista retra");
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

				
				if (prev!=""){
					htmlString += '</tbody></table><ul class="pager"><li class="pull-left" onClick="javascript:getListretraURL('+prev+')"><a>Anterior</a></li>';
				}else{
					htmlString += '</tbody></table><ul class="pager"><li class="hide pull-left" onClick="javascript:getListretraURL('+prev+')"><a>Anterior</a></li>';
				}
				if (next!=""){
					htmlString += '<li class="pull-right" onClick="javascript:getListretraURL('+next+')"><a type="submit" id="next" name="next" >Siguiente</a></li></ul>';
				}else{
					htmlString += '<li class="hide pull-right"><a onClick="javascript:getListretraURL("'+next+'")">Siguiente</a></li></ul>';
				}
			$('#retrashow').html(htmlString);

		},
		error : function(jqXHR, options, error) {
		}
	});
	
}

function getComments() {
	var user = $.cookie('usuario');
	var pass =  $.cookie('password');
	var linkretra = $.cookie('LinkRetransmision');
	var url = linkretra + '/comentarios?offset=0&length=3'; 

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
		
		var comentarios = response.comentarios;
		var htmlString = "";
		

		$.each(comentarios, function(i,v){
			var comentario = v;
			var i=0;
			
			if (i==0){
				console.log("dentro funcion comentario");
			htmlString += '<div class="panel panel-default"> <div class="panel-heading"><h3 class="panel-title"> Usuario: '+comentario.username;	
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
			
			
			if (prev!=""){
				htmlString += '</tbody></table><ul class="pager"><li class="pull-left" onClick="javascript:getListURL('+prev+')"><a>Anterior</a></li>';
			}else{
				htmlString += '</tbody></table><ul class="pager"><li class="hide pull-left" onClick="javascript:getListURL('+prev+')"><a>Anterior</a></li>';
			}
			if (next!=""){
				htmlString += '<li class="pull-right" onClick="javascript:getListURL('+next+')"><a type="submit" id="next" name="next" >Siguiente</a></li></ul>';
			}else{
				htmlString += '<li class="hide pull-right"><a onClick="javascript:getListURL("'+next+'")">Siguiente</a></li></ul>';
			}	
		$('#commshow').html(htmlString);

	},
	error : function(jqXHR, options, error) {
		//callbackError(jqXHR, options, error);
	}
});
}
function getListURL(url){
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
			var links = response.links;
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
			
			var comentarios = response.comentarios;
			var htmlString = "";
			

			$.each(comentarios, function(i,v){
				var comentario = v;
				var i=0;
				
				if (i==0){
					console.log("dentro funcion comentario");
				htmlString += '<div class="panel panel-default"> <div class="panel-heading"><h3 class="panel-title"> Usuario: '+comentario.username;	
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
				
				
				if (prev!=""){
					htmlString += '</tbody></table><ul class="pager"><li class="pull-left" onClick="javascript:getListURL('+prev+')"><a>Anterior</a></li>';
				}else{
					htmlString += '</tbody></table><ul class="pager"><li class="hide pull-left" onClick="javascript:getListURL('+prev+')"><a>Anterior</a></li>';
				}
				if (next!=""){
					htmlString += '<li class="pull-right" onClick="javascript:getListURL('+next+')"><a type="submit" id="next" name="next" >Siguiente</a></li></ul>';
				}else{
					htmlString += '<li class="hide pull-right"><a onClick="javascript:getListURL("'+next+'")">Siguiente</a></li></ul>';
				}	
			$('#commshow').html(htmlString);

		},
		error : function(jqXHR, options, error) {
			//callbackError(jqXHR, options, error);
		}
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