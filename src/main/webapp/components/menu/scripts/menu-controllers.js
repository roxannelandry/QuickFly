menuApp.controller('menu-controller', function($scope, $rootScope, $cookieStore, $location, logoutResource, menuResource) {
	
	$scope.logout = function(id) {
		logoutResource.clearCookies();
		logoutResource.createAnonymousSession();
        $location.path("/home");
    	menuResource.resetCart();
	}
	
	$scope.showAdministratorOptions = function() {
		$location.path("/admin");
	}

	$rootScope.numberOfItem = 0;

	var user;

	if ($cookieStore.get('user') != null) {
		user = $cookieStore.get('user').email;
	} else if ($cookieStore.get('sessionId') != null) {
		user = $cookieStore.get('sessionId');
	} else {
		return;
	}

	menuResource.findNumber(user);

});