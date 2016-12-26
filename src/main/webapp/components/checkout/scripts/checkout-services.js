checkoutApp.factory('checkoutService',function($resource){
    return $resource('http://localhost:8080/api/quickfly/checkout',{},{
        post:{
            method: 'POST'
        }
    });
    
});