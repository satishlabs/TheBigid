'use strict';

/**
 * @ngdoc function
 * @name tbidApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the tbidApp
 */
angular
  .module('tbidApp')
  .controller('MainCtrl',MainController);



MainController.$inject =['$scope', '$location', 'ID_CATEGORIES', 'postService',
                         '$http','$timeout','notificationService','$interval',
                         '$window','myProfileService','$rootScope','$anchorScroll',
                         '$cookieStore', 'loginService','preLoginService'];


function MainController($scope, $location, ID_CATEGORIES, postService,
                        $http, $timeout, notificationService, $interval,
                        $window, myProfileService, $rootScope, $anchorScroll,
                        $cookieStore, loginService,preLoginService) {


    $rootScope.isStubbedMode = false;

    if($rootScope.isStubbedMode){
    	$scope.rootData.isLoggedIn = true;//added as said by Chandra Singh on call 12.4.2017
    }
    else{
        //temp deactivate the console logs
        console.log = function(){};
        console.error = function(){};
    }



    //These variables are to be configured for infinite scroll
    $rootScope.pageNumber = 0;
    $rootScope.size = 4;
    $scope.activateInfiniteScrollLoader = false;
    
    $rootScope.showSmallScrollLoader = false;
    $rootScope.isCreatingPostStatus = false;
    $rootScope.createPostGeneralPicture = ''; //reset the pic uploaded for creating a post during reload of the page

    $scope.$on("logout", function(){
      $scope.data  = {};
      $scope.showLoader();
      $location.path('/login');
      $scope.hideLoader();
    });

    
    $scope.isCreatingPost = false;
   	/*The below commented code doesn't work in IE; fix below it - START*/
	//$scope.selectedCategories = Array.from(ID_CATEGORIES);
    $scope.selectedCategories = [];
	var length = ID_CATEGORIES.length;
	for (var i = 0; i < length; i++) {
		$scope.selectedCategories.push(ID_CATEGORIES[i]);
	}
	/*The above commented code doesn't work in IE; fix below it - END*/

    $scope.typesAllOn = true;//by default all the types would be in ON state
    $scope.scrollTypeMainScreenOptions = ['GENERAL','EVENT','VISUAL','QUESTION','LINK','NSFW','TO/FROM'];

   
    $scope.displayCriterias = ["Recent","Vote","Discussed","Saved","Following","Rising","Quality","My Posts"];
  //  $scope.displayCriterias = ["Recent","Vote","Discussed","Following","Rising","Random","Saved"];
        $scope.selectedDisplayCriteria = "Recent"; //Default Selected from dropdown menu

        if($scope.rootData.isLoggedIn){

        		$scope.displayNotifications();

    		    postService.getAllPost($rootScope.loggedInId).then(function(response){
    			    	$scope.data = response;
    			    	myProfileService.data = $scope.data;
    			    	$scope.postsResponse = response;
    		    	}, function(error){
    		    	
    		    	});
    		   // postService.getAllPost(1).then(getAllPostSuccess, getAllPostError);

        };
	  
	  
	  $rootScope.callScrollFunction = function(){
		  $scope.activateInfiniteScrollLoader = true;
		  if($scope.rootData.isLoggedIn){
		  $rootScope.pageNumber++;
		  
		  postService.getPostsByScrollUrl($rootScope.loggedInId,$rootScope.pageNumber,$rootScope.size).then(getPostsByScrollSuccess, getPostsByScrollError);
		  
		  function getPostsByScrollSuccess(response){

			  //console.log("RESPONSE####################");
			  //console.log(response);

			    if(response.length >0 && $scope.data === undefined){
			    	
			    	$scope.data= {};
			    	$scope.data.posts = [];
			    	$scope.data.posts = response;
			    	
				    myProfileService.data = $scope.data.posts;
				    
			    	$scope.postsResponse= {};
			    	$scope.postsResponse.posts = [];
				    $scope.postsResponse.posts = $scope.data.posts;
			    }
			    else{
			    	
			    	$scope.data.posts = $scope.data.posts.concat(response);
				    myProfileService.data = $scope.data.posts;
				    $scope.postsResponse.posts = $scope.data.posts;
				    
				   /* $scope.data = response;
				    myProfileService.data = $scope.data;
				    $scope.postsResponse = response;*/
			    }			    
			    
			    $scope.activateInfiniteScrollLoader = false;
			  }
			
			  function getPostsByScrollError(error){
				  console.error(error);
				  $scope.activateInfiniteScrollLoader = false;
			  }
		  
		  }
	  };


    $scope.showrepliesfunc = function(replyObj){
    	$scope.showreplies = true;
    };



    $scope.toggleExpansion = function(postId) {

    	$rootScope.clearFlagsOnPageLoad();

	      if(!!$scope.isCreatingPost) {
	        $scope.togglePostCreation();
	      }
	      if($scope.expandedPost==postId) {
	        $scope.expandedPost = -1;
	      } else {
	        $scope.expandedPost = postId;
	      }
    };



    $scope.togglePostCreation = function() {

    	$scope.isCreatingPost = !$scope.isCreatingPost;
    	$rootScope.isCreatingPostStatus = !$scope.isCreatingPost;

    	$rootScope.clearFlagsOnPageLoad();
    	////console.log("##inside main.controller.js-- togglePostCreation -- Clearing Error message");

    	////console.log("after $scope.isCreatingPost:"+$scope.isCreatingPost);
    	//$window.initialize();
    	//call the global location initialize method; inorder to get to display the contents again for the second time.
    	//There was some issue in calling this function, hence have moved the complete set of code to this file itself.

    	// ## initialize method contents - START

    	$window.addEventListener('load',function(){

    		  var script = document.createElement('script');
    		  script.type = 'text/javascript';
    		  script.src = 'https://maps.googleapis.com/maps/api/js?key=AIzaSyB980f7lPB9Cek3g68AoFsjq6g5fHwsdsg&v=3.exp&libraries=places';
    		  document.body.appendChild(script);
    		});

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

      			 $window.locationGlobalVariable = address;

      			//This check is done. As the last component in Indian addresses, would be having pincode in them. Hence considering the last string value as country
      			if(isNaN(place.address_components[place.address_components.length-1].long_name)){
      				$window.locationCountryGlobalVariable = place.address_components[place.address_components.length-1].long_name;
      			}
      			else if(isNaN(place.address_components[place.address_components.length-2].long_name)){
      				$window.locationCountryGlobalVariable = place.address_components[place.address_components.length-2].long_name;
      			}

  			     }

 	    		document.getElementById('latitude').innerHTML = place.geometry.location.lat();
  				document.getElementById('longitude').innerHTML = place.geometry.location.lng();
  				$window.latitudeGlobalVariable = place.geometry.location.lat();
  				$window.longitudeGlobalVariable = place.geometry.location.lng();
  				$window.fullPlace = place;
  			});

 	    	// ## initialize method contents - END

    	if(!!$scope.isCreatingPost) {
    		$scope.expandedPost = 0;
    	}
    };




    $scope.isNotFilteredOut = function(category) {
      return $scope.selectedCategories.indexOf(category)>-1;
    };



    $scope.customSort = function(selectedDisplayCriteria) {

        return function(post) {

              if(selectedDisplayCriteria === "Recent"){
              	var date = new Date(post.postCreatedTime);
                return date;
              }
              else if(selectedDisplayCriteria === "NotificationRecent"){
                  var date = new Date(post.timeupdated);
                  return date;
               }
              else if(selectedDisplayCriteria === "Vote"){
            	  return post.voteDetails.voteCount;
              }
              else if(selectedDisplayCriteria === "Discussed"){

            	  ////console.log("For Post id :"+post.id);

            	  //Don't delete below as the logic works if they ask us based on total count of comments and replies.
            /*	  var countTotalReplies = '';
            	  for(var i=0;i<post.comments.length;i++){
            		  countTotalReplies = Number(countTotalReplies) + Number(post.comments[i].replies.length);
            	  }
            	  var totalCount = post.comments.length + countTotalReplies;
            	  return totalCount;//arrange according to the number of comments and replies for the post.*/

            	  //Logic for checking based on the total number of comments only.
            	  return post.comments.length;

              }
              /* These are implemented under the filter section ; cannot be taken under OrderBy section - START */
              else if(selectedDisplayCriteria === "Random"){
            	  return post.title.length; //temporary fix for random; as we face issues with random no. generation
              }
              else if(selectedDisplayCriteria === "Quality"){
            	  if(post.savedPostCount === null){post.savedPostCount=0;}
            	  return post.savedPostCount;//arrange based on the highest saved posts.
              }              
              else if(selectedDisplayCriteria === "Rising"){
            	  if(post.voteDetails.pushCount === null){post.voteDetails.pushCount=0;}
            	  return post.voteDetails.pushCount;//arrange based on the highest push counts.
              }
              else if(selectedDisplayCriteria === "Following"){

              }
              else if(selectedDisplayCriteria === "NearMe"){

              }
              /* These are implemented under the filter section ; cannot be taken under OrderBy section - END */
              else 
              {
                var date = new Date(post.postCreatedTime);
                return date; //By default show them according to Recent time
              }
        };
    };




    $scope.sortPost = function(post) {
        var date = new Date(post.postCreatedTime);
        return date;
    };


    $scope.selectedTypeList = [];
    $scope.checkBoxTickEvent = function(){
    	//////console.log("##inside checkBoxTickEvent - checked :"+$scope.checked);
    };


    $scope.updateTypeList = function(type,checked){
    	//no implementation yet
    };

    $scope.showPostsBasedOnDisplayCriteria = function(post){

       $rootScope.selectedDisplayCriteria = $scope.selectedDisplayCriteria;

       if($scope.selectedDisplayCriteria === 'Following'){
        	
        	if($rootScope.userData.followers.length > 0){
        		
            	for(var i=0;i<$rootScope.userData.followers.length;i++){
            		
                	if ($rootScope.userData.followers[i].following === post.createdBy) {
                		return true;
                	}
            	}
            	return false;
        	}

    	}
       else if($scope.selectedDisplayCriteria === 'My Posts'){

       }       
       else if($scope.selectedDisplayCriteria === 'Saved'){
    	   
       }
       else if($scope.selectedDisplayCriteria === 'NearMe'){

       }
       else if($scope.selectedDisplayCriteria === 'Rising'){
           // ////console.log("##inside Rising -- showPostsBasedOnDisplayCriteria - main.controller.js ");

            //Rising - sorted based on highest increase in votes and/or saves in the last 3 days
            //Here we display only the posts which are having upvotes in last 3 days only.

              /* var date1 = null;
               var date2 = new Date();*/

            /*   for(var i=0; i< $scope.postVoteDetailsData.postVoteDetails.length; i++){

            	   //////console.log("comparing id with id :"+$scope.postVoteDetailsData.postVoteDetails[i].id);
            	   if(post.id === $scope.postVoteDetailsData.postVoteDetails[i].id)
            	   {
               		   //////console.log("post.id entered :"+post.id);
            		   for(var j=0;j<$scope.postVoteDetailsData.postVoteDetails[i].upvote.length;j++)
            			   {
                		   			date1 = new Date($scope.postVoteDetailsData.postVoteDetails[i].upvote[j].upvotedTime);
                		   			var timeDiff = Math.abs(date2.getTime() - date1.getTime());
                		   			var differenceInDays = (Math.ceil(timeDiff / (1000 * 3600 * 24))-1);

                		   			if(differenceInDays >= 0 && differenceInDays <=3){
                		   				return true;
                		   			}
            			   }
            		   for(var j=0;j<$scope.postVoteDetailsData.postVoteDetails[i].push.length;j++)
        			   {
            		   			date1 = new Date($scope.postVoteDetailsData.postVoteDetails[i].push[j].pushActivatedTime);
            		   			var timeDiff = Math.abs(date2.getTime() - date1.getTime());
            		   			var differenceInDays = (Math.ceil(timeDiff / (1000 * 3600 * 24))-1);

            		   			if(differenceInDays >= 0 && differenceInDays <=3){
            		   				return true;
            		   			}
        			   }

            	   }
            	   else{
            		 //  return false;
            	   }
            	  // return false;

               }*/


               /*for(var i=0; i< $scope.postVoteDetailsData.postVoteDetails.length; i++){

            	   //////console.log("comparing id with id :"+$scope.postVoteDetailsData.postVoteDetails[i].id);
            	   if(post.id === $scope.postVoteDetailsData.postVoteDetails[i].id)
            	   {
               		   //////console.log("post.id entered :"+post.id);
            		   for(var j=0;j<$scope.postVoteDetailsData.postVoteDetails[i].push.length;j++)
        			   {
            		   			date1 = new Date($scope.postVoteDetailsData.postVoteDetails[i].push[j].pushActivatedTime);
            		   			var timeDiff = Math.abs(date2.getTime() - date1.getTime());
            		   			var differenceInDays = (Math.ceil(timeDiff / (1000 * 3600 * 24))-1);

            		   			if(differenceInDays >= 0 && differenceInDays <=3){
            		   				return true;
            		   			}
        			   }

            	   }
            	   else{
            		 //  return false;
            	   }
            	  // return false;

               }*/

        }

       else {
    	   return true; //Anyother option selected then return true; tell it satisfies the condition.
       }
    };

    $scope.showPostsBasedOnSearchText = function(post){

    	if($scope.searchQuery === undefined){
    		//don't do anything,return all.
    		return true;
    	}
    	else if($scope.searchQuery === ''){
    		//don't do anything,return all.
    		return true;
    	}
    	else if($scope.searchQuery !== undefined){


        var searchQueryString = $scope.searchQuery.toLowerCase();

         if($scope.postsResponse !== undefined){
           if($scope.postsResponse.posts !== undefined){
	           for(var i=0;i< $scope.postsResponse.posts.length ; i++){
	        		    //console.log("Searching in Titles!");
	        		    if($scope.postsResponse.posts[i].title !== undefined || $scope.postsResponse.posts[i].title !== null )
	        			{
	        		    	if($scope.postsResponse.posts[i].title !== null){
			        			var titleName = $scope.postsResponse.posts[i].title.toLowerCase();
			             		if(post.title === $scope.postsResponse.posts[i].title){
			        					if(titleName.indexOf(searchQueryString)!=-1){
			        							//console.log("Found!!######### it was in one of the TITLEs...");
			        							return true;
			        					}
			        			}
	        		    	}

	        			}

	             		//console.log("Not Found in Titles!");

	             		//console.log("Now Searching in Body!");
	             		if($scope.postsResponse.posts[i].body !== undefined || $scope.postsResponse.posts[i].body !== null )
	             		{
	             			if($scope.postsResponse.posts[i].body !== null){
			        			var bodyName = $scope.postsResponse.posts[i].body.toLowerCase();
			        			if(post.body === $scope.postsResponse.posts[i].body){
			    					if(bodyName.indexOf(searchQueryString)!=-1){
			    						//console.log("Found!!######### it was in one of the BODYs...");
										return true;
			    					}
			        			}
		        			}

	             		}
	        			//console.log("Not Found in Body!");

	             		//console.log("Now Searching in Caption!");
	             		if($scope.postsResponse.posts[i].body !== undefined || $scope.postsResponse.posts[i].body !== null )
	             		{
	             			if($scope.postsResponse.posts[i].body !== null){
			        			var captionName = $scope.postsResponse.posts[i].body.toLowerCase();
			        			////console.log("captionName:"+captionName);
			        			if(post.body === $scope.postsResponse.posts[i].body){
			    					if(captionName.indexOf(searchQueryString)!=-1){
			    						//console.log("Found!!######### it was in one of the captionNames...");
										return true;
			    					}
			        			}
		        			}

	             		}
	        			//console.log("Not Found in Caption!");

	             		//console.log("Now Searching in Post Questions!");
	             		if($scope.postsResponse.posts[i].title !== undefined || $scope.postsResponse.posts[i].title !== null )
	             		{
	             			if($scope.postsResponse.posts[i].title !== null){
			        			var postQuestion = $scope.postsResponse.posts[i].title.toLowerCase();
			        			if(post.title === $scope.postsResponse.posts[i].title){
			    					if(postQuestion.indexOf(searchQueryString)!=-1){
			    						//console.log("Found!!######### it was in one of the postQuestion...");
										return true;
			    					}
			        			}
		        			}

	             		}
	        			//console.log("Not Found in Post Questions!");

	             		//console.log("Now Searching in Post Thoughts!");
	             		if($scope.postsResponse.posts[i].body !== undefined || $scope.postsResponse.posts[i].body !== null )
	             		{
	             			if($scope.postsResponse.posts[i].body !== null){
			        			var postThought = $scope.postsResponse.posts[i].body.toLowerCase();
			        			if(post.body === $scope.postsResponse.posts[i].body){
			    					if(postThought.indexOf(searchQueryString)!=-1){
			    						//console.log("Found!!######### it was in one of the postThought...");
										return true;
			    					}
			        			}
		        			}

	             		}
	        			//console.log("Not Found in Post Thoughts!");

	             		//console.log("Now Searching in Post Link!");
	             		if($scope.postsResponse.posts[i].title !== undefined || $scope.postsResponse.posts[i].title !== null )
	             		{
	             			if($scope.postsResponse.posts[i].title !== null){
			        			var postLink = $scope.postsResponse.posts[i].title.toLowerCase();
			        			if(post.title === $scope.postsResponse.posts[i].title){
			    					if(postLink.indexOf(searchQueryString)!=-1){
			    						//console.log("Found!!######### it was in one of the postLink...");
										return true;
			    					}
			        			}
		        			}

	             		}
	        			//console.log("Not Found in Post Link!");

	             		//console.log("Now Searching in Post Description!");
	             		if($scope.postsResponse.posts[i].body !== undefined || $scope.postsResponse.posts[i].body !== null )
	             		{
	             			if($scope.postsResponse.posts[i].body !== null){
			        			var postDescription = $scope.postsResponse.posts[i].body.toLowerCase();
			        			if(post.body === $scope.postsResponse.posts[i].body){
			    					if(postDescription.indexOf(searchQueryString)!=-1){
			    						//console.log("Found!!######### it was in one of the postDescription...");
										return true;
			    					}
			        			}
		        			}

	             		}
	        			//console.log("Not Found in Post Description!");

	        			//console.log("Now Searching in ToField!");

	        			if($scope.postsResponse.posts[i].toField !== undefined || $scope.postsResponse.posts[i].toField !== null )
	        			{
	        				//console.log("$scope.postsResponse.posts[i].toField:"+$scope.postsResponse.posts[i].toField);
	        				if($scope.postsResponse.posts[i].toField !== null){
			        			var toFieldString =  $scope.postsResponse.posts[i].toField.toLowerCase();
			        			if(post.toField === $scope.postsResponse.posts[i].toField){
			    					if(toFieldString.indexOf(searchQueryString)!=-1){
			    						//console.log("Found!!######### it was in one of the ToField...");
										return true;
			    					}
			        			}
	        				}

	        			}

	        			//console.log("Not Found in ToField!");

	        			//console.log("Now Searching in FromField!");
	        			if($scope.postsResponse.posts[i].fromField !== undefined || $scope.postsResponse.posts[i].fromField !== null )
	        			{
	        				if($scope.postsResponse.posts[i].fromField !== null){
			        			var fromFieldString =  $scope.postsResponse.posts[i].fromField.toLowerCase();
			        			if(post.fromField === $scope.postsResponse.posts[i].fromField){
			    					if(fromFieldString.indexOf(searchQueryString)!=-1){
			    						//console.log("Found!!######### it was in one of the FromField...");
										return true;
			    					}
			        			}
		        			}
	        			}

	        			//console.log("Not Found in FromField!");


	        			//console.log("Now Searching in Tags!");
	        			if($scope.postsResponse.posts[i].tags !== undefined || $scope.postsResponse.posts[i].tags !== null )
	        			{
		        			var tagArrayString =  $scope.postsResponse.posts[i].tags;
		        			tagArrayString = tagArrayString+'';
		        			var tagArray = new Array();
		        			tagArray = tagArrayString.split(',');

		        			for(var j=0;j<post.tags.length;j++)
		        			{
		        				for(var k=0;k<tagArray.length;k++){
		                			if(post.tags[j] === tagArray[k]){
		            					if(tagArray[k].toLowerCase().indexOf(searchQueryString)!=-1){
		            						//console.log("Found!!######### it was in one of the Tags...");
		        							return true;
		            					}
		                			}
		        				}

		        			}
	        			}
	        			//console.log("Not Found in Tags!");


	        			//console.log("Now Searching in Comments!");
	        			for(var j=0;j<$scope.postsResponse.posts[i].comments.length;j++)
	        			{
	        				if($scope.postsResponse.posts[i].comments[j].commentContent !== undefined || $scope.postsResponse.posts[i].comments[j].commentContent !== null )
	        					{
		                			var commentDescription = $scope.postsResponse.posts[i].comments[j].commentContent.toLowerCase();
		                			if(post.id === $scope.postsResponse.posts[i].id){
		            					if(commentDescription.indexOf(searchQueryString)!=-1){
		            						//console.log("Found!!######### it was in one of the Posts Comments...");
		            							return true;
		            					}
		                			}
	        					}
	        			}

	        			//console.log("Not Found in Comments!");
	        			
	        			//console.log("Now Searching in LocationArea-------------");
	        			if($scope.postsResponse.posts[i].location !== undefined && $scope.postsResponse.posts[i].location !== null )
	        			{
	        				if($scope.postsResponse.posts[i].location !== null){
			        			var locationString =  $scope.postsResponse.posts[i].location.toLowerCase();
			        			if(post.location === $scope.postsResponse.posts[i].location){
			    					if(locationString.indexOf(searchQueryString)!=-1){
			    						////console.log("Found!!######### it was in one of the Location...");
										return true;
			    					}
			        			}
		        			}
	        			}

	        			//console.log("Not Found in LocationArea---------------");   
	        			
	        			//console.log("Now Searching in Usernames-------------");
	        			if($scope.postsResponse.posts[i].username !== undefined && $scope.postsResponse.posts[i].username !== null )
	        			{
	        				if($scope.postsResponse.posts[i].username !== null){
			        			var usernameString =  $scope.postsResponse.posts[i].username.toLowerCase();
			        			if(post.username === $scope.postsResponse.posts[i].username){
			    					if(usernameString.indexOf(searchQueryString)!=-1){
			    						////console.log("Found!!######### it was in one of the usernames...");
										return true;
			    					}
			        			}
		        			}
	        			}

	        			//console.log("Not Found in Usernames---------------");   	        			

	        	}
           }
           else{
        	   console.error("##inside main.controller.js -- $scope.postsResponse.posts is undefined");
           }
        }
         else{
        	 console.error("##inside main.controller.js -- $scope.postsResponse is undefined");
         }

        	return false;
    	}

    };

    $scope.showPostsBasedOnUserLoggedIn = function(post){
    	////console.log("##inside main.controller.js -- showPostsBasedOnUserLoggedIn --- ");
    };

    $scope.showTypeSelectedPostsOnly = function(post){

    	if($scope.showTypeSelectPosts){

        	var checkedValue = '';
        	$scope.selectedTypeListArray =[];

        	var inputElements = document.getElementsByClassName('typeCheckBox');
            for(var i=0; inputElements[i]; ++i){
            	  if(inputElements[i].checked){
            	         checkedValue = inputElements[i].value;
            	      //   ////console.log("loop i:"+i+"checkedValue :"+checkedValue);
            	         $scope.selectedTypeListArray.push(checkedValue);
            	         // break;
            	   }
           }

        	$scope.selectedTypeList1 =$scope.selectedTypeListArray;

        	if(($scope.selectedTypeList1.indexOf("NSFW")!== -1 && post.isNSWF === true && post.isNSWF!== null) ||
               ($scope.selectedTypeList1.indexOf("TO/FROM")!== -1 && (post.toField !== "" || post.fromField !== "") && (post.toField !== null || post.fromField !== null)))
        	{
        		////console.log("YES - NSFW or TO/FROM POSTS FOUND!!");
          		return true;
        	}
        	if(
        		($scope.selectedTypeList1.indexOf(post.postType)!=-1)
        	   )
        	{
        		////console.log("YES - General,Event,Question,Link,Visual POSTS FOUND!!");
          		return true;
        	}
        	else{
        		////console.log("NO - General,Event,Question,Link,Visual,To/From,NSFW POSTS ARE NOT FOUND!!");
        	   return false;
        	}
    	}
    	else{
    		//////console.log("NOT DOUBLE CLICKED!!!! -- CNT CHANGE");
    	}
    };


    $scope.callChangePassword = function(url){
    	$location.path(url);
    };



    $scope.resetThePosts = function(selectedDisplayCriteria){
    	$rootScope.clearFlagsOnPageLoad();
    	if(selectedDisplayCriteria === 'Saved'){
    		$scope.showLoader();
    		 postService.getSavedPosts($rootScope.loggedInId).then(getSavedPostsSuccess, getSavedPostsError);
    	}else if(selectedDisplayCriteria === 'My Posts'){
    		$scope.showLoader();
    		postService.getMyPosts($rootScope.loggedInId).then(getMyPostsSuccess, getMyPostsError);
    	}
    };
    
	 function getSavedPostsSuccess(response){
		
		// response = {"posts":[{"id":22,"title":"SAVED Work in Progress on server side","body":"New Body","tags":[],"postType":"VISUAL","postCategory":"CASUAL","postUserInputCategory":"","postCreatedTime":"Wed May 23 1900 12:26:00 GMT+0530 (India Standard Time)","locationArea":"Jalahalli Police Station, BEL Colony, Bengaluru, Karnataka, India","longitude":77.55045500000006,"latitude":13.048769,"imgPath":"","toField":"","fromField":"","username":"Chandan1","userId":"user_Chandan1231","anonymous":false,"nswf":null,"expand":false,"enableNotificationFromUser":false,"eventDate":"930508200000","eventDescription":"Event Description","caption":"This image was captured last year!","postQuestion":"Post Question","postThought":"Post Thought","postLink":"Post Link","postDescription":"Link Description","savedPost":false,"voteDetails":{"voteCount":1,"upVote":false,"downVote":true,"upVotedTimeByMe":"","downVotedTimeByMe":"","pushActivatedTimeByMe":"","pushCount":0,"pushActivate":false,"pushActivated":false,"reportedNSFW":true,"reportedIsNSFW":false,"reportedNSFWReason":""},"comments":[{"commentId":"P1C1","commentPostedBy":"CommentUser1a","commentContent":"comment1 -  User1a - 1st Post","upVote":false,"downVote":true,"voteCount":5,"commentPostedTime":"","replies":[{"replyId":"P1C1R1","replyCommentBy":"Replier1ForCommentUser1a","replyCommentContent":"reply1 - comment1 -  User1a - 1st Post","voteCount":1,"replyCommentTime":"","upVote":true},{"replyId":"P1C1R2","replyCommentBy":"Replier2ForCommentUser1a","replyCommentContent":"reply2 - comment1 -  User1a - 1st Post","voteCount":4,"replyCommentTime":"","upVote":false}]},{"commentId":"P1C2","commentPostedBy":"CommentUser2a","commentContent":"comment2 -  User2a - 1st Post","upVote":false,"downVote":false,"voteCount":4,"commentPostedTime":"","replies":[{"replyId":"P1C2R1","replyCommentBy":"Replier1ForCommentUser2","replyCommentContent":"reply1 - comment2 -  User2a - 1st Post","voteCount":3,"replyCommentTime":"","upVote":false},{"replyId":"P1C2R2","replyCommentBy":"Replier2ForCommentUser2","replyCommentContent":"reply2 - comment2 -  User2a - 1st Post","voteCount":1,"replyCommentTime":"","upVote":false}]},{"commentId":"P1C3","commentPostedBy":"CommentUser3a","commentContent":"comment3 -  User3a - 1st Post","upVote":false,"downVote":false,"voteCount":6,"commentPostedTime":"","replies":[]},{"commentId":"P1C4","commentPostedBy":"CommentUser4a","commentContent":"comment4 -  User3a - 1st Post","upVote":false,"downVote":false,"voteCount":2,"commentPostedTime":"","replies":[]}]}]};
		 $scope.data.serverPosts = response.posts;
		 $scope.hideLoader();
	 };
	 
	 function getSavedPostsError(error){
		 console.error("Facing some issue in retrieving SAVED IDs from server");
		 console.error(error);
		
		 $scope.hideLoader();
		 
		 var errorMessage = "Facing some issue in retrieving SAVED IDs from server";
		 $rootScope.$broadcast("showErrorMessageOnMainScreen",errorMessage);
		 $rootScope.$broadcast("hideLoader","BackendCallDone");
		 
	 };
 
	 function getMyPostsSuccess(response){
		 	 $scope.data.serverPosts = response.posts;
			 $scope.hideLoader();
	 };
		 
     function getMyPostsError(error){
			 console.error("Facing some issue in retrieving your IDs from server");
			 console.error(error);    	 
			 $scope.hideLoader();
			 var errorMessage = "Facing some issue in retrieving your IDs from server";
			 $rootScope.$broadcast("showErrorMessageOnMainScreen",errorMessage);
			 $rootScope.$broadcast("hideLoader","BackendCallDone");			 
	 };	 
 
    $scope.switchOnTypesCategories = function(){
    	$scope.typesAllOn = true;
       	
       	/*The below commented code doesn't work in IE; fix below it - START*/
    	//$scope.selectedCategories = Array.from(ID_CATEGORIES);
        $scope.selectedCategories = [];
    	var length = ID_CATEGORIES.length;
		for (var i = 0; i < length; i++) {
			$scope.selectedCategories.push(ID_CATEGORIES[i]);
		}
		/*The above commented code doesn't work in IE; fix below it - END*/
    	$scope.switchedOn = true;
    	$scope.switchedOff = false;
    };
    $scope.$on("switchOnTypesCategoriesCall", function(){
    	$scope.switchOnTypesCategories();
      });
    $scope.switchOffTypesCategories = function(){
    	$scope.typesAllOn = false;
    	$scope.selectedCategories = [];//Assign no values which are to be checked.
    	$scope.switchedOn = false;
    	$scope.switchedOff = true;
    };

  };
