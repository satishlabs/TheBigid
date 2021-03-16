'use strict';

angular.module('tbidApp').factory('postService', PostService);

PostService.$inject = [ '$q', 'ajaxService', '$http', '$rootScope',
		'postUrlService' ];

function PostService($q, ajaxService, $http, $rootScope, postUrlService) {

	return {
		getPost : getPost,
		getAllPost : getAllPost,
		getPostsByScrollUrl : getPostsByScrollUrl,
		doPushUpVote : doPushUpVote,
		updatePostWithNewComment : updatePostWithNewComment,
		doUpVoteUpdate : doUpVoteUpdate,
		doDownVoteUpdate : doDownVoteUpdate,
		doCommentUpVoteUpdate : doCommentUpVoteUpdate,
		doCommentDownVoteUpdate : doCommentDownVoteUpdate,
		followUserUpdateToServer : followUserUpdateToServer,
		updateNotificationForUserPost : updateNotificationForUserPost,
		postReplyForAComment : postReplyForAComment,
		updateTagsAddedToServer : updateTagsAddedToServer,
		updateReplyCommentLikeToServer : updateReplyCommentLikeToServer,
		fetchFreshComments : fetchFreshComments,
		getSavedPosts : getSavedPosts,
		savePost : savePost,
		getMyPosts:getMyPosts
	};

	function getPost() {

	}

	function getAllPost(userId) {
		var deferred = $q.defer();
		var url = postUrlService.getAllPostUrl(userId);
		ajaxService.doGet(url, {}).then(function(response) {
			deferred.resolve(response);
		}, function(errorResult) {
			deferred.reject(errorResult);
		});
		return deferred.promise;
	}

	function getPostsByScrollUrl(userId, pageNo, noOfPosts) {
		var deferred = $q.defer();
		var url = postUrlService.getPostsByScrollUrl(userId, pageNo, noOfPosts);
		ajaxService.doGet(url, {}).then(function(response) {
			deferred.resolve(response);
		}, function(errorResult) {
			deferred.reject(errorResult);
		});
		return deferred.promise;
	}

	function doPushUpVote(paramaters) {
		var deferred = $q.defer();
		var data = {
			"pushActivate" : paramaters.pushActivate,
			"userId" : paramaters.userId
		};
		var url = "/post/" + paramaters.postid + "/pushedvote";
		ajaxService.doPost(url, {}, data).then(function(resolution) {
			deferred.resolve(resolution);
		}, function(error) {
			deferred.reject(error);
		});
		return deferred.promise;
	}

	function updatePostWithNewComment(commentData) {
		var deferred = $q.defer();
		$http({
			method : 'POST',
			url : "/post/" + commentData.postid + "/comments",
			data : commentData
		}).then(function(resolution) {
			deferred.resolve(resolution.data);
		}, function(error) {
			deferred.reject(error);
		});
		return deferred.promise;
	}

	function doUpVoteUpdate(data) {
		var deferred = $q.defer();
		var urlString = "/post/" + data.postid + "/upvote";
		$http({
			method : 'POST',
			contentType : "application/json",
			url : urlString,
			data : {
				"upVote" : data.upVote,
				"userId" : data.userId
			}
		}).then(function(resolution) {
			deferred.resolve(resolution.data);
		}, function(error) {
			deferred.reject(error);
		});
		return deferred.promise;
	}

	function doDownVoteUpdate(data) {
		var deferred = $q.defer();
		var urlString = "/post/" + data.postid + "/downvote";
		$http({
			method : 'POST',
			contentType : "application/json",
			url : urlString,
			data : {
				"downVote" : data.downVote,
				"userId" : data.userId
			}
		}).then(function(resolution) {
			deferred.resolve(resolution.data);
		}, function(error) {
			deferred.reject(error);
		});
		return deferred.promise;
	}

	function doCommentUpVoteUpdate(data) {
		var deferred = $q.defer();
		var urlToServer = "/post/" + data.commentId + "/commentupvote";
		$http({
			method : 'POST',
			url : urlToServer,
			data : {
				"upVote" : true,
				"userId" : data.userId
			}
		}).then(function(resolution) {
			deferred.resolve(resolution.data);
		}, function(error) {
			deferred.reject(error);
		});
		return deferred.promise;
	}

	function doCommentDownVoteUpdate(data) {
		var deferred = $q.defer();
		var urlToServer = "/post/" + data.commentId + "/commentdownvote";

		$http({
			method : 'POST',
			url : urlToServer,
			data : {
				"downVote" : true,
				"userId" : data.userId
			}
		}).then(function(resolution) {
			deferred.resolve(resolution.data);
		}, function(error) {
			deferred.reject(error);
		});
		return deferred.promise;
	}

	function followUserUpdateToServer(data) {
		var deferred = $q.defer();
		var url = postUrlService.getFollowUserUrl(data);
		var dataToServer = '';// need to be checked with Satish
		ajaxService.doPost(url, {}, dataToServer).then(function(response) {
			deferred.resolve(response);
		}, function(rejectError) {
			deferred.reject(rejectError)
		});
		return deferred.promise;
	}

	function updateNotificationForUserPost(data) {
		var deferred = $q.defer();
		var url = postUrlService.getPostNotificationUrl(data);
		var dataToServer = '';// need to be checked with Satish

		ajaxService.doPut(url, {}, dataToServer).then(function(response) {
			deferred.resolve(response);
		}, function(rejectError) {
			deferred.reject(rejectError)
		});
		return deferred.promise;
	}

	function postReplyForAComment(commentData) {
		var deferred = $q.defer();
		$http({
			method : 'POST',
			url : "/post/" + commentData.postId + "/replies",
			data : commentData
		}).then(function(resolution) {
			deferred.resolve("success");
		}, function(error) {
			deferred.reject(error);
		});
		return deferred.promise;
	}

	function updateTagsAddedToServer(dataToServer) {
		
		var deferred = $q.defer();
		var url = postUrlService.updatePostTags(dataToServer.postId);
		var tagsToServer = dataToServer.tags.toString();
		
		ajaxService.doPut(url, {},tagsToServer).then(function(response) {
			deferred.resolve(response);
		}, function(errorResult) {
			deferred.reject(errorResult);
		});
		return deferred.promise;
		
	}

	function updateReplyCommentLikeToServer(data) {
		var deferred = $q.defer();
		var urlToServer = "/post/" + data.replyId + "/replycommentupvote";
		$http({
			method : 'POST',
			url : urlToServer,
			data : {
				"upVote" : true,
				"userId" : data.userId,
				"commentId" : data.commentId
			}
		}).then(function(resolution) {
			deferred.resolve(resolution.data);
		}, function(error) {
			deferred.reject(error);
		});
		return deferred.promise;
	}

	function fetchFreshComments(postId) {
		var deferred = $q.defer();
		$http({
			method : 'GET',
			url : "/post/" + postId + "/commentsreply",
			contentType : "application/json"
		}).then(function(result) {
			deferred.resolve(result.data);
		}, function(error) {
			deferred.reject(error);
		});
		return deferred.promise;
	}

	function getSavedPosts(userId) {
		var deferred = $q.defer();
		var url = postUrlService.getSavedPostsUrl(userId);

		ajaxService.doGet(url, {}).then(function(response) {
			deferred.resolve(response);
		}, function(errorResult) {
			deferred.reject(errorResult);
		});
		return deferred.promise;
	}

	function savePost(dataToServer) {
		var deferred = $q.defer();
		var url = postUrlService.savePostUrl(dataToServer.userId,
				dataToServer.postId);
		ajaxService.doPost(url, {}).then(function(response) {
			deferred.resolve(response);
		}, function(errorResult) {
			deferred.reject(errorResult);
		});
		return deferred.promise;
	}
	
	function getMyPosts(userId) {
		var deferred = $q.defer();
		var url = postUrlService.myPostsUrl(userId);
		ajaxService.doGet(url, {}).then(function(response) {
			deferred.resolve(response);
		}, function(errorResult) {
			deferred.reject(errorResult);
		});
		return deferred.promise;
	}	
}
