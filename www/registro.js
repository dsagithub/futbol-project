var API_BASE_URL = "http://localhost:8080/futbol-api";






function login(username,password)
{
var username = $('#username').val();

	var password = $('#password').val();

	var url= "http://localhost:8080/futbol-auth/ServletLogin";
	var datos= 'username='+username+'&password='+password+'';
	$.cookie('usuario', username);
	$.cookie('password', password);

	var usuario = $.cookie('usuario');
	var pass = $.cookie('password');
	console.log(pass);
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		data: datos,
		dataType: 'html',
		beforeSend: function (request)

		{
			request.setRequestHeader('content-type', 'application/x-www-form-urlencoded');
		},
		success : function(ata, status, jqxhr) {
	var response = jqxhr.responseText;
	//console.log("dentro");
	console.log(response);
	console.log("login ajax ok");
	if (response=="successadmin")
	{
		window.location.href="http://localhost:8080/futbol/administracion/clubs.html"
	}
	 else if (response=="successusuario"){

	
	
	window.location.href="http://localhost:8080/futbol/VistaUsuario/clubsusuario.html"

	}
	else if (response=="wrongpass"){
		
		//window.location.href="http://localhost:8080/futbol/index.html"
		var htmlString ='<div class="alert  alert-danger"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button> La password de login que has introducido no es correcta.</div>';
		
            $('#error').html(htmlString);
           
          
	}
		else if (response==""){
		
		//window.location.href="http://localhost:8080/futbol/index.html"
		var htmlString ='<div class="alert  alert-danger"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button> No existe el usuario que has introducido.</div>';
		
            $('#error').html(htmlString);
           
          
	}


		},
		error : function(jqXHR, options, error) {}

		});

}


function registro(usernamer,passwordr,name,email){
	console.log("dentro funcion registrar");

	var usernamer = $('#usernamer').val();
	var passwordr = $('#passwordr').val();
	var name = $('#name').val();
	var email = $('#email').val();
	var passwordr2 =$('#passwordr2').val();
	if (passwordr!=passwordr2)
	{
		var htmlString ='<div class="alert  alert-danger"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button> Las passwords no coinciden.</div>';
		
            $('#error').html(htmlString);

	}
	var url= "http://localhost:8080/futbol-auth/ServletRegister";
	var datos= 'usernamer='+usernamer+'&passwordr='+passwordr+'&name='+name+'&email='+email+'';
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		data: datos,
		dataType: 'html',
		success : function(data, status, jqxhr) {
	var response = $.parseJSON(jqxhr.responseText);
	
	
	if (response.status = 404){
		var htmlString ='<div class="alert  alert-success"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button> Registro realizado correctamente. Redirigiendo...</div>';
        $('#error').html(htmlString);

        $.cookie('usuario', usernamer);
	   $.cookie('password', passwordr);

	var usuario = $.cookie('usuario');
	var pass = $.cookie('password');
	console.log(usuario);
	console.log(pass);
      setTimeout ("redireccionar()", 2000); //tiempo expresado en milisegundos

	}else{
		console.log("dentro else");
		        var htmlString ='<div class="alert  alert-danger"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button> El usuario ya existe</div>';
        $('#error').html(htmlString);
}
      

		},
		error : function(jqXHR, options, error) {}

		});

}
   function redireccionar(){
   	console.log("dentro redireccionar");
   	console.log(usuario);


	var usuario = $.cookie('usuario');
	var pass = $.cookie('password');
              window.location.href="http://localhost:8080/futbol/VistaUsuario/clubsusuario.html"
            }
            
        
	
