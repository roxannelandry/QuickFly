registerApp.factory('registerResource', ["logoutResource", "$cookieStore", "$rootScope", function (logoutResource, $cookieStore, $rootScope) {

    function setUser(user) {
      logoutResource.clearCookies();
      $rootScope.isLogin = true;
      $rootScope.user = user;
      $cookieStore.put("user", user);        
    }
    
    return {
        setUser: setUser,
    };
    
}]);