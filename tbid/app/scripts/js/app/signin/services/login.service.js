
function LoginService($http, $q, $rootScope, ajaxService, loginUrlService,base64Service) {

	return {
		doLogout : doLogout,
		doLogin : doLogin,
		checkLoggedStatus : checkLoggedStatus,
		fetchUserProfilePicture : fetchUserProfilePicture,
		getProfileInformation : getProfileInformation
	};

	function doLogout(userId) {
		var deferred = $q.defer();
		var url = loginUrlService.getLogoutUrl(userId);

		ajaxService.doGet(url, {}).then(function(response) {
			deferred.resolve(response);

		}, function(rejectError) {
			deferred.reject(rejectError);
		});
		return deferred.promise;
	}

	function doLogin(data) {
		var deferred = $q.defer();
		var url = loginUrlService.getLoginUrl();
		var headers = {
			"x-requested-with" : "XMLHttpRequest"
		};

        /*Uncomment me to encrypt the value - START*/
	    //data.password = base64Service.doEncryption(data.password);
	    /*Uncomment me to encrypt the value - END*/
	    
		ajaxService.doLoginPost(url, {}, data).then(function(response) {
			deferred.resolve(response);
		}, function(rejectError) {
			deferred.reject(rejectError);
		});
		return deferred.promise;
	}

	function checkLoggedStatus() {
		var deferred = $q.defer();

		var url = loginUrlService.getcheckLoggedStatusUrl();
		ajaxService.doGet(url, {}).then(function(resolution) {
			deferred.resolve(true);
		}, function(error) {
			deferred.reject(error);
		});
		return deferred.promise;
	}

	function fetchUserProfilePicture(userData) {

		var deferred = $q.defer();

		$http({
			method : 'GET',
			url : "",
			data : userData
		}).then(function(resolution) {
			deferred.resolve(true);
		}, function(error) {
			deferred.reject(error);
		});
		deferred.resolve(true);
		return deferred.promise;

	}

	function getProfileInformation(sentUserName) {

		var deferred = $q.defer();
		var urlString = '';
		if ($rootScope.isStubbedMode) {
			urlString = "./temp/json/userDetails.json";
		} else {
			urlString = "/user/" + sentUserName;
		}

		$http({
			method : 'GET',
			url : urlString,
			accept : "application/json"
		}).then(function(resolution) {
			deferred.resolve(resolution);
		}, function(error) {
			deferred.reject(error);
		});
		return deferred.promise;

	}

}

angular.module('tbidApp').factory('loginService', LoginService);

LoginService.$inject = [ '$http', '$q', '$rootScope', 'ajaxService','loginUrlService','base64Service' ];
