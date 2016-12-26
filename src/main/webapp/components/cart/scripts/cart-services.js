cartApp.factory('cartService', function($resource) {
	return $resource('http://localhost:8080/api/quickfly/cart', {}
	, {
		addItemInCart : {
			method : 'POST'
		},
		updateItemInCart : {
			method : 'PUT'
		},
		getItemsInCart : {
			method : 'GET',
			isArray : false,
			params : {
				user : "user"
			}
		},
		deleteCartItem :{
			url : 'http://localhost:8080/api/quickfly/cart/delete',
			method : 'DELETE'
		}
	});

});