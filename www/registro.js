var API_BASE_URL = "http://localhost:8080/futbol-api";


$(document).ready(function(e){
	 var htmlString ='<div style="width: 600px"class="alert alert-info alert-dismissable"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button> Introduce tu login o registrate.</div>';
		
            $('#error').html(htmlString);
            var usuario = $.cookie('usuario');
            console.log(usuario);
            $.removeCookie('usuario');
              console.log(usuario);
          
						
						
});
function mensaje()
{
	BootstrapDialog.alert('Logeate o utiliza el formulario de registro');
}


function login(username,password)
{
var username = $('#username').val();

	var password = $('#password').val();

	var url= "http://localhost:8080/futbol-auth/ServletLogin";
	var datos= 'username='+username+'&password='+password+'';
	
	

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
	
	if (response=="successadmin")
	{
		$.cookie('usuario', username);
	$.cookie('password', password);

	var usuario = $.cookie('usuario');
	var pass = $.cookie('password');
	
		window.location.href="administracion/clubs.html"
	}
	 else if (response=="successusuario"){

	$.cookie('usuario', username,{path: 'VistaUsuario' });
	$.cookie('password', password,{path: 'VistaUsuario' });

	var usuario = $.cookie('usuario');
	var pass = $.cookie('password');
	
	window.location.href="VistaUsuario/clubsusuario.html"

	}
	else if (response=="wrongpass"){
		
		
		var htmlString ='<div class="alert  alert-danger"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button> La password de login que has introducido no es correcta.</div>';
		
            $('#error').html(htmlString);
           
          
	}
		else if (response==""){
		
		
		var htmlString ='<div class="alert  alert-danger"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button> No existe el usuario que has introducido.</div>';
		
            $('#error').html(htmlString);
           
          
	}


		},
		error : function(jqXHR, options, error) {}

		});

}


function registro(usernamer,passwordr,name,email){



	var usernamer = $('#usernamer').val();
	var passwordr = $('#passwordr').val();
	var name = $('#name').val();
	var email = $('#email').val();
	var passwordr2 =$('#passwordr2').val();
	if (passwordr!=passwordr2)
	{
		var htmlString ='<div class="alert  alert-danger"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button> Las passwords no coinciden.</div>';
		
            $('#error').html(htmlString);

	}else
	
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
	console.log(response.status);
	console.log(response);


	
	
if (response.status == undefined){
		var htmlString ='<div class="alert  alert-success"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button> Registro realizado correctamente. Redirigiendo...</div>';
        $('#error').html(htmlString);

        $.cookie('usuario', usernamer);
	   $.cookie('password', passwordr);

	var usuario = $.cookie('usuario');
	var pass = $.cookie('password');

      setTimeout ("redireccionar()", 2000); //tiempo expresado en milisegundos

	}else if (response.status == 404){
	
		        var htmlString ='<div class="alert  alert-danger"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button> El usuario ya existe</div>';
        $('#error').html(htmlString);
}
      

		},
		error : function(jqXHR, options, error) {}

		});

}
   function redireccionar(){
 

	var usuario = $.cookie('usuario');
	var pass = $.cookie('password');
              window.location.href="VistaUsuario/clubsusuario.html"
            }
            
        
	
