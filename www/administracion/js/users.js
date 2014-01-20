var API_BASE_URL = "http://localhost:8080/futbol-api/users";
var pass;
var user;
var userId;

$(document).ready(function(e){
	user="admin";
	pass="admin";
	getList();
});

function getList(search) {
	var url;
	if (search!=null){
		url = API_BASE_URL + '?offset=0&length=5&pattern='+search; 
		console.log("search")
	}else{
		url = API_BASE_URL + '?offset=0&length=5'; 
		console.log("no search")
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
			var htmlString = '<table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>Id</th><th>Nombre</th><th>Username</th></tr></thead><tbody>';

			$.each(response.users, function(i,v){
				var user = v;
				var username = "'" + user.username + "'";
				htmlString += '<tr onClick="javascript:getUser('+username+');"><td>'+user.idusuario+'</td><td>'+user.name+'</td><td>'+user.username+'</td></tr>'
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
		error : function(jqXHR, options, error) {}
	});
}

function getListURL(url) {
	console.log(url);
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
			var htmlString = '<table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>Id</th><th>Nombre</th><th>Username</th></tr></thead><tbody>';

			$.each(response.users, function(i,v){
				var user = v;
				var username = "'" + user.username + "'";
				htmlString += '<tr onClick="javascript:getUser('+username+');"><td>'+user.idusuario+'</td><td>'+user.name+'</td><td>'+user.username+'</td></tr>'
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
		error : function(jqXHR, options, error) {}
	});
}

function getUser(username){
	userId = username;
	var url = API_BASE_URL + "/" + username;
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
			var namehtmlcampe = "Información del usuario " + response.name;
			var htmlString = '<div class="col-lg-4"><dl class="dl-horizontal"><dt>Username</dt><dd>'+response.username+'</dd><dt>Nombre</dt><dd>'+response.name+'</dd><dt>Email</dt><dd>'+response.email+'</dd></dl></div><button type="button" class="btn btn-danger pull-right" onClick="javascript:delUser()">Eliminar</button><button type="button" class="btn btn-primary pull-right" onClick="javascript:showEdit();">Editar</button>';
			$('#usename').html(namehtmlcampe);
			$('#usershow').html(htmlString);	
			
		},
		error : function(jqXHR, options, error) {}
	});
}

function showEdit(){

	var url = API_BASE_URL + "/" + userId;
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
			var codehtml = '<form><label>Nombre</label><input id="nomed" class="form-control" value="'+ response.name +'" required/><label>Email</label><input id="emailed" class="form-control" value="'+ response.email +'" required/><label>NEW Password</label><input type="password" id="passed" class="form-control" required/></form>';
			BootstrapDialog.show({
            title: 'Editar usuario '+ response.username,
            message: codehtml,
            buttons: [ {
                label: 'Editar',
                cssClass: 'btn-primary',
                action: function(dialogItself){
                    editUser(),
                    location.reload(),
                    getUser(userId),
                    dialogItself.close();                    
                }
            },  {
                label: 'Cerrar',
                action: function(dialogItself){
                    dialogItself.close();
                }
            }]
        });			
		},
		error : function(jqXHR, options, error) {}
	});
}

function editUser(){
	var nombre = $('#nomed').val();
	var email = $('#emailed').val();
	var passw = $('#passed').val();
	var datos;
	if (passw==""){
		datos = '{"email":"'+email+'","name":"'+nombre+'"}';
	}else{
		datos = '{"email":"'+email+'","name":"'+nombre+'","password":"'+passw+'"}';
	}
	var url;
	url = API_BASE_URL + '/' + userId;
	$.ajax({
		url : url,
		type : 'PUT',
		crossDomain : true,
		data: datos,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
			request.setRequestHeader("Content-Type", "application/vnd.futbol.api.user+json");
			request.setRequestHeader("Accept", "application/vnd.futbol.api.user+json");

		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
		},
		error : function(jqXHR, options, error) {}
		});
}

function delUser(){
		BootstrapDialog.confirm('Estas seguro que deseas eliminar el usuario seleccionado?', function(result){
		if(result) {
                //DELTE FUNCTION
        var url;
	url = API_BASE_URL + '/' + userId;
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
			//var response = $.parseJSON(jqxhr.responseText);
			location.reload();
		},
		error : function(jqXHR, options, error) {}
		});
            }
        });

}

function showAddUser(){
		var codehtml = '<form><div class="form-group input-group"><span class="input-group-addon">@</span><input id="addname" name="addname" class="form-control" placeholder="Nombre">';
        codehtml += '<input id="addusername" type="text" class="form-control" placeholder="Username">';
        codehtml += '<input id="addemail" type="text" class="form-control" placeholder="Email"><input id="addpass" type="password" class="form-control" placeholder="Password"></div></form>';

	BootstrapDialog.show({
            title: 'Añadir nuevo Usuario',
            message: codehtml,
            buttons: [ {
                label: 'Crear',
                cssClass: 'btn-primary',
                action: function(dialogItself){
                    addUser(),                    
                    getList(),
                    location.reload(),
                    dialogItself.close();                    
                }
            },  {
                label: 'Cerrar',
                action: function(dialogItself){
                    dialogItself.close();
                }
            }]
        });
}

function addUser(){
	var url = API_BASE_URL;
	var nombre = $('#addname').val();
	var usernam = $('#addusername').val();
	var email = $('#addemail').val();
	var passw = $('#addpass').val();
	var datos = '{"name":"'+nombre+'","email":"'+email+'","username":"'+usernam+'","password":"'+passw+'"}';
	console.log(datos);
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		data: datos,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
			request.setRequestHeader("Content-Type", "application/vnd.futbol.api.user+json");
			request.setRequestHeader("Accept", "application/vnd.futbol.api.user+json");

		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			location.reload();
		},
		error : function(jqXHR, options, error) {}
		});
}