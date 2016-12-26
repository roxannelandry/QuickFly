loginApp.controller('login-controller', function ($scope, loginService, loginResource, menuResource) {
	
	$scope.email = "";
	$scope.password = "";
				
	$scope.login = function() {
		$scope.error = false;
		
		var user = {
			"email" : $scope.email,
			"password" : $scope.password,
		}
		loginService.post(user, function onSuccess(data) {
			loginResource.processAdminVerification(data);
			menuResource.resetCart();

		}, function onError(data) {
			$scope.error = true;
			$scope.errorMessage = data.data;
		});
	}
	
});