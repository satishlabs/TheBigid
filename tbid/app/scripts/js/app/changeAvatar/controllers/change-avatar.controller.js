function changeAvatarController($scope, myProfileService, $rootScope,$location, changeAvatarService,loginService,preLoginService) {

	$scope.profilepicture = myProfileService.userProfilePictureImage;
	$rootScope.changedProfilePicture ='';
		
	$scope.rootData.error = "";

	$scope.reloadMainWall = function() {
		$rootScope.showSearchBoxFlag = true;
		$scope.rootData.error = "";
		$rootScope.changedProfilePicture ='';
		$location.path('/');
	}
	
	$scope.rePaintWallWithUpdatedImage = function() {
		$rootScope.showSearchBoxFlag = true;
		$scope.rootData.error = "";
		
		var sendUserName = preLoginService.loggedInUserName;
		$scope.showLoader();
	    loginService.getProfileInformation(sendUserName).then(function(dataResponse){
	    	
			   $rootScope.userData.profileImagePath = $scope.createUrlForImage(dataResponse.data.avatarImgPath); // fetch the updated image
			   myProfileService.userProfilePictureImage = $rootScope.userData.profileImagePath;
			   
	    },function(error){
			var errorMsg = '';
			if (error.status === 404) {	errorMsg = "Sorry, we are having trouble connecting you to TheBigId, please try again later.";	} 
			else if (error.status === 500) { errorMsg = "Sorry, we are having trouble fetching your Avatar details, please try again later.";} 
			else if (error.status === 503) { errorMsg = "Sorry, we are having trouble connecting you to TheBigId, please try again later.";	} 
			else {	errorMsg = "Sorry, we are having trouble fetching your Avatar details, please try again later.";}
			$scope.rootData.error = errorMsg;
			$rootScope.changedProfilePicture ='';
			$scope.hideLoaderNow();
	    });
	    		
		$location.path('/');//traverse back to main page
	}

	$scope.createUrlForImage = function(avatarImgPath){
		var imageUrl = '';
		imageUrl = $location.protocol() + "://" + $location.host() +":"+ $location.port() + avatarImgPath;
		return imageUrl;
	}
	
	$scope.changeAvatarSubmission = function() {

		$scope.changedProPic = $rootScope.changedProfilePicture;
		if($rootScope.changedProfilePicture !== ''){
			$scope.showLoader();
			changeAvatarService.doChangeAvatar($scope.changedProPic,$rootScope.userData.loggedInUserName).then(doChangeAvatarSuccess, doChangeAvatarError);
			
			function doChangeAvatarSuccess(response) {
				var successMessage = "Avatar has been changed successfully!";
				$rootScope.$broadcast("showSuccessMessageOnMainScreen",successMessage);
				$scope.rePaintWallWithUpdatedImage();
				$scope.rootData.error = "";
				$scope.hideLoader();
				$rootScope.changedProfilePicture ='';
			}

			function doChangeAvatarError(error) {
				var errorMsg = '';
				if (error.status === 404) {
					errorMsg = "Sorry, we are having trouble connecting you to TheBigId, please try again later.";
				} else if (error.status === 500) {
					errorMsg = "Sorry, we are having trouble updating your Avatar, please try again later.";
				} else if (error.status === 503) {
					errorMsg = "Sorry, we are having trouble connecting you to TheBigId, please try again later.";
				} else {
					errorMsg = "Sorry, we are having trouble updating your Avatar, please try again later.";
				}
				$scope.rootData.error = errorMsg;
				$rootScope.changedProfilePicture ='';
				$scope.hideLoaderNow();
			}			
		}


	}

	
	  $scope.$on("logout", function(){
		    $scope.data  = {};
		    $scope.showLoader();
		    $location.path('/login');
		    $scope.hideLoader();
		  });
      
};

changeAvatarController.$inject = [ '$scope', 'myProfileService', '$rootScope',
		'$location', 'changeAvatarService','loginService','preLoginService'];

angular.module('tbidApp').controller('changeAvatarController',
		changeAvatarController);