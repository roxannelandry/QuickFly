checkoutApp.controller('checkout-controller', function($scope, $rootScope, $cookieStore, menuResource, checkoutService) {
	if ($rootScope.user != null){
		$scope.checkoutEmail = $rootScope.user.email;
	}
	
	$scope.checkout = function() {
		$scope.error = false;
		$scope.showTransactionSection = false;
		$scope.disableBuyButton = false;
		var user = "";
		if($cookieStore.get("user") != null){
		  user = $cookieStore.get("user").email;		
		} else {
		  user = $cookieStore.get('sessionId');
		}
		
		var cartInfo = {
			"user" : user,
			"checkoutEmail" : $scope.checkoutEmail,
			"cartDto" : $rootScope.cart
		}
				
		checkoutService.post(cartInfo, function onSuccess(data) {
		  $scope.cartItems = data.cartItems;      
		  menuResource.resetCart();			
			$scope.transactionDetails = data;
			$scope.showTransactionSection = true;
			$scope.disableBuyButton = true;
			
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
			
		}, function onError(data) {
			$scope.error = true;
			$scope.errorMessage = data.data;
		});
	}
});