registerApp.factory('registerService',function($resource){
	return $resource('http://localhost:8080/api/quickfly/accounts',{},{
        post:{
            method: 'POST'
        }
    });
	
});