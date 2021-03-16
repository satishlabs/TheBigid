'use strict';


angular.module('tbidApp')
  .controller('userPostsController', ['$http','$scope','$rootScope','$location','myProfileService','notificationService',function ($http,$scope,$rootScope,$location,myProfileService,notificationService) {
	  
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
		  
	    $scope.toggleExpansion = function(postId) {
	        if($scope.expandedPost==postId) {
	          $scope.expandedPost = -1;
	        } else {
	          $scope.expandedPost = postId;
	        }
	      };
	      
	    $scope.showrepliesfunc = function(replyObj){
	    	$scope.showreplies = true;
	    };
	    
		$scope.userSpecificPosts = [];
		var userId = notificationService.userIdClicked;
		   
		notificationService.fetchUserPosts(userId).then(function(response){
			$scope.data = response;
            //$scope.userSpecificPostsinfo.display = 'displayPostSection';            
            //$scope.userSpecificPosts = response.posts;
            
		},function(error){
			console.error("There was some issue in fetching the user related posts");
			console.error(error);
	    	 $scope.rootData.error = "Sorry for the inconvenience.There was some issue fetching the clicked user related posts";
		});
		
  }]);
