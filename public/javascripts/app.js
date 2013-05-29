function ArtistController($scope, $http) {
	$scope.payload = [];

  $scope.artists = [
						         { name: "Rihanna", checked: false },
						         { name: "Nickelback", checked: false },
						         { name: "Mötörhead", checked: false }
	                 ];
	$scope.submit = function() {
    angular.forEach($scope.artists, function(artist) {
      $scope.payload.push({ artist: artist.name, checked: artist.checked });
    });

		$http({
      method: 'POST',
      url: '/guess',
      data: $scope.postData
    }).success(function() {
      console.log('SUCCESS');
      console.log($scope.payload);
    }).error(function() {
      console.log('ERROR');
      console.log($scope.payload);
    });
	}
}