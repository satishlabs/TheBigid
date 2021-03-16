'use strict';


angular.module('tbidApp')
  .controller('LogoutCtrl', function ($scope, loginService, $location) {


    if(!$scope.rootData.isLoggedIn){
      $location.path("/login");
    }

    $scope.data = {};
    $scope.hideLoader();
  });
