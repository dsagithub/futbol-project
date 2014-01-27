var API_BASE_URL = "http://localhost:8080/futbol-api/club/";
var pass;
var user;
var idClub;

$(document).ready(function(e){
	user="admin";
	pass="admin";
	getList();
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

function getList(search) {
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
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));

		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			var htmlString = '<table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>Id</th><th>Nombre</th></tr></thead><tbody>';

			$.each(response.clubs, function(i,v){
				var club = v;
				htmlString += '<tr onClick="javascript:getNoticias('+club.idClub+');"><td>'+club.idClub+'</td><td>'+club.nombre+'</td></tr>'
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
				htmlString += '</tbody></table><ul class="pager"><li class="pull-left" onClick="javascript:getListURL('+prev+')"><a>Anterior</a></li>';
			}else{
				htmlString += '</tbody></table><ul class="pager"><li class="hide pull-left" onClick="javascript:getListURL('+prev+')"><a>Anterior</a></li>';
			}
			if (next!=""){
				htmlString += '<li class="pull-right" onClick="javascript:getListURL('+next+')"><a type="submit" id="next" name="next" >Siguiente</a></li></ul>';
			}else{
				htmlString += '<li class="hide pull-right"><a onClick="javascript:getListURL("'+next+'")">Siguiente</a></li></ul>';
			}			
			$('#listshow').html(htmlString);
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}

function getListURL(url) {
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
			var htmlString = '<table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>Id</th><th>Nombre</th></tr></thead><tbody>';

			$.each(response.clubs, function(i,v){
				var club = v;
				htmlString += '<tr onClick="javascript:getNoticias('+club.idClub+');"><td>'+club.idClub+'</td><td>'+club.nombre+'</td></tr>'
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
				htmlString += '</tbody></table><ul class="pager"><li class="pull-left" onClick="javascript:getListURL('+prev+')"><a>Anterior</a></li>';
			}else{
				htmlString += '</tbody></table><ul class="pager"><li class="hide pull-left" onClick="javascript:getListURL('+prev+')"><a>Anterior</a></li>';
			}
			if (next!=""){
				htmlString += '<li class="pull-right" onClick="javascript:getListURL('+next+')"><a type="submit" id="next" name="next" >Siguiente</a></li></ul>';
			}else{
				htmlString += '<li class="hide pull-right"><a onClick="javascript:getListURL("'+next+'")">Siguiente</a></li></ul>';
			}			
			$('#listshow').html(htmlString);
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}

function getNoticias(id){
	var url;
	idClub = id;
	url = API_BASE_URL + id + '/noticias?offset=0&length=5';
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
			
			var htmlString = '<button type="button" class="btn btn-primary btn-xs pull-right" onClick="javascript:showAddNoticia();">+ Añadir Noticia</button><p><br><table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>Id </th><th>Titulo</th></tr></thead><tbody>';
			$.each(response.noticias, function(i,v){
				var not = v;
				var linkself="'"+not.links[0].uri+"'";
				htmlString += '<tr onClick="javascript:showeditNoticia('+linkself+');"><td>'+not.idNoticia+'</td><td>'+not.titulo+'</td></tr>'
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
				htmlString += '</tbody></table><ul class="pager"><li class="pull-left" onClick="javascript:getNoticiasURL('+prev+')"><a>Anterior</a></li>';
			}else{
				htmlString += '</tbody></table><ul class="pager"><li class="hide pull-left" onClick="javascript:getNoticiasURL('+prev+')"><a>Anterior</a></li>';
			}
			if (next!=""){
				htmlString += '<li class="pull-right" onClick="javascript:getNoticiasURL('+next+')"><a type="submit" id="next" name="next" >Siguiente</a></li></ul>';
			}else{
				htmlString += '<li class="hide pull-right"><a onClick="javascript:getNoticiasURL("'+next+'")">Siguiente</a></li></ul>';
			}

			url = API_BASE_URL + id + '/';
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

					var namehtmlclub = "Noticias del Club " + response.nombre;

					$('#nameclubshow').html(namehtmlclub);	
					$('#clubshow').html(htmlString);	
				},
				error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
				alertify.error("No se ha podido completar la acción");}
			});

		},
		error : function(jqXHR, options, error) {						
			url = API_BASE_URL + id + '/';
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

					var namehtmlclub = "Noticias del Club " + response.nombre;

					$('#nameclubshow').html(namehtmlclub);
					var htmlString = 'Este Club no tiene noticias añadidas<button type="button" class="btn btn-primary btn-xs pull-right" onClick="javascript:showAddNoticia();">+ Añadir Noticia</button><p><br>';	
					$('#clubshow').html(htmlString);
				},
				error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
				alertify.error("No se ha podido completar la acción");}
			});}
		});
}
function getNoticiasURL(url){
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
			
			var htmlString = '<button type="button" class="btn btn-primary btn-xs pull-right" onClick="javascript:showAddNoticia();">+ Añadir Noticia</button><p><br><table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>Id </th><th>Titulo</th></tr></thead><tbody>';
			$.each(response.noticias, function(i,v){
				var not = v;
				var linkself="'"+not.links[0].uri+"'";
				htmlString += '<tr onClick="javascript:showeditNoticia('+linkself+');"><td>'+not.idNoticia+'</td><td>'+not.titulo+'</td></tr>';
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
				htmlString += '</tbody></table><ul class="pager"><li class="pull-left" onClick="javascript:getNoticiasURL('+prev+')"><a>Anterior</a></li>';
			}else{
				htmlString += '</tbody></table><ul class="pager"><li class="hide pull-left" onClick="javascript:getNoticiasURL('+prev+')"><a>Anterior</a></li>';
			}
			if (next!=""){
				htmlString += '<li class="pull-right" onClick="javascript:getNoticiasURL('+next+')"><a type="submit" id="next" name="next" >Siguiente</a></li></ul>';
			}else{
				htmlString += '<li class="hide pull-right"><a onClick="javascript:getNoticiasURL("'+next+'")">Siguiente</a></li></ul>';
			}			
			$('#clubshow').html(htmlString);	
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});

}

