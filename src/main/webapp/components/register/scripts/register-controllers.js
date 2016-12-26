registerApp.controller('register-controller', function($scope, $location, registerService, registerResource, menuResource) {
			
	$scope.email = "";
	$scope.password = "";

	$scope.register = function() {
		$scope.error = false;

		var user = {
			"email" : $scope.email,
			"password" : $scope.password,
		}
		
		registerService.post(user, function onSuccess(data) {
			registerResource.setUser(data);
			$location.path("/home");
			$scope.error = false;
			menuResource.findNumber(user.email);

		}, function onError(data) {
			$scope.error = true;
			$scope.errorMessage = data.data;
		});
	}
	
	$scope.cancelRegistration =  function() {
	  $location.path("/home");
	}
	
});