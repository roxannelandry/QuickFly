homeApp.controller('home-controller', function($scope, homeService,
		homeResource, automaticSystemService, $state, $cookieStore, $rootScope, cartService) {
	
	$scope.source = "";
	$scope.destination = "";
	$scope.date = homeResource.getSystemDate();
	$scope.luggageWeight = 0;
	$scope.showWarningPrice = false;
	$scope.showWarningFilter = false;
	$scope.minDate = homeResource.getSystemDate();
	$scope.numberOfTickets = 1;
	$scope.wantAirVivant = false;
	$scope.airCargoAllowed = false;
	$scope.flightsEconomic;
	$scope.flightsRegular;
	$scope.flightsBusiness;
	$scope.wantEconomic = true;
	$scope.wantRegular = true;
	$scope.wantBusiness = true;
	$scope.showClasses = false
	$scope.flights;
		
	var warningFilterAlwaysHidden = false;
	var previousLuggageWeight = 0;
	var tempDate = new Date;

	
	$scope.switchInfo = function() {
		var tempSource = $scope.source;
		$scope.source = $scope.destination;
		$scope.destination = tempSource;

		if ($scope.isReturn) {
			$scope.date = tempDate
			$scope.isReturn = false;
		} else {
			tempDate = $scope.date;
			$scope.date = homeResource.addDays($scope.date, 2);
			$scope.isReturn = true;
		}
	}

	$scope.roundWeightToUpperHalf = function() {
		$scope.luggageWeight = homeResource.roundWeightToUpperHalf($scope.luggageWeight);
	}

	$scope.autoDetectWeight = function() {
		var detectedValue = automaticSystemService.get();
		detectedValue.$promise.then(function(data) {
			$scope.luggageWeight = parseFloat(data.luggageWeight);
			if (data.luggageWeight < 25) {
				$scope.showClasses = false;
			} else {
				$scope.showClasses = true;
			}
		});
	}

	$scope.hideWarningFilter = function() {
		$scope.showWarningFilter = false;
		warningFilterAlwaysHidden = true;
	}
	
	// Obtenir l'adresse IP et faire l appel au backend pour obtenir la
	// localisation.
	$.getJSON("http://jsonip.com/?callback=?", function (data) {
        $rootScope.ip = data.ip;
    }).done(function() {
    	homeService.getLocationFromIpAdress({
    		"ip" : $rootScope.ip
    	}, function onSucces(data){
    		$scope.source = data.currentCity;
    	}, function onError(data){
    		processError(data);
    	});
    })
    .fail(function() {
    	processError(data);
    });
	
	$scope.searchFlight = function() {		
		$scope.error = false;
		if ($scope.date == null) {
			$scope.date = homeResource.getSystemDate();
		}
		
		homeService.searchFlight({
			"source" : $scope.source,
			"destination" : $scope.destination,
			"date" : homeResource.formatDate($scope.date, $scope),
			"luggageWeight" : $scope.luggageWeight,
			"wantAirVivant" : $scope.wantAirVivant,
			"airCargoAllowed" : $scope.airCargoAllowed,
			"wantEconomic" : $scope.wantEconomic,
			"wantRegular" : $scope.wantRegular,
			"wantBusiness" : $scope.wantBusiness

		}, function onSuccess(data) {
			
			$scope.flightsEconomic = new Array();
			$scope.flightsRegular = new Array();
			$scope.flightsBusiness = new Array();
			$scope.lightFlights = new Array();
			
			for (flight in data.listOfFlightDto) {
				
				var date = data.listOfFlightDto[flight].date;
				var dateTime = date.split("T");
				
				if(data.listOfFlightDto[flight].seatsEconomicAvailable != 0 && $scope.wantEconomic && data.listOfFlightDto[flight].flightCategory != 'AIR_LEGER') {
					data.listOfFlightDto[flight].hasToBeDisable = data.listOfFlightDto[flight].flightCategory == 'AIR_LOURD' && $scope.luggageWeight > data.listOfFlightDto[flight].exceedingWeightAvailable + 65 && !data.listOfFlightDto[flight].hasAirCargo;
					$scope.flightsEconomic.push(data.listOfFlightDto[flight]);
					$scope.flightsEconomic[$scope.flightsEconomic.length - 1].date = dateTime[0];
					$scope.flightsEconomic[$scope.flightsEconomic.length - 1].departureTime = dateTime[1];
				}
				if(data.listOfFlightDto[flight].seatsRegularAvailable != 0 && $scope.wantRegular && data.listOfFlightDto[flight].flightCategory != 'AIR_LEGER') {
					data.listOfFlightDto[flight].hasToBeDisable = data.listOfFlightDto[flight].flightCategory == 'AIR_LOURD' && $scope.luggageWeight > data.listOfFlightDto[flight].exceedingWeightAvailable + 65 && !data.listOfFlightDto[flight].hasAirCargo;
					$scope.flightsRegular.push(data.listOfFlightDto[flight]);
					$scope.flightsRegular[$scope.flightsRegular.length - 1].date = dateTime[0];
					$scope.flightsRegular[$scope.flightsRegular.length - 1].departureTime = dateTime[1];
				}
				if(data.listOfFlightDto[flight].seatsBusinessAvailable != 0 && $scope.wantBusiness && data.listOfFlightDto[flight].flightCategory != 'AIR_LEGER') {
					data.listOfFlightDto[flight].hasToBeDisable = data.listOfFlightDto[flight].flightCategory == 'AIR_LOURD' && $scope.luggageWeight > data.listOfFlightDto[flight].exceedingWeightAvailable + 65 && !data.listOfFlightDto[flight].hasAirCargo;
					$scope.flightsBusiness.push(data.listOfFlightDto[flight]);
					$scope.flightsBusiness[$scope.flightsBusiness.length - 1].date = dateTime[0];
					$scope.flightsBusiness[$scope.flightsBusiness.length - 1].departureTime = dateTime[1];
				}
				if(data.listOfFlightDto[flight].flightCategory == 'AIR_LEGER') {
					if ($scope.wantRegular == true || $scope.wantEconomic == true) {
						$scope.lightFlights.push(data.listOfFlightDto[flight]);
						$scope.lightFlights[$scope.lightFlights.length - 1].date = dateTime[0];
						$scope.lightFlights[$scope.lightFlights.length - 1].departureTime = dateTime[1];
					}
				}
			}

			processWarning(data, $scope.flightsRegular);
			processEmptyListError(data);

		}, function onError(data) {
			$scope.flightsEconomic = null;
			$scope.flightsRegular = null;
			$scope.flightsBusiness = null;
			processError(data);
		});
	}
	
	$scope.addToCart = function(company, date, departureTime, destination, source, luggageWeight, numberOfTickets, index, seatCategory, isLightFlight){
 		var user = $cookieStore.get("user");
 		if(user == null) {
 			user = $cookieStore.get("sessionId");
 		} else {
 		  user = user.email;
 		}
 		var flightInfos = {
 				"company" : company,
 				"date" : date + " " + departureTime,
 				"destination" : destination,
 				"source" : source,
 				"luggageWeight" : luggageWeight,
 				"numberOfTickets" : numberOfTickets,
 				"user" : user,
 				"airCargoAllowed" : $scope.airCargoAllowed,
 				"seatCategory" : seatCategory,
 			}
 		cartService.addItemInCart(flightInfos, 
 			function onSucces(data){
	 			if (seatCategory == 'ECONOMIC') {		 			
	 	 			  $scope.flightsEconomic[index].seatsEconomicAvailable -= numberOfTickets;
	 		 		  if ($scope.flightsEconomic[index].seatsEconomicAvailable <= 0) {
	 		 		    $scope.flightsEconomic.splice(index, 1);
	 		 		  }
	 		 		  if ($scope.flightsEconomic.length > 0 && $scope.flightsEconomic[index].flightCategory == 'AirLourd' && luggageWeight > 65) {
	 		 		    $scope.flightsEconomic[index].exceedingWeightAvailable = $scope.flightsEconomic[index].exceedingWeightAvailable + 65 - luggageWeight;
	 		 		  }
	 			}
	 			else if (seatCategory == 'REGULAR') {
	 				if(isLightFlight){
		 	 			  $scope.lightFlights[index].seatsRegularAvailable -= numberOfTickets;
		 		 		  if ($scope.lightFlights[index].seatsRegularAvailable <= 0) {
		 		 		    $scope.lightFlights.splice(index, 1);
		 		 		  }
		 		 		  if ($scope.lightFlights.length > 0 && $scope.lightFlights[index].flightCategory == 'AirLourd' && luggageWeight > 65) {
		 		 		    $scope.lightFlights[index].exceedingWeightAvailable = $scope.lightFlights[index].exceedingWeightAvailable + 65 - luggageWeight;
		 		 		  }
	 				} else {
	 	 			  $scope.flightsRegular[index].seatsRegularAvailable -= numberOfTickets;
	 		 		  if ($scope.flightsRegular[index].seatsRegularAvailable <= 0) {
	 		 		    $scope.flightsRegular.splice(index, 1);
	 		 		  }
	 		 		  if ($scope.flightsRegular.length > 0 && $scope.flightsRegular[index].flightCategory == 'AirLourd' && luggageWeight > 65) {
	 		 		    $scope.flightsRegular[index].exceedingWeightAvailable = $scope.flightsRegular[index].exceedingWeightAvailable + 65 - luggageWeight;
	 		 		  }
	 				}
	 			}
	 			else if (seatCategory == 'BUSINESS') {
	 				$scope.flights = $scope.flightsBusiness;
	 	 			  $scope.flightsBusiness[index].seatsBusinessAvailable -= numberOfTickets;
	 		 		  if ($scope.flightsBusiness[index].seatsBusinessAvailable <= 0) {
	 		 		    $scope.flightsBusiness.splice(index, 1);
	 		 		  }
	 		 		  if ($scope.flightsBusiness.length > 0 && $scope.flightsBusiness[index].flightCategory == 'AirLourd' && luggageWeight > 65) {
	 		 		    $scope.flightsBusiness[index].exceedingWeightAvailable = $scope.flightsBusiness[index].exceedingWeightAvailable + 65 - luggageWeight;
	 		 		  }
	 			}
		 		  $scope.error = false;
		 		  $rootScope.numberOfItem = data.numberOfItems;
 		}, function onError(data){
 		     processError(data);
 		});
 	}
	
	function processWarning(data, flights) {
		if (previousLuggageWeight != $scope.luggageWeight || warningFilterAlwaysHidden == false) {
			$scope.showWarningFilter = homeResource.showWarningFilter(data);
			$scope.warningFilterMessage = "The results has been filter because of your luggage weight.";
		}

		$scope.showWarningPrice = homeResource.showWarningPrice(flights, $scope.luggageWeight);
		$scope.warningPriceMessage = "The weight of the luggages is too high, addition charges will be applied.";
		previousLuggageWeight = $scope.luggageWeight;
	}
	
	function processError(data) {
		$scope.error = true;
		$scope.showWarningFilter = false;
		$scope.showWarningPrice = false
		$scope.errorMessage = data.data;
	}
	
	function processEmptyListError(data) {
		if (data.listOfFlightDto.length == 0) {
			$scope.error = true;
			$scope.errorMessage = "There is no flight for this traject at the date given.";
		}
	}
	
	$scope.navigate = function(company, date, departureTime, destination, source, wantEconomic, wantRegular, wantBusiness) {
	$scope.changeWeight = function(luggageWeight) {
		if (luggageWeight < 25) {
			$scope.showClasses = false;
		} else {
			$scope.showClasses = true;
		}
	}
		$state.go('flight', {
			company : company,
			date : date + " " + departureTime,
			destination : destination,
			source : source
		});
	}
	
});