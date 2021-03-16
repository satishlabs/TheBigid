'use strict';


angular.module('tbidApp')
  .controller('notificationListController', ['$http','$scope','$rootScope','$location','myProfileService',function ($http,$scope,$rootScope,$location,myProfileService) {
	  
	  if(document.getElementById("myDropdown") !== null){
		  document.getElementById("myDropdown").classList.remove("showglobal");//hide the notification menu
	  }
	  
	  $scope.isLoggedIn=myProfileService.isLoggedIn;
	    
		$scope.reloadMainWall = function() {
			$rootScope.showSearchBoxFlag = true;
			$scope.rootData.error = "";
			$location.path('/');
		}

		$scope.$on("logout", function(){
			    $scope.data  = {};
			    $scope.showLoader();
			    $location.path('/login');
			    $scope.hideLoader();
	    });
		  
  }]);
