'use strict';

angular.module('tbidApp')
  .service('mapService', function ($http,$q) {
	   
	  this.fetchFeedMapValues = function(currentfeedsUrlString){
		  var deferred = $q.defer();

		   	  $http({
				   method : 'GET',
				   url : currentfeedsUrlString
			   }).then(function(response){
				            deferred.resolve(response);
			           },
			           function(error){
				   			console.error(error);		
				   			deferred.reject(error);
			   	       });
		   	  
		  return deferred.promise;	 
	 };
	 
	 
	  this.fetchHeatMapValues = function(heatmapfeedsUrlString){
		  var deferred = $q.defer();

		   	  $http({
				   method : 'GET',
				   url : heatmapfeedsUrlString
			   }).then(function(response){
				            deferred.resolve(response);
			           },
			           function(error){
				   			console.error(error);		
				   			deferred.reject(error);
			   	       });
		   	  
		  return deferred.promise;	 
	 };	 
	  
});
