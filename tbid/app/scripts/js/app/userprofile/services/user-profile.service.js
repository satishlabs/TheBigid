angular.module('tbidApp')
  .factory('myProfileService',['notificationService','$location',function (notificationService,$location) {
  
	  var myinfo = {};
	  
	  myinfo.userProfilePictureImage = '';
	  myinfo.isLoggedIn='';
	  myinfo.notificationCount=0;
	  myinfo.notificationCountReadStatus=false;
	  myinfo.notificationDisplayObj = [];
	  myinfo.loggedInId='';
	  
	  myinfo.showNotificationsGlobal = function(loggedInId){

         	notificationService.fetchNotifications(loggedInId).then(function(response){	

         				myinfo.notificationDisplayObj = notificationService.transformNotifications(response);
    	                $location.path("/");
    	                
         				},function(error){
         						console.error("There was some issue fetching the notifications");
         						myinfo.notificationDisplayObj = [];
         				});

    };
	  
	  return myinfo;
	  
  }]);