function showAddNoticia(){
	var htmlString = '<form><label>Titulo</label><input id="addtitle" class="form-control" required/><label>Contenido</label><textarea id="addcontent" class="form-control" rows="3"></textarea><br><p></form>';
	BootstrapDialog.show({
		title: 'Añadir nueva noticia',
		message: htmlString,
		buttons: [ {
			label: 'Crear',
			cssClass: 'btn-primary',
			action: function(dialogItself){
				addNoticia(),
				dialogItself.close(),
				getNoticias(idClub);
			}
		},  {
			label: 'Cerrar',
			action: function(dialogItself){
				dialogItself.close();
			}
		}]
	});
}

function addNoticia(){

	var url = API_BASE_URL + idClub + '/noticias';
	var titulo = $('#addtitle').val();
	var contenido = $('#addcontent').val();
	var datos = '{"content":"'+contenido+'","titulo":"'+titulo+'","media":""}';
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		data: datos,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
			request.setRequestHeader("Content-Type", "application/vnd.futbol.api.noticia+json");
			request.setRequestHeader("Accept", "application/vnd.futbol.api.noticia+json");

		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			getNoticias(idClub);
			alertify.log("Notification", "error", 5);
			alertify.success("Operación completada correctamente");
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}

function showeditNoticia(url){
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

			var htmlString = '<form><label>Titulo</label><input id="edittitle" class="form-control" value="'+response.titulo+'" required/><label>Contenido</label><textarea id="editcontent" class="form-control" rows="3">'+response.content+'</textarea><br><p></form>';
			BootstrapDialog.show({
				title: 'Añadir nueva noticia',
				message: htmlString,
				buttons: [ {
					label: 'Editar',
					cssClass: 'btn-primary',
					action: function(dialogItself){
						editNoticia(url)
						dialogItself.close(),
						getNoticias(idClub);
					}
				},{
					label: 'Eliminar',
					cssClass: 'btn-warning',
					action: function(dialogItself){
						deleteNoticia(url),
						dialogItself.close(),
						getNoticias(idClub);
					}
				},  {
					label: 'Cerrar',
					action: function(dialogItself){
						dialogItself.close();
					}
				}]
			});	

		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}
function editNoticia(url){
	var titulo = $('#edittitle').val();
	var contenido = $('#editcontent').val();
	var datos = '{"content":"'+contenido+'","titulo":"'+titulo+'","media":"";}';
	$.ajax({
		url : url,
		type : 'PUT',
		crossDomain : true,
		data: datos,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
			request.setRequestHeader("Content-Type", "application/vnd.futbol.api.noticia+json");
			request.setRequestHeader("Accept", "application/vnd.futbol.api.noticia+json");
		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			getNoticias(idClub);
			alertify.log("Notification", "error", 5);
			alertify.success("Operación completada correctamente");
		},
		error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
		alertify.error("No se ha podido completar la acción");}
	});
}
function deleteNoticia(url){
	BootstrapDialog.confirm('Estas seguro que deseas eliminar la noticia?', function(result){
		if(result) {
			$.ajax({
				url : url,
				type : 'DELETE',
				crossDomain : true,
				beforeSend: function (request)
				{
					request.withCredentials = true;
					request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));

				},
				success : function(data, status, jqxhr) {
					getNoticias(idClub);
					alertify.log("Notification", "error", 5);
					alertify.success("Operación completada correctamente");
				},
				error : function(jqXHR, options, error) {alertify.log("Notification", "error", 5);
				alertify.error("No se ha podido completar la acción");}
			});
		}
	});
}