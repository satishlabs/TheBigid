'use strict';

/**
 * @ngdoc overview
 * @name tbidApp
 * @description
 * # tbidApp
 *
 * Main module of the application.
 */
var app = angular
  .module('tbidApp', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch',
    'ui.bootstrap',
    'infinite-scroll',
    'ngMaterial',
    'appFilters',
    'ab-base64'
  ]);


 app.config(function ($routeProvider, $locationProvider) {
    $locationProvider.hashPrefix('');
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main'
      })
      .when('/register', {
        templateUrl: 'views/register.html',
        controller: 'RegisterCtrl',
        controllerAs: 'register'
      })
      .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl',
        controllerAs: 'loginService'
      })
/*      .when('/logout', {
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl',
        controllerAs: 'login'
      })   */
      .when('/notificationDetailDisplay',{
          templateUrl: 'views/templates/notificationdetail.html',
          controller: 'notificationdetailController',
          controllerAs: 'notificationdetailctrl'
      })
      .when('/notificationListDisplay',{
          templateUrl: 'views/templates/notificationlist.html',
          controller: 'notificationListController',
          controllerAs: 'notificationlistctrl'
      })
      .when('/changePassword',{
          templateUrl: 'views/templates/changePassword.html',
          controller: 'changePasswordController',
          controllerAs: 'changePasswordCtrl'
      })
      .when('/changeAvatar',{
          templateUrl: 'views/templates/changeAvatar.html',
          controller: 'changeAvatarController',
          controllerAs: 'changeAvatarCtrl'
      })      
      .when('/profileInfo',{
          templateUrl: 'views/templates/userProfilePage.html',
          controller: 'userProfilePageController',
          controllerAs: 'userProfilePageCtrl'
      })
      .when('/userPosts',{
          templateUrl: 'views/templates/userPosts.html',
          controller: 'userPostsController',
          controllerAs: 'userPostsCtrl'    	  
      })
      .otherwise({
        redirectTo: '/'
      });
  });


