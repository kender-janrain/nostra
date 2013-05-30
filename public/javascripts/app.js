$(document).ready(function() {
  $('#janrainEngageEmbed').hide();

  $('#signin').click(function() {
    $('#janrainEngageEmbed').show();
  });
});

function UserController($scope, $http) {
	//$httpProvider.defaults.headers.post = { 'Content-Type': 'application/x-www-form-urlencoded' };

  $scope.user = {};
  $scope.user.name = '';
  $scope.user.artists = [
						         { name: "Rihanna", checked: false },
						         { name: "Nickelback", checked: false },
						         { name: "Mötörhead", checked: false }
	                 ];
	$scope.submit = function() {
    angular.forEach($scope.user.artists, function(artist) {
      $scope.user.artists.push({ artist: artist.name, checked: artist.checked });
    });

    var xsrf = $.param({ name: $scope.user.name, artists: $scope.user.artists });

		$http({ 
      method: 'POST',
      url: '/guess',
      data: xsrf,
      headers: {'Content-Type': 'application/x-www-form-urlencoded'}
    }).success(function() {
      console.log('SUCCESS');
      console.log(xsrf);
      $scope.user = {};
    }).error(function() {
      console.log('ERROR');
      console.log(xsrf);
      $scope.user = {};
    });
	}
}
/*
function GuessController($scope, $http) {
  $scope.returnData = { age: 'Joe Bloe', gender: 'Male' };

  $scope.fetch = function() {
    $http({
      method: 'GET',
      url: '/guess'
    }).success(function(data) {
      console.log('SUCCESS');
      console.log(data);
      $scope.returnData = data;      
    }).error(function(data) {
      console.log('ERROR');
      console.log(data);
    })
  }
}*/