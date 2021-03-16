'use strict';

angular.module('tbidApp')
       .controller('feedController', ['$scope','$http','$rootScope','mapService','$timeout','myProfileService',function ($scope,$http,$rootScope,mapService,$timeout,myProfileService){
    	   
    	   var map;
    	      	   
    	   var currentfeedsUrlString = '';
    	   if($rootScope.isStubbedMode){
    		   currentfeedsUrlString = "./temp/json/currentfeeds.json";
    	   }
    	   else{
    		   currentfeedsUrlString = "/post/currentfeeds";//to be configured asking server side resource
    	   }
	    
    	   mapService.fetchFeedMapValues(currentfeedsUrlString).then(function(response){
		    		   
		    		   // var coords = response.data.feedlocations[0].entries.coordinates;
				       var mapOptions = {
			        		  center: new google.maps.LatLng(29.232078399999978, 28.7666216),
			                  zoom: 0,
			                //minZoom: 0,
			                  disableDefaultUI: true, // a way to quickly hide all controls
			                  mapTypeControl: false,
			                  mapTypeId: 'satellite',
			                  draggable: false, 
			                  zoomControl: false, 
			                  scrollwheel: false, 
			                  disableDoubleClickZoom: true,
			                  maxZoom: 12
			                //minZoom:5
		               };

		    		   var map = new google.maps.Map(document.getElementById("map"), mapOptions);
		    		   map.setCenter(new google.maps.LatLng(29.232078399999978, 28.7666216));

		    		   google.maps.event.addDomListener(window, 'resize', function() {
		    			   map.setCenter(new google.maps.LatLng(29.232078399999978, 28.7666216));
		    		   });

		    		   //Fix for centering the position of the map in the map layout - START
		    		   $timeout(function(){
		    			   google.maps.event.trigger(map, "resize");
		    			   map.setCenter(new google.maps.LatLng(29.232078399999978, 28.7666216));
		    		   },1000);
		    		  //Fix for centering the position of the map in the map layout - END
		    		   
		    		  /*//Different try to make the map center :
		    		    google.maps.event.addDomListener(window, 'load', function() {
		      	    	map.setCenter(new google.maps.LatLng(29.232078399999978, 28.7666216));
		      	   		});
		      	   		
		      	   		setTimeout(function() {
		    			   // google.maps.event.trigger(map, "resize");
		    			    map.setCenter(new google.maps.LatLng(29.232078399999978, 28.7666216));
		    			}, 1000);
		    			
		    			$(document).mouseover(function() {		    			   
		    			    map.setCenter(new google.maps.LatLng(29.232078399999978, 28.7666216));
		    			});
		      	   		*/
		    		   
		    		   if(response.data.feedlocations !== undefined){
		            	
		    			   for (var i = 0; i < response.data.feedlocations.length; i++) {
		     	        	
		    				   var coords = response.data.feedlocations[i].coordinates;
		    				   var latLng = new google.maps.LatLng(coords[1],coords[0]);
		     	            
		    				   	/*var icon = {
		     	            	    url: "./temp/marker.png", // url
		     	            	    scaledSize: new google.maps.Size(20, 20), // scaled size
		     	            	    origin: new google.maps.Point(0,0), // origin
		     	            	    anchor: new google.maps.Point(0, 0) // anchor
		     	            	};*/

		    				   var marker = new google.maps.Marker({
		    					   position: latLng,
		    					   map: map,
                     clickable: false,
		    					   animation: google.maps.Animation.DROP
		    					   // animation:google.maps.Animation.BOUNCE
		    					   // icon : icon
		    				   });
		    				   marker.setMap(map);
		     	          }
		            }    		   
	    	   },function(error){
			    	console.error("XXXX : inside feedController.js -- fetchFeedMapValues Error Response");
			    	console.error(error);
	    	   });
    	   

    	      google.charts.load('current', {
    	    	  callback: drawRegionsMap,
    	    	  packages: ['bar', 'corechart', 'table','geochart']
    	    	});
    	   
    	      
    	      function drawRegionsMap(){

    	    	  if(myProfileService.isLoggedIn === true){
    			    var heatmapfeedsUrlString = '';
    			    
    			    if($rootScope.isStubbedMode){
    			    	heatmapfeedsUrlString = "./temp/json/heatmapfeeds.json";
    			    }else{
    			    	heatmapfeedsUrlString = "/post/heatmapfeeds";//to be configured asking server side resource
    			    }
    		  
    			    mapService.fetchHeatMapValues(heatmapfeedsUrlString).then(function(response){

			    		          var data=[];
			    		          var header= ['Country', 'Posts'];
			    		          data.push(header);
			    		          for (var i = 0; i < response.data.length; i++) {
			    		               var temp=[];
			    		               temp.push(response.data[i].country);
			    		               temp.push(response.data[i].popularity);
			    		
			    		               data.push(temp);
			    		           }

			    		        	var chartdata = google.visualization.arrayToDataTable(data);
			    		            //$rootScope.chartdataArray =   chartdata; 
			    			        var options = {
			    			        		colors: ['#B27676','#800000'] //eg. maroon red
			    			        };
			    			       
			    			        options['backgroundColor'] = '#FFFFFF';
			    			        options['datalessRegionColor'] = '#D3D3D3'; //light grey
			    			        options['width'] = 240;
			    			        options['height'] = 200;
			    			        if(document.getElementById("regions_div") !== null){
			    			        	var chart = new google.visualization.GeoChart(document.getElementById('regions_div'));
			    			        	chart.draw(chartdata, options);
			    			        }
			          			    	
		    			    },function(error){
		    			    	console.error("XXXX : inside feedController.js -- fetchHeatMapValues Error Response");
		    			    	console.error(error);
		    			    });

    	      } };
    	      

	  
  }]);