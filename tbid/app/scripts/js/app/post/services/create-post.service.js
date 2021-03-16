angular.module('tbidApp')
  .service('createPostService', function ($q,$http) {


	  this.submitPost = function(postDetails){
	      var deferred = $q.defer();


	     $http({
	        method: 'POST',
	        url: '/post/publish',
	        contentType : "application/json",
            data : postDetails
	        })
	        .then(
	        function(result){
	          deferred.resolve(result.data);
	        },
	        function(error){
	          deferred.reject(error);
	        }
	      );

	      return deferred.promise;
	    };



		  this.reportPost = function(reportedInfo){
		      var deferred = $q.defer();

		      var urlToString = "/post/"+reportedInfo.postId+"/isnsfw";
		      $http({
		        method: 'POST',
		        url: urlToString,
	            data : {
	            		"reportedNSFW":true,
	            		"reasonForNsfw":reportedInfo.reasonForNsfw,
	            		"userId":reportedInfo.userId
	                	}
		        })
		        .then(
		        function(result){
		          deferred.resolve(result.data);
		        },
		        function(error){
		          deferred.reject(error);
		        }
		      );

		      return deferred.promise;
		    };

  });
