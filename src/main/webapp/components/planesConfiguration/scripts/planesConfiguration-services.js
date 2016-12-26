planesConfigurationApp.factory('planesConfigurationService',function($resource){
  return $resource('http://localhost:8080/api/quickfly/flights',{},{
    
    getAllFlights:{
      method: 'GET'
    }, 
    
    getAirLourdFlights: {
      url : 'http://localhost:8080/api/quickfly/flights/AirLourd',
      method : 'GET'
    },
    
    updateFlight: {
      url : 'http://localhost:8080/api/quickfly/flights/updateFlight',
      method : 'PUT'
    }
  });  
});