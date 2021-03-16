'use strict';

angular.module('tbidApp').controller('userProfilePageController',['$scope', 'myProfileService','$rootScope',function($scope, myProfileService,$rootScope) {
			
			$scope.profileCompletedata = myProfileService.data;
			$scope.showUserDetailsTopBar = false;

			/*Methods created inorder to filter the required posts for the User Profile page - START*/
			$scope.showPostsBasedOnUserLoggedIn = function(post) {
				for (var i = 0; i < myProfileService.data.posts.length; i++) {
					if ($rootScope.userData.loggedInUserId.toLowerCase() === post.userId.toLowerCase()) {
							return true;
					}
				}
			};

			$scope.showPostsBasedOnComments = function(post){
				for (var i = 0; i < myProfileService.data.posts.length; i++) {
					for(var j = 0; j < post.comments.length; j++){
						//get the commentPostedBy from each post compare it to the logged in user and display the post
						if($rootScope.userData.loggedInUserId !== null && post.comments[j].commentPostedBy !== null){
							if ($rootScope.userData.loggedInUserId.toLowerCase() === post.comments[j].commentPostedBy.toLowerCase()) {
								return true;
						    }
						}

						
					}
				}
			};
			/*Methods created inorder to filter the required posts for the User Profile page - END*/
			
		}]);