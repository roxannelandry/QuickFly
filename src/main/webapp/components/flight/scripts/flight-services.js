flightApp.factory('flightService', function($resource) {
	return $resource('http://localhost:8080/api/quickfly/flights/flight', {}, {
		getFlightsList : {
			method : 'GET',
			isArray : true,
			params : {
				list: "list[]"
			}
		}
	});
	
});