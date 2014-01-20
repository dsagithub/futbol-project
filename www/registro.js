var API_BASE_URL = "http://localhost:8080/futbol-api";






function login(username,password)
{
var username = $('#username').val();
	console.log(username);
	var password = $('#password').val();
	console.log(password);
	url= "http://localhost:8080/futbol-auth/ServletLogin";
	var datos= 'username='+username+'&password='+password+'';
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		data: datos,
		beforeSend: function (request)

		{
			request.setRequestHeader('content-type', 'application/x-www-form-urlencoded');
		},
		success : function(data, status, jqxhr) {
			//var response = $.parseJSON(jqxhr.responseText);
			

	console.log("login ajax ok");
	$.cookie('usuario', username);
	var usuario = $.cookie('usuario');
	console.log(usuario);

	window.location.href="http://localhost:8080/futbol/VistaUsuario/clubsusuario.html"



			
			
		},
		error : function(jqXHR, options, error) {}

		});

}

