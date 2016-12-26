menuApp.factory('menuResource', [ "cartService","$rootScope", function(cartService, $rootScope) {
	
	function findNumber(user){	
		cartService.getItemsInCart({
			"user" : user
		}, function onSuccess(data) {
			$rootScope.numberOfItem = data.numberOfItems;

		}, function onError(data) {
			$rootScope.numberOfItem = 0;
		});
	}
	
	function resetCart(){	
		$rootScope.numberOfItem = 0;
	}

	return {
		findNumber:findNumber,
		resetCart : resetCart
	};
	
}]);