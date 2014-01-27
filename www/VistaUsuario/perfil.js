
var API_BASE_URL = "http://localhost:8080/futbol-api/users";

var userId;


$(document).ready(function(e){
	var user = $.cookie('usuario');
	var pass =  $.cookie('password');
	getUser(user,pass);
	console.log("hola");
	console.log(user);
	
	var htmlString = '<ul class="nav navbar-nav navbar-right navbar-user"><li class="dropdown user-dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown"> <i class="fa fa-user"></i>'+user;
	 htmlString += '<b class="caret"></b></a><ul class="dropdown-menu"> <li class="divider"></li> <li> <a href="perfilusuario.html">Ver perfil</a></li><li onClick="javascript:deletecookie()"><a><i class="fa fa-power-off" ></i> Salir</a></li></ul></li></ul>';
						
	$('#usuario').html(htmlString);	
	
	
});



function deletecookie(){
	console.log("dentro delete cookie");
	$.removeCookie('usuario');
	$.removeCookie('password');
	var usuario = null;
	var password = null;
 window.location.href="../index.html"

}


function postuser(user,pass){
	var user = $.cookie('usuario');
	var pass =  $.cookie('password');
	var passe = $('#pass').val();
	var url1= "http://localhost:8080/futbol-auth/ServletLogin";
	var cpass= 'username='+user+'&password='+passe+'';
	console.log(cpass);
	console.log("nuevopost");

	$.ajax({
		url : url1,
		type : 'POST',
		crossDomain : true,
		data: cpass,
		dataType: 'html',
		beforeSend: function (request)

		{
			request.setRequestHeader('content-type', 'application/x-www-form-urlencoded');
		},
		success : function(ata, status, jqxhr) {
	var response = jqxhr.responseText;
	
	if (response=="successusuario"){

        
		var htmlString ='<div class="alert  alert-success"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>Password correcto</div>';
		
        $('#error').html(htmlString);
    	var user = $.cookie('usuario');
    	console.log("entra1");
    	var pass =  $.cookie('password');
        var url = API_BASE_URL + "/" + user;
    	console.log("entramos en post user antiguo");
        console.log(url);
        var nombre = $('#newname').val();
        var email = $('#newemail').val();
        var username = $('#username').val();
        var passw = $('#newpass').val();
        var passw2 = $('#newpass2').val();
        var password = $('#pass').val();
        console.log("booootooon");
        console.log("entra2");
        var datos = null;
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
                        console.log(response.name);
                        console.log(passw);
                        console.log(passw2);
                        console.log("entra3");
                        if (nombre == ""){
                                nombre= response.name;
                                }
                        if (email == ""){
                                email= response.email;
                                }

                       /* if ((passw != "") && (passw==passw2))
                                {
                            datos = '{"email":"'+email+'","name":"'+nombre+'","password":"'+passw+'"}';
                            console.log(" coincide");
                                }
                     
                        else if ((passw != "") && (passw =!passw2)){
                            BootstrapDialog.confirm('Contraseñas diferentes');
                        	
                        }*/
                        
                        if (passw != "")
                        	{
                        	if (passw==passw2){
                        		 datos = '{"email":"'+email+'","name":"'+nombre+'","password":"'+passw+'"}';
                                 console.log(" coincide");
                        	}
                        	else {
                        		console.log("no coincide");
                                BootstrapDialog.confirm('Passwords diferentes');
                        	}
                        	
                        	}
                        else{
                        	
                            passw == response.password;
                            datos = '{"email":"'+email+'","name":"'+nombre+'"}';
                            console.log("blanco");
                            
                        }
                                
                        console.log(datos);

                        

    console.log("entra5");
    console.log(url);
                        $.ajax({
                                url : url,
                                type : 'PUT',
                                crossDomain : true,
                                data: datos,
                                beforeSend: function (request)
                                {
                                        request.withCredentials = true;
                            			request.setRequestHeader("Authorization", "Basic "+ btoa('admin:admin'));
                            			//request.setRequestHeader("Authorization", "Basic "+ btoa(user+':'+pass));
                                        request.setRequestHeader("Content-Type", "application/vnd.futbol.api.user+json");
                                        request.setRequestHeader("Accept", "application/vnd.futbol.api.user+json");

                                },
                                success : function(data, status, jqxhr) {
                                        var response = $.parseJSON(jqxhr.responseText);
                                        BootstrapDialog.confirm('Informacion de usuario actualizada');
                                        console.log("6a");
                                },
                                error : function(jqXHR, options, error) {
                                    BootstrapDialog.confirm('Informacion de usuario NO actualizada');
                                    console.log("6b");

                                }
                                });
                        
                },
                
                error : function(jqXHR, options, error) {}        
                });
        

	}
	else if (response=="wrongpass"){
		
		//window.location.href="http://localhost:8080/futbol/index.html"
		var htmlString ='<div class="alert  alert-danger"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>Password incorrecto, no se ha actualizado tus datos.</div>';
		
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





function getUser(user,pass) {
	var user = $.cookie('usuario');
	var pass =  $.cookie('password');
	console.log(user + "getuser");
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