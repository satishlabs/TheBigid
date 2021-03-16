'use strict';


angular.module('tbidApp')
  .controller('notificationdetailController', ['$http','$scope','$rootScope','$location','notificationService','loginService','myProfileService',function ($http,$scope,$rootScope,$location,notificationService,loginService,myProfileService) {
	  

	  if(document.getElementById("myDropdown") !== null){
		  document.getElementById("myDropdown").classList.remove("showglobal");//hide the notification menu
	  }

	  $rootScope.showSearchBoxFlag = !myProfileService.isLoggedIn;//hide the search box
	  $scope.isLoggedIn=myProfileService.isLoggedIn;
	  
      var userId = $rootScope.loggedInId;
      var postId = notificationService.notifypostid;

	  notificationService.fetchNotificationDetailUrl(userId,postId).then(function(response){
            $scope.notificationpostinfo = response;
            $scope.notificationpostinfo.display = notificationService.displaySection;            
            $scope.notificationDetailsObj = response.posts;
	    	//$scope.notificationDetailsObj = [{ "creationTimestamp": 1502876354000, "lastModifiedTimestamp": 1502890283000, "id": 5, "title": "TEST123 Post", "body": "body...", "tags": [], "postType": "GENERAL", "postCategory": "BIG", "longitude": 133.77513599999997, "latitude": -25.274398, "imgPath": "", "anonymous": false, "isNSWF": false, "toField": "", "fromField": "", "username": "test123", "userId": "test1232", "postUserInputCategory": "", "location": "Australia", "country": "Australia", "postCreatedTime": 1502876354000, "voteCount": null, "expand": false, "followUser": true, "enableNotificationFromUser": false, "popularity": 2, "eventDate": null, "voteDetails": { "id": 2, "postId": 5, "userId": 1, "voteType": "UP", "upVotedTimeByMe": 1502890274000, "downVotedTimeByMe": null, "pushActivatedTimeByMe": 1502890275000, "reportedIsNSFWTimeByMe": null, "pushActivate": true, "pushActivated": true, "reportedNSFW": false, "reasonForNsfw": null, "reportedIsNSFW": false, "voteCount": 4, "upVote": true, "downVote": false, "pushCount": 1 }, "statusType": "SAVED", "postNotification": true, "savedPostCount": 0, "createdBy": 2, "comments": [], "savedPostByUser": [ { "id": 5, "postId": 5, "userId": 1, "statusType": "SHARED", "sharedTime": 1502890283000, "sharedPostCount": 0, "shared": true } ] }];
	    },function(error){
	    	console.error("There was some issue fetching information from server - notification.controller.js");
	    	console.error(error);
	    	 $scope.rootData.error = "Sorry for the inconvenience.There was some issue fetching information from server.";
	    	  
	    });

	    $scope.showrepliesfunc = function(replyObj){
	    	$scope.showreplies = true;
	    };

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
