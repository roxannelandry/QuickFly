loginApp.factory('loginResource', ["$location", "$cookieStore", "$rootScope", "menuResource", function ($location, $cookieStore, $rootScope, menuResource) {
	
    function setUser(user) {
        $rootScope.isLogin = true;
        $rootScope.user = user;
        $cookieStore.put("user", user);    
        $rootScope.sessionId = null;
        $cookieStore.put("sessionId", null); 
        menuResource.findNumber(user.email);
    }

    function processAdminVerification (data) {
		if(data.userType === "ADMIN"){
			$rootScope.isAdmin = true;
	        $cookieStore.put("admin", true);    
			setUser(data);
			
			$location.path("/admin");

		}else{
			$rootScope.isAdmin = false;
	        $cookieStore.put("admin", false);    
			setUser(data);
			
			$location.path("/home");
		}
    }
    
    return {
        setUser: setUser,
        processAdminVerification : processAdminVerification
    };
    
}]);