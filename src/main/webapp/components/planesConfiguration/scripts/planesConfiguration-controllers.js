planesConfigurationApp.controller('planesConfiguration-controller', function($scope, $rootScope, $cookieStore, $location, planesConfigurationService) {
	
	$scope.flightsCargo = new Array();
	$scope.flights = new Array();
	
	planesConfigurationService.getAllFlights({}, function onSuccess(data){
		for (var i = 0; i < data.listOfFlightDto.length; i++) {
			if(data.listOfFlightDto[i].flightCategory == null){
				$scope.flightsCargo.push(data.listOfFlightDto[i]);
				
				var date = data.listOfFlightDto[i].date;
				var dateTime = date.split("T");
				$scope.flightsCargo[$scope.flightsCargo.length - 1].date = dateTime[0];
				$scope.flightsCargo[$scope.flightsCargo.length - 1].departureTime = dateTime[1];
				$scope.flightsCargo[$scope.flightsCargo.length - 1].flightCategory = 'AIR_CARGO';
			}
		}
		for (var i = 0; i < data.listOfFlightDto.length; i++) {
			if(data.listOfFlightDto[i].flightCategory != null && data.listOfFlightDto[i].flightCategory != 'AIR_CARGO'){
				$scope.flights.push(data.listOfFlightDto[i]);
				
				var date = data.listOfFlightDto[i].date;
				var dateTime = date.split("T");
				$scope.flights[$scope.flights.length - 1].date = dateTime[0];
				$scope.flights[$scope.flights.length - 1].departureTime = dateTime[1];
			}
		}
		
		$scope.showAirCargo = $scope.flightsCargo.length > 0;
		
	}, function onError(data){})
	
	$scope.getAirLourdFlights = function () {
		planesConfigurationService.getAirLourdFlights({}, function onSuccess(data){
			$scope.flights = data.listOfFlightDto;
			$scope.flightsCargo = [];
			$scope.showAirCargo = false;
			for (var i = 0; i < data.listOfFlightDto.length; i++) {
				var date = data.listOfFlightDto[i].date;
				var dateTime = date.split("T");
				$scope.flights[i].date = dateTime[0];
				$scope.flights[i].departureTime = dateTime[1];
			}
		}, function onError(data){})
	}
	
	$scope.getAllFlights = function () {
		planesConfigurationService.getAllFlights({}, function onSuccess(data){
		  for (var i = 0; i < data.listOfFlightDto.length; i++) {
        if(data.listOfFlightDto[i].flightCategory == null){
          $scope.flightsCargo.push(data.listOfFlightDto[i]);
        
          var date = data.listOfFlightDto[i].date;
          var dateTime = date.split("T");
          $scope.flightsCargo[$scope.flightsCargo.length - 1].date = dateTime[0];
          $scope.flightsCargo[$scope.flightsCargo.length - 1].departureTime = dateTime[1];
          $scope.flightsCargo[$scope.flightsCargo.length - 1].flightCategory = 'AIR_CARGO';
        }
      }
      for (var i = 0; i < data.listOfFlightDto.length; i++) {
        if(data.listOfFlightDto[i].flightCategory != null && data.listOfFlightDto[i].flightCategory != 'AIR_CARGO'){
          $scope.flights.push(data.listOfFlightDto[i]);
        
          var date = data.listOfFlightDto[i].date;
          var dateTime = date.split("T");
          $scope.flights[$scope.flights.length - 1].date = dateTime[0];
          $scope.flights[$scope.flights.length - 1].departureTime = dateTime[1];
        }
      }
    
      $scope.showAirCargo = $scope.flightsCargo.length > 0;
		}, function onError(data){})
	}
	
	$scope.updateFlight = function(flight){
    flight.maximumExceedingWeight = Math.ceil(flight.maximumExceedingWeight * 2) / 2;
    
	  var flightInfos = {
	    "company" : flight.company,
	    "date" : flight.date + " " + flight.departureTime,
	    "destination" : flight.destination,
	    "source" : flight.source,
	    "maximumExceedingWeight": flight.maximumExceedingWeight
	  }
	 
	 planesConfigurationService.updateFlight(flightInfos,
	   function onSucces(data){
	     successMessage();
	   },function onError(data){
	     processError(data);
	   })
	 }
	
  $scope.options = {
    "true" : true,
    "false": false,
  }
	
	 $scope.setExceedingWeightAllowedPerLuggageToZero = function(flight){
	   if(flight.canExceedWeight == false) {
	     flight.exceedingWeightAllowedPerLuggage = 0;
	   }
   }
   
  function processError(data) {
    $scope.error = true;
    $scope.success = false;
    
    $scope.showWarningFilter = false;
    $scope.showWarningPrice = false
    $scope.errorMessage = data.data;
  }
  
  function successMessage() {
    $scope.success = true;
    $scope.error = false;
    
    $scope.successMessage = "The modifications has been saved!";
  }
  
});