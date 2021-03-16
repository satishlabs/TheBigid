angular.module('tbidApp')
    .directive('notificationItem', function() {
        var controller = ['$scope','$rootScope','$location','notificationService', '$route','$filter','myProfileService', function($scope,$rootScope,$location,notificationService, $route,$filter,myProfileService) {
        	
       	 	$scope.openNotificationItem = function(urlToTraverse,notifypostid,displaySection){
	       		$rootScope.clearFlagsOnPageLoad();
	       		document.getElementById("myDropdown").classList.toggle("showglobal");
	    		notificationService.notifypostid = notifypostid;
	    		notificationService.displaySection = displaySection;
	    		$location.path(urlToTraverse);//traverse to the page - /notificationDetailDisplay
	    		$route.reload();
       	 	};
       	 	
       	 	$scope.updateReadStatus = function(notifyid){
       	 		notificationService.updateReadStatus(notifyid).then(function(response){
      	 			 
      	 			 for(var i=0;i< myProfileService.notificationDisplayObj.length;i++)
      	 			 {
      	 				if(myProfileService.notificationDisplayObj[i].id === notifyid){
      	 					    myProfileService.notificationDisplayObj[i].notificationStatus = 'Read';
      	 				 }
      	 			 }
      	 		},function(error){
       	 			console.error("There was some issue while updating the read status of the notification");
       	 			console.error(error);
       	 			myProfileService.notificationDisplayObj[i].notificationStatus = 'NEW';
       	 		});
       	 	};
       	 	
       	 	$scope.updateGroupReadStatus = function(notifyidgrp){
       	 		
       	 		for(var i=0;i<notifyidgrp.length;i++){
       	 			var notifyid = notifyidgrp[i];
           	 		notificationService.updateReadStatus(notifyid).then(function(response){

          	 			 for(var i=0;i< myProfileService.notificationDisplayObj.length;i++)
          	 			 {
          	 				if(myProfileService.notificationDisplayObj[i].id === notifyid){
          	 					myProfileService.notificationDisplayObj[i].notificationStatus = 'Read';
          	 				 }
          	 			 }

          	 		},function(error){
           	 			console.error("There was some issue while updating the read status of the GROUP notification");
           	 			console.error(error);
           	 			myProfileService.notificationDisplayObj[i].notificationStatus = 'NEW';
           	 		});
       	 		}

       	 	};
       	 	
	        $scope.formatDateToDisplay = function(fetchedDate){
	        	//Wed May 23 2017 12:26:00 GMT+0530 (India Standard Time)
	            var fullDate = new Date(fetchedDate);//Get Post Date and Time
	        	var date = fullDate.getDate();
	        	var month = fullDate.getMonth() + 1;
	        	var year = fullDate.getFullYear();
	        	var hours = fullDate.getHours();
	            var minutes = fullDate.getMinutes();
	            
	            var diff = Math.abs(new Date() - new Date(fetchedDate));
	            var minutes = Math.floor((diff/1000)/60);// number of minutes
	            var seconds = Math.floor((diff/1000));//number of seconds 
	            
	            if(minutes > 1440){
	            	//display the date
	            	//scope.displayTimeInfo = date+"/"+month+"/"+year;
	            	$scope.displayTimeInfo = $filter('date')(fetchedDate, 'MMMM d, y');
	            }
	            else{
	            	var minChar = '';
                    var hourChar = '';
                    
	            	if(minutes > 60){
	            		var minsDisplay = minutes % 60;
	            		var hoursDisplay = (minutes - minsDisplay) / 60;
	            		if((hoursDisplay) <= 1){ hourChar  = " Hour "; } else { hourChar  = " Hours ";}
	            		if((minsDisplay) <= 1){ minChar = " Minute Ago"; } else { minChar = " Minutes Ago";}
	            		$scope.displayTimeInfo = hoursDisplay + hourChar + minsDisplay+ minChar;
	            	}
	            	else{	
	            		if((minutes) <= 1){ minChar = " Minute Ago"; } else { minChar = " Minutes Ago";}
	            		if(minutes === 0){	
	            			$scope.displayTimeInfo = "Just now";
	            		}
	            		else{
	            			$scope.displayTimeInfo = minutes + minChar;
	            		}
	            	}            	
	            }
	        };
	        
       	 	$scope.usernameClicked = function(event,userId){
       	 		event.stopPropagation();//Don't call the parent click events, since we are already on an hyperlink. It needs to be restricted.
       	 		notificationService.userIdClicked = userId;
       	 		$rootScope.showSearchBoxFlag=false;
       	 		$location.path("/userPosts");
       	 		$route.reload();
       	 	};
       	 	

        	$scope.initToolTipContent = function(){
        		$scope.notifybypplArray = $scope.notifybyppl;
        		$scope.notifypplnames = $scope.notifybypplArray.join("\n");
        	};
    	 
     }];

    return {
             restrict: 'E',
             scope: {
            	 notifbyuser : "=",
            	 notifbyuserid : "=",
            	 notifyaction : "=",
            	 notifyreadstatus : "=",
            	 notifyposttitle : "=",
            	 notifypostid : "=",
            	 notifyid : "=",
            	 notifytime : "=",
            	 notifyaccessurl : "=",
            	 notifybyppl : "=",
                 notifyidgrp : "="
             },
             controller : controller,
             templateUrl: 'views/templates/notificationItem.html',
             replace: true
         };

    });
