function notificationsUrlService(urlService) {
	return{
	      fetchNotificationsUrl : function(userId){
	          return urlService.getSecurePath() + "/notification/subscribe/"+userId;
	        },
	        fetchNotificationDetailUrl : function(userId,postId){	
	        	return urlService.getSecurePath() + "/post/"+userId+"/"+postId;
	        },
	        updateReadStatusUrl: function(notifId){	
	        	return urlService.getSecurePath() + "/notification/readnotification/"+notifId;
	        },
	        fetchUserPosts: function(userId){	
	        	return urlService.getSecurePath() + "/post/"+userId+"/allpost";
	        },
	        updateNotificationCountRead: function(userId){	
	        	return urlService.getSecurePath() + "/user/viewNotification/"+userId;
	        }
	}
};

angular
.module('tbidApp')
.service('notificationsUrlService', notificationsUrlService);
