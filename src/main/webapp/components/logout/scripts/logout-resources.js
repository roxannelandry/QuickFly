menuApp.factory('logoutResource', ["$cookieStore", "$rootScope", "UUIDResource", function ($cookieStore, $rootScope, UUIDResource) {

    function clearCookies() {
        $rootScope.isLogin = false;
        $rootScope.user = null;

        $cookieStore.remove("user", $rootScope.user);
        $rootScope.admin = false;
        
        $rootScope.sessionId = null;
        $cookieStore.remove("sessionId", $rootScope.sessionId);
    }
    
    function createAnonymousSession() {
        $rootScope.isLogin = true;
        user = UUIDResource.generateUUID();
		$cookieStore.put("sessionId", user);
		$rootScope.sessionId = $cookieStore.get('sessionId');
		$rootScope.isLogin = true;	
    }

    return {
    	clearCookies: clearCookies,
    	createAnonymousSession: createAnonymousSession
    };
    
}])
