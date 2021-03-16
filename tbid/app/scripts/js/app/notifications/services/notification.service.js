angular.module('tbidApp')
  .service('notificationService',['$q','ajaxService','notificationsUrlService', function ($q,ajaxService,notificationsUrlService) {
   	  
	  this.notifyaccessurl = '';
	  this.notifypostid = '';
	  this.displaySection='';
	  this.userIdClicked = '';
	  
	  this.fetchNotifications = function(userId){
            var deferred = $q.defer();
            var url = notificationsUrlService.fetchNotificationsUrl(userId);
           
            ajaxService.doGet(url,{},'').then(
              function (response) {
                deferred.resolve(response);
              },
              function (rejectError) {
                deferred.reject(rejectError)
              }
            );
            return deferred.promise;
        };
        
        this.fetchNotificationDetailUrl = function(userId,postId){
            var deferred = $q.defer();
            var url = notificationsUrlService.fetchNotificationDetailUrl(userId,postId);
           
            ajaxService.doGet(url,{},'').then(
              function (response) {
                deferred.resolve(response);
              },
              function (rejectError) {
                deferred.reject(rejectError)
              }
            );
            return deferred.promise;
        };
        
        this.fetchUserPosts = function(userId){
            var deferred = $q.defer();
            var url = notificationsUrlService.fetchUserPosts(userId);
           
            ajaxService.doGet(url,{},'').then(
              function (response) {
                deferred.resolve(response);
              },
              function (rejectError) {
                deferred.reject(rejectError)
              }
            );
            return deferred.promise;
        };
        
        this.updateReadStatus = function(notifId){
            var deferred = $q.defer();
            var url = notificationsUrlService.updateReadStatusUrl(notifId);
           
            ajaxService.doPut(url,{},'').then(
              function (response) {
                deferred.resolve(response);
              },
              function (rejectError) {
                deferred.reject(rejectError)
              }
            );
            return deferred.promise;
        };
        
        
        this.updateNotificationCountRead = function(userId){
            var deferred = $q.defer();
            var url = notificationsUrlService.updateNotificationCountRead(userId);
           
            ajaxService.doPut(url,{},'').then(
              function (response) {
                deferred.resolve(response);
              },
              function (rejectError) {
                deferred.reject(rejectError)
              }
            );
            return deferred.promise;
        };
        
        this.transformNotifications = function(response){
    		
            var totalNotificationsCount = 0;
            var totalUnreadNotificationsCount = 0;
                       
    		var postNotificationsArray = [];
    		if(response.postNotifications !== null && response.postNotifications.length >0){
    		postNotificationsArray = response.postNotifications;
	    		for(var i=0;i<postNotificationsArray.length;i++){
	    			postNotificationsArray[i].notifyaction = "NEWPOST";
	    		 }    	
    		}	
    		
    		var nsfwNotificationsArray = [];
    		if(response.nsfwNotifications !== null && response.nsfwNotifications.length >0){
    			nsfwNotificationsArray = response.nsfwNotifications;
    			for(var i=0;i<nsfwNotificationsArray.length;i++){
    				nsfwNotificationsArray[i].notifyaction = "NSFW";
    			}
    		}
    		
    		var commentNotificationsArray = [];
    		if(response.commentNotifications !== null && response.commentNotifications.length >0){
        		commentNotificationsArray = response.commentNotifications;
        		for(var i=0;i<commentNotificationsArray.length;i++){
        			commentNotificationsArray[i].notifyaction = "NEWCOMMENT";
        		}
    		}

    		var replyNotificationArray = [];
    		if(response.replyNotification !== null && response.replyNotification.length >0){
    			replyNotificationArray = response.replyNotification;
        		for(var i=0;i<replyNotificationArray.length;i++){
        			replyNotificationArray[i].notifyaction = "REPLY";
        		}
    		}    		
    		
    		var followingNotificationsArray = [];
    		if(response.followingNotifications !== null && response.followingNotifications.length >0){
    			followingNotificationsArray = response.followingNotifications;
    			for(var i=0;i<followingNotificationsArray.length;i++){
    				followingNotificationsArray[i].notifyaction = "FOLLOWING";
    			}
    		}
    		
         /* var sharedPostNotificationsArray = [];
    		if(response.sharedPostNotifications !== null && response.sharedPostNotifications.length >0){
    			sharedPostNotificationsArray = response.sharedPostNotifications;
    			for(var i=0;i<sharedPostNotificationsArray.length;i++){
    				sharedPostNotificationsArray[i].notifyaction = "SHARED";
    			}
    		}*/
    		
    		/*SAVED CONSOLIDATION RELATED CODE - END*/
    		var sharedPostNotificationsArray = [];
    		var sharedNotifArray = [];
    		if(response.sharedPostNotifications !== null && response.sharedPostNotifications.length >0){
    			sharedPostNotificationsArray = response.sharedPostNotifications;
    			
        		var unreadSharedNotificationArray = [];
    			for(var i=0;i<sharedPostNotificationsArray.length;i++){
    				if(sharedPostNotificationsArray[i].notificationStatus === 'NEW'){
    					unreadSharedNotificationArray.push(sharedPostNotificationsArray[i]);
    				}
    			}
    			
    			
    			var samepostflag = false;
    			for(var i=0;i<unreadSharedNotificationArray.length;i++){

    				if(sharedNotifArray.length>0)
    				{
        				for(var j=0;j<sharedNotifArray.length;j++){
        					samepostflag = false;
            				if(unreadSharedNotificationArray[i].postId === sharedNotifArray[j].postId){
            					sharedNotifArray[j].byPeople.push(unreadSharedNotificationArray[i].byUserName);
            					sharedNotifArray[j].idgrp.push(unreadSharedNotificationArray[i].id);
            					samepostflag = true;
            					break;
            				}
        				}   
        				if(!samepostflag){
                			var obj = {};
                			obj.postId = unreadSharedNotificationArray[i].postId;
                			obj.postName =unreadSharedNotificationArray[i].postName;
                			obj.byPeople = [];
                			obj.byPeople.push(unreadSharedNotificationArray[i].byUserName);
                			obj.idgrp = [];
                			obj.idgrp.push(unreadSharedNotificationArray[i].id);
                			obj.notifyaction = "SAVECONSOLIDATED";
                			obj.notificationStatus = unreadSharedNotificationArray[i].notificationStatus;
                			obj.notifiedTime=unreadSharedNotificationArray[i].notifiedTime;
                			sharedNotifArray.push(obj);
                			samepostflag= false;
        				}

    				}
    				else
    				{
            			var obj = {};
            			obj.postId = unreadSharedNotificationArray[i].postId;
            			obj.postName =unreadSharedNotificationArray[i].postName;
            			obj.byPeople = [];
            			obj.byPeople.push(unreadSharedNotificationArray[i].byUserName);
            			obj.idgrp = [];
            			obj.idgrp.push(unreadSharedNotificationArray[i].id);            			
            			obj.notifyaction = "SAVECONSOLIDATED";
            			obj.notificationStatus = unreadSharedNotificationArray[i].notificationStatus;
            			obj.notifiedTime=unreadSharedNotificationArray[i].notifiedTime;
            			sharedNotifArray.push(obj);
    				}
    			}
    		}
            //This piece of code below removes the redundant people if any from the array.
    		if(sharedNotifArray.length >0 && sharedNotifArray[0].byPeople.length > 1){
        		var listOfPplShared = [];
        		var filteredPplList =[];
        		listOfPplShared = sharedNotifArray[0].byPeople;
        		for(var i=0;i<listOfPplShared.length;i++){
        			if(filteredPplList.indexOf(listOfPplShared[i]) === -1){
        				filteredPplList.push(listOfPplShared[i]);
        			}
        		}   		
        		sharedNotifArray[0].byPeople = filteredPplList;
    		}
    		/*SAVED CONSOLIDATION RELATED CODE - END*/

    		
    		
    		var likeNotificationArray = [];
    		if(response.likeNotification !== null && response.likeNotification.length >0){
    			likeNotificationArray = response.likeNotification;
    			for(var i=0;i<likeNotificationArray.length;i++){
    				likeNotificationArray[i].notifyaction = "LIKE";
    			}
    		}
    		
    		var dislikeNotificationArray = [];
    		if(response.dislikeNotification !== null && response.dislikeNotification.length >0){
    			dislikeNotificationArray = response.dislikeNotification;
    			for(var i=0;i<dislikeNotificationArray.length;i++){
    				dislikeNotificationArray[i].notifyaction = "DISLIKE";
    			}
    		}    		
    		
    		/*VOTE COUNT CONSOLIDATION RELATED CODE - START*/
    		var voteNotificationArray = [];
    		var voteNotifArray = [];
    		if((response.likeNotification !== null && response.likeNotification.length >0) ||
    		   (response.dislikeNotification !== null && response.dislikeNotification.length >0)
    			){
    			
    			var likeNotificationArray = response.likeNotification;   			
    			var dislikeNotificationArray = response.dislikeNotification;    			    			
    			voteNotificationArray = likeNotificationArray.concat(dislikeNotificationArray);

    			var unreadVoteNotificationArray = [];
    			for(var i=0;i<voteNotificationArray.length;i++){
    				if(voteNotificationArray[i].notificationStatus === 'NEW'){
    					unreadVoteNotificationArray.push(voteNotificationArray[i]);
    				}
    			}

    			
    			var samepostflag = false;
    			for(var i=0;i<unreadVoteNotificationArray.length;i++)
    			{
    				samepostflag = false;
    				if(voteNotifArray.length>0)
    				{
        				for(var j=0;j<voteNotifArray.length;j++){
            				if(unreadVoteNotificationArray[i].postId === voteNotifArray[j].postId){
            					voteNotifArray[j].byPeople.push(unreadVoteNotificationArray[i].byUserName);
            					voteNotifArray[j].idgrp.push(unreadVoteNotificationArray[i].id);
            					samepostflag = true;
            					break;
            				}
        				}   
        				if(!samepostflag){
                			var obj = {};
                			obj.postId = unreadVoteNotificationArray[i].postId;
                			obj.postName =unreadVoteNotificationArray[i].postName;
                			obj.byPeople = [];
                			obj.byPeople.push(unreadVoteNotificationArray[i].byUserName);
                			obj.idgrp = [];
                			obj.idgrp.push(unreadVoteNotificationArray[i].id);                  			
                			obj.notifyaction = "VOTED";
                			obj.notificationStatus = unreadVoteNotificationArray[i].notificationStatus;
                			obj.notifiedTime=unreadVoteNotificationArray[i].notifiedTime;
                			voteNotifArray.push(obj);
                			samepostflag= false;
        				}

    				}
    				else
    				{
            			var obj = {};
            			obj.postId = unreadVoteNotificationArray[i].postId;
            			obj.postName =unreadVoteNotificationArray[i].postName;
            			obj.byPeople = [];
            			obj.byPeople.push(unreadVoteNotificationArray[i].byUserName);
            			obj.idgrp = [];
            			obj.idgrp.push(unreadVoteNotificationArray[i].id);            			
            			obj.notifyaction = "VOTED";
            			obj.notificationStatus = unreadVoteNotificationArray[i].notificationStatus;
            			obj.notifiedTime=unreadVoteNotificationArray[i].notifiedTime;
            			voteNotifArray.push(obj);
    				}
    			}    			
    		}
    		
            //This piece of code below removes the redundant people if any from the array.
    		if(voteNotifArray.length >0 && voteNotifArray[0].byPeople.length > 1){
        		var listOfPplVoted = [];
        		var filteredPplList =[];
        		listOfPplVoted = voteNotifArray[0].byPeople;
        		for(var i=0;i<listOfPplVoted.length;i++){
        			if(filteredPplList.indexOf(listOfPplVoted[i]) === -1){
        				filteredPplList.push(listOfPplVoted[i]);
        			}
        		}   		
        		voteNotifArray[0].byPeople = filteredPplList;
    		}

    		/*VOTE COUNT CONSOLIDATION RELATED CODE - END*/

    		
    		var pushNotificationArray = [];
    		if(response.pushNotification !== null && response.pushNotification.length >0){
    			pushNotificationArray = response.pushNotification;
    			for(var i=0;i<pushNotificationArray.length;i++){
    				pushNotificationArray[i].notifyaction = "PUSH";
    			}
    		}
    		
    		var notificationDisplayObj = [];
    		if(postNotificationsArray.length>0){ notificationDisplayObj = notificationDisplayObj.concat(postNotificationsArray);}
    		if(nsfwNotificationsArray.length>0){ notificationDisplayObj = notificationDisplayObj.concat(nsfwNotificationsArray);}
    		if(commentNotificationsArray.length>0){ notificationDisplayObj =  notificationDisplayObj.concat(commentNotificationsArray);}
    		if(replyNotificationArray.length>0){notificationDisplayObj =  notificationDisplayObj.concat(replyNotificationArray);}
    		if(followingNotificationsArray.length>0){notificationDisplayObj =  notificationDisplayObj.concat(followingNotificationsArray);}
    		if(voteNotifArray.length>0){notificationDisplayObj = notificationDisplayObj.concat(voteNotifArray);}
    		if(sharedNotifArray.length>0){notificationDisplayObj = notificationDisplayObj.concat(sharedNotifArray);}
    		if(pushNotificationArray.length>0){notificationDisplayObj =  notificationDisplayObj.concat(pushNotificationArray);}
    		
    		return notificationDisplayObj;
        }
  }]);