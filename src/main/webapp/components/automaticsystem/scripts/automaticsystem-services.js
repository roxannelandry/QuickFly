homeApp.factory('automaticSystemService', function($resource) {
  return $resource('http://localhost:8080/api/quickfly/weightsystem', {
  });
  
});