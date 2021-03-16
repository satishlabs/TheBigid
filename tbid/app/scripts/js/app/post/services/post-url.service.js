angular
  .module('tbidApp')
  .factory('postUrlService', PostUrlService);


PostUrlService.$inject = ['urlService'];

function PostUrlService(urlService) {

  return {
    getAllPostUrl: function (userid) {
      return urlService.getSecurePath()+"/post/"+userid+"/list"
    },
    getPostsByScrollUrl: function (userid,pageNo,noOfPosts) {
        return urlService.getSecurePath()+"/post/show/latest?page="+pageNo+"&size="+noOfPosts;
      },
    getFollowUserUrl : function(data){
    	return urlService.getSecurePath()+"/post/"+data.userId+"/"+data.postId;
    },
    getPostNotificationUrl : function(data){
    	if(data.enableNotificationFromUser){
    		return urlService.getSecurePath()+"/post/enablenotification/"+data.postId;
    	}else{
    		return urlService.getSecurePath()+"/post/disablenotification/"+data.postId;
    	}
    },
    getSavedPostsUrl : function(userId){
    	return urlService.getSecurePath()+"/post/shared/"+userId;
    },
    savePostUrl : function(userId,postId){
    	return urlService.getSecurePath()+"/post/shared/"+userId+"/"+postId;
    },
    myPostsUrl : function(userId){
    	return urlService.getSecurePath() + "/post/"+userId+"/allpost";
    },
    updatePostTags : function(postId){
    	return urlService.getSecurePath() + "/post/"+postId +"/tags";
    }
  }
}
