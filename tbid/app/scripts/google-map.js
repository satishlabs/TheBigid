var latitudeGlobalVariable = '', longitudeGlobalVariable = '', locationGlobalVariable='',locationCountryGlobalVariable='';
			
			function initialize() {
			//google.maps.event.addDomListener(window, 'load', function(){
     			 var address = (document.getElementById('draft-location'));
      			 var autocomplete = new google.maps.places.Autocomplete(address);
      			 autocomplete.setTypes(['geocode']);
     			 google.maps.event.addListener(autocomplete, 'place_changed', function() {
         		 	var place = autocomplete.getPlace();
         			 if (!place.geometry) {
             		 	return;
         			 }

      				 var address = '';
      			 	 if(place.address_components) {
          				address = [
             						 (place.address_components[0] && place.address_components[0].short_name || ''),
              						 (place.address_components[1] && place.address_components[1].short_name || ''),
                                     (place.address_components[2] && place.address_components[2].short_name || '')
                                  ].join(' ');
         
          			 locationGlobalVariable = address;
          			
          			

		      		//This check is done. As the last component in Indian addresses, would be having pincode in them. Hence considering the last string value as country
		      		if(isNaN(place.address_components[place.address_components.length-1].long_name)){
		      			locationCountryGlobalVariable = place.address_components[place.address_components.length-1].long_name;
		      		}
		      		else if(isNaN(place.address_components[place.address_components.length-2].long_name)){
		      			locationCountryGlobalVariable = place.address_components[place.address_components.length-2].long_name;
		      		}	 
		      			 
          			

      			     }
     
      				//var address contain your autocomplete address 
      				// place.geometry.location.lat() && place.geometry.location.lat()
      				// will be used for current address latitude and longitude

     	    		document.getElementById('latitude').innerHTML = place.geometry.location.lat();
      				document.getElementById('longitude').innerHTML = place.geometry.location.lng();
     				latitudeGlobalVariable = place.geometry.location.lat();
      				longitudeGlobalVariable = place.geometry.location.lng();
      				fullPlace = place;
      			});
			}
			google.maps.event.addDomListener(window, 'load', initialize);
			
			
			
			/** 
			 * 
			 */
			
			
			   function initMap() {
			          var mapOptions = {
			         		 center: new google.maps.LatLng(29.232078399999978, 28.7666216),
			                 zoom: 0,
			             //    minZoom: 0,
			                 disableDefaultUI: true, // a way to quickly hide all controls
			                 mapTypeControl: false,
			                 mapTypeId: 'satellite',
			                 draggable: false, 
			                 zoomControl: false, 
			                 scrollwheel: true, 
			                 disableDoubleClickZoom: true,
			                 maxZoom: 12,
			                // minZoom:5                 
			          };
			          var map = new google.maps.Map(document.getElementById("map"), mapOptions);
			          map.setCenter(new google.maps.LatLng(29.232078399999978, 28.7666216));  
			      
			       }
			   
			   
			   
			   /*
			    * 
			    */
			   
			   google.charts.load('current', {
			    	  callback: drawRegionsMap,
			    	  packages: ['bar', 'corechart', 'table','geochart']
			    	}); 

			      google.charts.setOnLoadCallback(drawRegionsMap);

			      function drawRegionsMap() {

			 /*        var data = google.visualization.arrayToDataTable([
			          ['Country', 'Posts'],
			          ['Germany', 1],
			          ['United States', 2],
			          ['Brazil', 2],
			          ['Canada', 4],
			          ['France', 6],
					  
			          ['Russia',18],
			          ['India',20]
			        ]); */
			        

			        //MAKE AN HTTP CALL - START
					var xhrObj = new XMLHttpRequest();
				//	xhrObj.open('GET', "./temp/json/heatmapfeeds.json", true);
					xhrObj.open('GET', "/post/heatmapfeeds", true);
					xhrObj.send();
					xhrObj.addEventListener("readystatechange", processRequest, false);
					
					var responsedata ='';
					
					function processRequest(e) {
			    		if (xhrObj.readyState == 4 && xhrObj.status == 200) {
			        		responsedata = JSON.parse(xhrObj.responseText);
			       
			       			 //MAKE AN HTTP CALL - END
			        		var data=[];
			       			var header= ['Country', 'Posts'];
			        		data.push(header);
			        		for (var i = 0; i < responsedata.length; i++) {
			           			  var temp=[];
			             		  temp.push(responsedata[i].country);
			             		  temp.push(responsedata[i].popularity);

			                      data.push(temp);
			        		 }
			       			var chartdata = google.visualization.arrayToDataTable(data);

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

			    		}
					}

			      }