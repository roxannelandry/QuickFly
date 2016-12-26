homeApp.factory('homeService', function($resource) {
	return $resource('http://localhost:8080/api/quickfly/flights/search/:id',{id:'@id'}, {
		searchFlight : {
			method : 'GET',
			isArray: false,
			params : {
				source : "source",
				destination : "destination",
				date : "date",
				luggageWeight : "luggageWeight",
				wantAirVivant : "wantAirVivant",
				wantEconomic : "wantEconomic",
				wantRegular : "wantRegular",
				wantBusiness : "wantBusiness"
			}
		},
	
		getLocationFromIpAdress : {
			url: "http://localhost:8080/api/quickfly/localization",
			method : 'GET'
							
		}
	});
	
});