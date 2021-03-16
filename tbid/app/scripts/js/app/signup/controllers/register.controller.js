function RegisterController($scope, registerService, $window, $location,
		preLoginService, loginService) {
	if ($scope.rootData.isLoggedIn) {
		$location.path("/");
	}
	$scope.rootData.error = '';
	var currentDate = new Date();
	$scope.submitEnabled = false;
	$scope.data = {};

	$scope.dateOptions = {
		formatYear : 'yy',
		maxDate : new Date(),
		initDate : new Date(currentDate.getFullYear() - 18, currentDate
				.getMonth(), currentDate.getDate()),
		startingDay : 1
	};

	$scope.popup = {
		opened : false
	};

	$scope.open = function() {
		$scope.popup.opened = true;
	};

	$scope.check = function() {
		if ($scope.data.username && $scope.data.password
				&& $scope.data.passwordConfirm && $scope.data.email) {
			$scope.submitEnabled = true;
		} else {
			$scope.submitEnabled = false;
		}
	};

	$scope.doSubmit = function() {

		preLoginService.loggedInUserName = $scope.data.username;

		if ($window.fullPlace !== undefined) {

			$scope.data.fullAddress = $window.fullPlace.formatted_address;
			$scope.data.country = $window.locationCountryGlobalVariable;
		}

		$scope.data.userlatitude = $window.latitudeGlobalVariable;
		$scope.data.userlongitude = $window.longitudeGlobalVariable;

		if (!$scope.submitEnabled) {
			return;
		}

		$scope.rootData.error = "";
		$scope.showLoader();

		registerService.doRegister($scope.data).then(doRegisterSuccess,
				doRegisterError);// REGISTRATION SERVICE CALL

		function doRegisterSuccess(response) {
			loginService.doLogin($scope.data)
					.then(doLoginSuccess, doLoginError);// LOGIN SERVICE CALL
		}

		function doRegisterError(error) {
			var errorMsg = '';
			if (error.status === 404) {
				errorMsg = "Sorry, we are having trouble connecting you to TheBigId, please try again later.";
			} else if (error.status === 500) {
				errorMsg = "An internal error occurred on the server. This may be because of an application error or configuration problem.";
			} else if (error.status === 503) {
				errorMsg = "Sorry, we are having trouble connecting you to TheBigId, please try again later.";
			} else {
				errorMsg = "We were unable to process your registration at this time. We apologise for the inconvenience. Please try again later.";
			}

			$scope.rootData.error = errorMsg;
			$scope.hideLoaderNow();
		}

		function doLoginSuccess(resolution) {

			$scope.rootData.isLoggedIn = true;
			$location.path("/");

		}

		function doLoginError(error) {
			var errorMsg = '';
			if (error.status === 401) {
				errorMsg = "The user name or password you entered is incorrect.";
			}
			if (error.status === 404) {
				errorMsg = "An internal error occurred on the server. This may be because of an application error or configuration problem";
			}
			if (error.status === 500) {
				errorMsg = "An internal error occurred on the server. This may be because of an application error or configuration problem";
			}
			if (error.status === 503) {
				errorMsg = "Sorry, we are having trouble connecting you to TheBigId, Please try again later.";
			}
			$scope.rootData.error = errorMsg;
		}

	};

	$scope.initializeGoogleLocations = function() {

		$window.addEventListener(
						'load',
						function() {
							var script = document.createElement('script');
							script.type = 'text/javascript';
							script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyB980f7lPB9Cek3g68AoFsjq6g5fHwsdsg&v=3.exp&libraries=places';
							document.body.appendChild(script);
						});

		var address = (document.getElementById('draft-location'));
		var autocomplete = new google.maps.places.Autocomplete(address);
		autocomplete.setTypes([ 'geocode' ]);
		google.maps.event.addListener(autocomplete,	'place_changed',function() {
							var place = autocomplete.getPlace();
							if (!place.geometry) {
								return;
							}

							var address = '';
							if (place.address_components) {
								address = [
										(place.address_components[0] && place.address_components[0].short_name || ''),
										(place.address_components[1] && place.address_components[1].short_name || ''),
										(place.address_components[2] && place.address_components[2].short_name || '') ]
										.join(' ');

								$window.locationGlobalVariable = address;

								// This check is done. As the last component in
								// Indian addresses, would be having pincode in
								// them. Hence considering the last string value
								// as country
								if (isNaN(place.address_components[place.address_components.length - 1].long_name)) {
									$window.locationCountryGlobalVariable = place.address_components[place.address_components.length - 1].long_name;
								} else if (isNaN(place.address_components[place.address_components.length - 2].long_name)) {
									$window.locationCountryGlobalVariable = place.address_components[place.address_components.length - 2].long_name;
								}

							}

							document.getElementById('latitude').innerHTML = place.geometry.location.lat();
							document.getElementById('longitude').innerHTML = place.geometry.location.lng();
							$window.latitudeGlobalVariable = place.geometry.location.lat();
							$window.longitudeGlobalVariable = place.geometry.location.lng();
							$window.fullPlace = place;
						});

		// ## initialize method contents - END
	};

	$scope.hideLoader();

}

RegisterController.$inject = [ '$scope', 'registerService', '$window',
		'$location', 'preLoginService', 'loginService' ];

angular.module('tbidApp').controller('RegisterCtrl', RegisterController);
