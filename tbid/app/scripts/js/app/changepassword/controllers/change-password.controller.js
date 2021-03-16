'use strict';


function ChangePasswordController($scope,$location,$rootScope,changePasswordService,loginService) {

  $scope.doesntmatchError = false;
  $scope.oldNewPwdSameError = false;
  $scope.data = {};
  $scope.data.oldpassword = '';
  $scope.data.newpassword = '';
  $scope.data.reenternewpassword = '';
  $scope.rootData.error = "";

  
  $scope.doReset = function() {
    $scope.data.oldpassword = '';
    $scope.data.newpassword = '';
    $scope.data.reenternewpassword = '';
    $scope.doesntmatchError = false;
    $scope.oldNewPwdSameError = false;
  };

  $scope.$on("logout", function(){
    $scope.data  = {};
    $scope.showLoader();
    $location.path('/login');
    $scope.hideLoader();
  });


  $scope.check = function() {
    if ($scope.data.oldpassword && $scope.data.newpassword	&& $scope.data.reenternewpassword) {
      $scope.doneButtonEnabled = true;
    } else {
      $scope.doneButtonEnabled = false;
    }
  }

  $scope.doSubmitPassword = function() {

    var pwdObj = {};
    
    pwdObj.oldpassword = $scope.data.oldpassword;
    pwdObj.newpassword = $scope.data.newpassword;
    pwdObj.reenternewpassword = $scope.data.reenternewpassword;
    pwdObj.userId=$rootScope.loggedInId;
    
    if ($scope.data.oldpassword === $scope.data.newpassword) {
      $scope.oldNewPwdSameError = true;
    } else {
      $scope.oldNewPwdSameError = false;
    }

    if (!$scope.oldNewPwdSameError) {
      if ($scope.data.newpassword === $scope.data.reenternewpassword) {
        $scope.doesntmatchError = false;
      } else {
        $scope.doesntmatchError = true;
      }

      if (!$scope.doesntmatchError)
      {
        $scope.rootData.error = "";
        $scope.showLoader();

        changePasswordService.submitChangePassword(pwdObj).then(function(resolve) {
            var successMessage = "Password has been changed successfully!";
            $rootScope.$broadcast("showSuccessMessageOnMainScreen",successMessage);
            $location.path('/');
			$scope.rootData.error = "";
            $scope.hideLoader();
          },
          function(error) {
        	 // console.error(error);
            $scope.rootData.error = "There was some issue updating your password.";
            $scope.hideLoader();
          });
      }
    }

  };


  $scope.checkFieldsChangePassword = function() {
    $scope.$broadcast('showErrorInChangePasswordScreen');
    if ($scope.rootData.error) {
      $scope.doSubmitPassword();
    }
  };


  $scope.reloadMainWall = function(){
	  $rootScope.showSearchBoxFlag=true;
    $location.path('/');
  }

}

ChangePasswordController.$inject = ['$scope','$location','$rootScope','changePasswordService','loginService'];

angular
  .module('tbidApp')
	.controller('changePasswordController',ChangePasswordController);
