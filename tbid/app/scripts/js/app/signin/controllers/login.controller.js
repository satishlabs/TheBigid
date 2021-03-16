'use strict';

angular.module('tbidApp')
		.controller('LoginCtrl',['$scope', 'loginService', '$location', 'preLoginService','myProfileService','$rootScope',function($scope, loginService, $location, preLoginService,myProfileService,$rootScope) {

					if ($scope.rootData.isLoggedIn) {
						$location.path("/");
					}

					$scope.data = {};
					$scope.rootData.error = '';

					$scope.doSubmit = function() {

						preLoginService.loggedInUserName = $scope.data.username;

						$scope.rootData.error = "";
						$scope.showLoader();

						loginService.doLogin($scope.data).then(function(resolution) {
											$scope.rootData.isLoggedIn = true;
											loginService.getProfileInformation(preLoginService.loggedInUserName).then(function(dataResponse){
																			$rootScope.loggedInId = dataResponse.data.id;
																			myProfileService.showNotificationsGlobal($rootScope.loggedInId);
																			
																		}, 
																		function(error){
																				console.error("There was some issue while populating or fetching notifications");
																				$rootScope.loggedInId = '';
																				$location.path("/");
																		});
											
											},function(error) {

													var errorMsg = '';
													if (error.status === 401) {
														errorMsg = "The user name or password you entered is incorrect.";
													}
													else{
														errorMsg = "Sorry, we are having trouble connecting you to TheBigId, Please try again later.";
													}

													$scope.rootData.error = errorMsg;
													$scope.hideLoaderNow();
											});
						};

					$scope.check = function() {
						if ($scope.data.username && $scope.data.password) {
							$scope.submitEnabled = true;
						} else {
							$scope.submitEnabled = false;
						}
					};

					$scope.hideLoader();

				}]);
