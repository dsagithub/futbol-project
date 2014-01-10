var API_BASE_URL = "http://localhost:8080/futbol-api/";

$(document).ready(function(e){
getList();
});

function getList() {
	var url = API_BASE_URL + 'club?offset=0&length=5'; 
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		beforeSend: function (request)
		{
			request.withCredentials = true;
			request.setRequestHeader("Authorization", "Basic "+ btoa('admin:admin'));

		},
		success : function(data, status, jqxhr) {
			var response = $.parseJSON(jqxhr.responseText);
			var htmlString = '<table class="table table-bordered table-hover table-striped tablesorter"><thead><tr><th>Id <i class="fa fa-sort"></i></th><th>Nombre <i class="fa fa-sort"></i></th></tr></thead><tbody>';

			$.each(response.clubs, function(i,v){
				var club = v;
				htmlString += '<tr><td>'+club.idClub+'</td><td>'+club.nombre+'</td></tr>'
			})
			htmlString += '</tbody></table><ul class="pager pull-left"><li><a href="#">Previous</a></li><li><a href="#">Next</a></li></ul>';
			$('#listshow').html(htmlString);
		},
		error : function(jqXHR, options, error) {}
		});
}