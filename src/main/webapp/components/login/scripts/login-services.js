loginApp.factory('loginService',function($resource){
    return $resource('http://localhost:8080/api/quickfly/accounts/login',{},{
        post:{
            method: 'POST'
        }
    });
    
});