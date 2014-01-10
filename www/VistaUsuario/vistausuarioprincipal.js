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
					
				htmlString += '<div class="panel panel-primary"> <div class="panel-heading"><h3 class="panel-title">'+club.idClub;
				htmlString += '</h3> </div> <div class="panel-body">'+club.nombre;
				htmlString += '</div>'
					
        
          
          
          
    
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