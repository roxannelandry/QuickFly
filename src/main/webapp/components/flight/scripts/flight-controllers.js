flightApp.controller('flight-controller', function($scope, $cookieStore, $rootScope,
		flightService, cartService, menuResource, $stateParams) {

	$scope.flight;
	
	flightService.get({
		source : $stateParams.source,
		destination : $stateParams.destination,
		date : $stateParams.date,
		company : $stateParams.company,
	},
    function onSucces(data){
		$scope.flight = data;
		var date = data.date;
		var dateTime = date.split("T");
		$scope.flight.date = dateTime[0];
		$scope.flight.departureTime = dateTime[1];
		if(data.isAirVivant) {
			$scope.flight.isAirVivant = "Yes";
		} else {
			$scope.flight.isAirVivant = "No";
		}
      }, function onError(data){
    	  processError(data);
      });

  $scope.addToCart = function(company, date, destination, source){
    var user = $cookieStore.get("user");
    if(user == null) {
      user = $cookieStore.get("sessionId");
    } else {
      user = user.email;
    }
    var flightInfos = {
        "company" : company,
        "date" : date,
        "destination" : destination,
        "source" : source,
        "numberOfTickets" : 1,
        "user" : user,
      }
    cartService.addItemInCart(flightInfos, 
      function onSucces(data){
        $scope.error = false;
        $rootScope.numberOfItem = data.numberOfItems;
      }, function onError(data){
        processError(data);
      });
  }

	function processError(data) {
		$scope.error = true;
		$scope.errorMessage = data.data;
	}

});