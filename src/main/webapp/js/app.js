var homeApp = angular.module('quickflyApp', 
		[ 'ui.router', 
		  'ngResource',
		  'ngCookies', 
		  'quickflyApp.menu', 
		  'quickflyApp.commons', 
		  'quickflyApp.home', 
		  'quickflyApp.register',
		  'quickflyApp.login', 
		  'quickflyApp.flight', 
		  'quickflyApp.cart', 
		  'quickflyApp.admin',
		  'quickflyApp.checkout',
		  'quickflyApp.planesConfiguration']);

homeApp.config(function($stateProvider, $httpProvider) {
	$stateProvider.state('home', {
		url : '/home',
		templateUrl : 'components/home/views/home.html',
	}).state('login', {
		url : '/login',
		templateUrl : 'components/login/views/login.html',
	}).state('register', {
		url : '/register',
		templateUrl : 'components/register/views/register.html',
	}).state('cart', {
		url : '/cart',
		templateUrl : 'components/cart/views/cart.html',
	}).state('flight', {
		url : '/flight/:company/:date/:destination/:source',
		templateUrl : 'components/flight/views/flight.html',
	}).state('admin', {
		url : '/admin',
		templateUrl : 'components/admin/views/admin.html',
	}).state('checkout', {
		url : '/checkout',
		templateUrl : 'components/checkout/views/checkout.html',
	}).state('planesConfiguration', {
		url : '/planesConfiguration',
		templateUrl : 'components/planesConfiguration/views/planesConfiguration.html',
	});
}).run([ '$rootScope', '$cookieStore', '$location','$http', "UUIDResource", function($rootScope, $cookieStore, $location, $http, UUIDResource) {
			
	$location.path('/home');

	$rootScope.isLogin = false;
	
	if ($cookieStore.get('user') != null) {
		$rootScope.user = $cookieStore.get('user');
		$rootScope.isLogin = true;	
		$rootScope.isAdmin = $cookieStore.get('admin');
	}
	else if ($cookieStore.get('sessionId') != null) {
	  $rootScope.sessionId = $cookieStore.get('sessionId');
	  $rootScope.isLogin = true;  
	}
	
	else {
		user = UUIDResource.generateUUID();
		$cookieStore.put("sessionId", user);
		$rootScope.sessionId = $cookieStore.get('sessionId');
		$rootScope.isLogin = true;	
	}
	
}]);
