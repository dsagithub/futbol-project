
var API_BASE_URL = "http://localhost:8080/futbol-api/users";
var pass;
var user;
var userId;
user="judit";
pass="judit";

$(document).ready(function(e){
	getUser(user,pass);
});




function postuser(user,pass){
	user="judit";
	pass="judit";
	var url = API_BASE_URL + "/" + user;
	
	var nombre = $('#newname').val();
	var email = $('#newemail').val();
	var username = $('#username').val();
	var passw = $('#newpass').val();
	var pass = $('#pass').val();
	console.log("booootooon");
	console.log(nombre);
	var datos = null;
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa('judit:judit'));

		},
		
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			console.log(response.name);
			
			if (nombre == ""){
				nombre= response.name;
				}
			if (email == ""){
				email= response.email;
				}

			if (passw == "")
				{
				passw == response.password;
				datos = '{"email":"'+email+'","name":"'+nombre+'"}';
				}
			else{
				datos = '{"email":"'+email+'","name":"'+nombre+'","password":"'+passw+'"}';
			}
				
			console.log(datos);
			console.log(pass);
			console.log(user);

			$.ajax({
				url : url,
				type : 'PUT',
				crossDomain : true,
				data: datos,
				beforeSend: function (request)
				{
					request.withCredentials = true;
					request.setRequestHeader("Authorization", "Basic "+ btoa('admin:admin'));
					request.setRequestHeader("Content-Type", "application/vnd.futbol.api.user+json");
					request.setRequestHeader("Accept", "application/vnd.futbol.api.user+json");

				},
				success : function(data, status, jqxhr) {
					var response = $.parseJSON(jqxhr.responseText);
					BootstrapDialog.confirm('Informacion de usuario actualizada');

				},
				error : function(jqXHR, options, error) {}
				});
			
		},
		
		error : function(jqXHR, options, error) {}	
		});
	
	console.log(datos);


	
	
	
}


function getUser(user,pass) {
	user="judit";
	pass="judit";
	userId = user;
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
			var namehtmlcampe = "Información del usuario " + response.name;
			var htmlString = '<div class="col-lg-4"><dl class="dl-horizontal"><dt>Username</dt><dd>'+response.username+'</dd><dt>Nombre</dt><dd>'+response.name+'</dd><dt>Email</dt><dd>'+response.email+'</dd></dl></div>';
			var nombre = '<input type="text" class="form-control" id="newname" placeholder="' + response.name +'">';
			var Email= '<input type="text" class="form-control" id="newemail" placeholder="' + response.email +' ">';
	
			$('#nameshow').html(nombre);
			$('#Emailshow').html(Email);

			


		},
		error : function(jqXHR, options, error) {}
	});
	
}