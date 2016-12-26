cartApp.controller('cart-controller', function($scope, $rootScope, $cookieStore, cartService) {
	
	if ($cookieStore.get('user') != null) {
		user = $cookieStore.get('user').email;
	}
	else if ($cookieStore.get('sessionId') != null){
		user = $cookieStore.get('sessionId');
	} 
	else {
		return;
	}
	
	cartService.getItemsInCart({
		"user" : user
		}, function onSuccess(data) {
		  $rootScope.cart = data;
		  $scope.cartItems = data.cartItems;
		  for(var i = 0; i < $scope.cartItems.length; i++) {
			  var seatCategory = data.cartItems[i].seatCategory;
			  var seatCategory = seatCategory.toLowerCase();
			  var seatCategory = seatCategory.charAt(0).toUpperCase() + seatCategory.slice(1);
			  $scope.cartItems[i].seatCategory = seatCategory;
		  }
		  $scope.showCheckout = true;
		  $scope.hasItemInCart = true;
		  if(data.cartItems.length == 0){
		    $scope.showCheckout = false;
		    $scope.error = true;
		    $scope.errorMessage = "There is no item in this cart";
		    $scope.hasItemInCart = false;
		  }
		  else {
		    for (var i = 0; i < data.cartItems.length; i++) {
		      var date = data.cartItems[i].date;
		      var dateTime = date.split("T");
		      $scope.cartItems[i].date = dateTime[0];
		      $scope.cartItems[i].departureTime = dateTime[1];
		      if ($scope.cartItems[i].hasAirCargo) {
		        $scope.cartItems[i].airCargoDate = $scope.cartItems[i].airCargoDate.replace("T"," ");
		      } else {
		        $scope.cartItems[i].airCargoDate = "-";
		      }
		    }
		  }
	  }, function onError(data) {
	    $scope.error = true;
	    $scope.errorMessage = data.data;
		});
	
	$scope.updateToCart = function(source, destination, date, departureTime, company, luggageWeight, numberOfTickets, seatCategory){
 		var user = $cookieStore.get("user");
 		if(user == null) {
 			user = $cookieStore.get("sessionId");
 		} else {
 		  user = user.email;
 		}
 		var cartItem = {
 				"user" : user,
 				"company" : company,
 				"date" : date + " " + departureTime,
 				"destination" : destination,
 				"source" : source,
 				"numberOfTickets": numberOfTickets,
 				"seatCategory" : seatCategory.toUpperCase(),
 				"luggageWeight" : luggageWeight
 			}
 		cartService.updateItemInCart(cartItem,
 		  function onSucces(data){
 			$rootScope.cart = data;
 			  $scope.cartItems = data.cartItems;
 			  $rootScope.numberOfItem = data.numberOfItems;
 			  $scope.showCheckout = true;
 			  $scope.error = false;
 			  $scope.hasItemInCart = true;
 			  
 			  if(data.cartItems.length == 0){
 			    $scope.showCheckout = false;
 			    $scope.error = true;
 			    $scope.errorMessage = "There is no item in this cart";
 			    $scope.hasItemInCart = false;
 			  }
 			  else {
 		  		  for(var i = 0; i < $scope.cartItems.length; i++) {
 				      var date = data.cartItems[i].date;
 	 			      var dateTime = date.split("T");
 	 			      $scope.cartItems[i].date = dateTime[0];
 	 			      $scope.cartItems[i].departureTime = dateTime[1];
 	 			      if ($scope.cartItems[i].hasAirCargo) {
 	 			        $scope.cartItems[i].airCargoDate = $scope.cartItems[i].airCargoDate.replace("T"," ");
 	 			      } else {
 	 			        $scope.cartItems[i].airCargoDate = "-";
 	 			      }
 	 			      var seatCategory = data.cartItems[i].seatCategory;
 	 			      var seatCategory = seatCategory.toLowerCase();
 	 			      var seatCategory = seatCategory.charAt(0).toUpperCase() + seatCategory.slice(1);
 	 			      $scope.cartItems[i].seatCategory = seatCategory;
 		  		  }
 			  }
 			},function onError(data){
 			  $scope.error = true;
 			  $scope.errorMessage = data.data;
 			  $scope.showCheckout = false;
 			  processError(data);
 			});
 		}
	
	$scope.deleteCartItem = function(source, destination, date, departureTime, company, luggageWeight, numberOfTickets, index, seatCategory){
 		var user = $cookieStore.get("user");
 		if(user == null) {
 			user = $cookieStore.get("sessionId");
 		} else {
 		  user = user.email;
 		}
 		var cartItem = {	
 				"user" : user,
 				"company" : company,
 				"date" : date + " " + departureTime,
 				"destination" : destination,
 				"source" : source,
 				"seatCategory" :  seatCategory.toUpperCase(),
 				"luggageWeight" : luggageWeight
 			}
 		cartService.deleteCartItem(cartItem,
 		  function onSucces(data){
 			$rootScope.cart = data;
 			$scope.cartItems.splice(index, 1);
 			$rootScope.numberOfItem = data.numberOfItems;
		    $scope.error = false;
			cartService.getItemsInCart({
				"user" : user
				}, function onSuccess(data) {
				  $rootScope.cart = data;
				  $scope.cartItems = data.cartItems;		  
				  $scope.showCheckout = true;
	 			  $scope.hasItemInCart = true;
				  if(data.cartItems.length == 0){
				    $scope.showCheckout = false;
				    $scope.error = true;
				    $scope.errorMessage = "There is no item in this cart";
	 			    $scope.hasItemInCart = false;
				  }
				  else {
				    for (var i = 0; i < data.cartItems.length; i++) {
				      var date = data.cartItems[i].date;
				      var dateTime = date.split("T");
				      $scope.cartItems[i].date = dateTime[0];
				      $scope.cartItems[i].departureTime = dateTime[1];
				      if ($scope.cartItems[i].hasAirCargo) {
                $scope.cartItems[i].airCargoDate = $scope.cartItems[i].airCargoDate.replace("T"," ");
              } else {
                $scope.cartItems[i].airCargoDate = "-";
              }
              var seatCategory = data.cartItems[i].seatCategory;
              var seatCategory = seatCategory.toLowerCase();
              var seatCategory = seatCategory.charAt(0).toUpperCase() + seatCategory.slice(1);
              $scope.cartItems[i].seatCategory = seatCategory;
				    }
				  }
			  }, function onError(data) {
			    $scope.error = true;
			    $scope.errorMessage = data.data;
				});
 			
 		}, function onError(data){
		       $scope.error = true;
 		       $scope.errorMessage = data.data;
 		       processError(data);
 	    });
 	}
 	
 	function processError(data) {
    $scope.error = true;
    $scope.showWarningFilter = false;
    $scope.showWarningPrice = false
    $scope.errorMessage = data.data;
  }
  $scope.showCheckout = $scope.error;
  
});