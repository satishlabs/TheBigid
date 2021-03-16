'use strict';

/**
 * @ngdoc directive
 * @name tbidApp.directive:post
 * @description
 * # post
 */
angular.module('tbidApp')
  .directive('post',['IMG_PATH','$rootScope','$timeout','postService','$filter','myProfileService','loginService','preLoginService', function (IMG_PATH,$rootScope,$timeout,postService,$filter,myProfileService,loginService,preLoginService) {

    return {

    	scope:{
	    	fulldata: '=',
	    	data:'=',
	    	expand: '&',
	    	showreplies:'=',
	    	showrepliesfunc:'&',
	    	userdata : '=',
	    	selecteddisplaycriteria : "="
    	},
    	templateUrl: 'views/templates/post.html',
    	restrict: 'E',
    	controller : ["modalService", "createPostService","$scope","$rootScope","postService",function (modalService,createPostService,$scope,$rootScope,postService){

    		/*Initializations - START */
    		$scope.followSectionError = false;
    		$scope.followSectionErrorMsg = "";
    		$scope.showFollowNotifySection = false;
   		 	$scope.postServerError = "";
   		 	$scope.showPostServerError = false;
   		 	/*Initializations - END */
   		 	
   		 	$scope.checkVoteDefaultCase = function(){
   		 			if($scope.data.voteDetails.voteCount === 0){
		   		 			$scope.data.voteDetails.upVote = false;
		   		   		 	$scope.data.voteDetails.downVote = false;
   		 			}
   		 	}
   		 	
    			$scope.downVotedTag = function (postId,title) {

    					var modalOptions = {
    							closeButtonText: 'Back',
    							actionButtonText: 'Tell Moderator',
    							headerText: 'Choose a Reason :',
    						 // bodyText: messageContent
    							modalForPostTitle : title
    					};

    					modalService.showModal({}, modalOptions).then(function (result) {

    							var packageData = {};
    							packageData.reasonForNsfw = result;
    							packageData.postId = postId;
    							packageData.postTitle = title;
    							packageData.userId = $rootScope.userData.loggedInId;


    							createPostService.reportPost(packageData).then(function(response){

    	                			$scope.data.voteDetails.reportedIsNSFW = response.reportedIsNSFW;

    							},function(error){
    								//console.error("##inside post.js -- loadModalService -- REPORTING TO MODERATOR FAILED.");
    								console.error(error);
    							});
    					});
    			};

    			$scope.formatDate = function(fetchedDate){
    				var formattedDate = new Date(fetchedDate);
    				//$scope.eventDateDisplay = (formattedDate.getDate()) + "/" +(formattedDate.getMonth() + 1) + "/"+ (formattedDate.getFullYear());
    				$scope.eventDateDisplay = $filter('date')(fetchedDate, 'MMMM d, y');
    			};    			
    			
    			$scope.checkHighestLikedComment = function(commentsArray){
    				$scope.displayComment = '';
    				if(commentsArray.length > 0)
    				{
        				if(commentsArray.length > 1){
        					var highestLiked = commentsArray[0].commentLikes;
        					var highestLikedCommentIndex =0;
        					for(var i=1;i<commentsArray.length;i++){  
        						if(commentsArray[i].commentVoteDetails.voteCount > highestLiked){
        							highestLikedCommentIndex = i;
        							highestLiked = commentsArray[highestLikedCommentIndex].commentVoteDetails.voteCount;
        						}
        					}        					
        					$scope.displayComment = commentsArray[highestLikedCommentIndex].commentContent;
        				}
        				else{
        					$scope.displayComment = commentsArray[0].commentContent;
        				}
        			}

    			};
    			
		        $scope.savePost = function(postId){
			          
		             var dataToServer = {};
		             dataToServer.userId = $rootScope.userData.loggedInId;
		             dataToServer.postId = postId;
		             
		             if(!$scope.data.postSavedbythisUser){
			        	 postService.savePost(dataToServer).then(function(response){
			        		 $scope.data.postSavedbythisUser = true;
			        		 $scope.data.savedPostCount = $scope.data.savedPostCount+1;
			        		 
			        		 /* Code to open the follow section
			        		  * if(!$scope.checkOwnerOfThePost($scope.data.userId)){
			        			 $scope.showFollowNotifySection = true;
			        		 }*/
			        		 
			        		 $scope.resetPostErrors();//Reset if any errors are already displayed on screen.
			        		 
			        	 },function(error){
		        		 
			        		 $scope.postServerError = "Sorry.We are unable to save your post due to some issue. Please try again after sometime.";
			        		 $scope.showPostServerError = true;
			        		 
			        	 });
		             }

		        	
		        };
		        
		        
		        $scope.resetPostErrors = function(){
		   		 	$scope.postServerError = "";
		   		 	$scope.showPostServerError = false;
		        };

      }],
      link: function postLink(scope, element, attrs) {

    	  	scope.expandedPostId = (attrs['expandedPostId'])?Number(attrs['expandedPostId']):0;
    	  	scope.titleCharCount = (attrs['titleCharCount'])?Number(attrs['titleCharCount']):-1;
    	  	scope.bodyCharCount = (attrs['bodyCharCount'])?Number(attrs['bodyCharCount']):-1;
    	  	scope.showTypeIcon = attrs['showTypeIcon']==="true";
    	  	scope.followedUser = false;//default initialization

    	  	/*flags for error messages for posting comments/replies - START */
    	  	scope.errorMessageForComment = '';
    	  	scope.errorMessageForReply = '';
    	  	/*flags for error messages for posting comments/replies - END */

    	  	scope.pathOfImage = IMG_PATH;
    	  	scope.data.pushAvailabilityNo = scope.userdata.pushAvailabilityNo;
    	  	scope.selectedDisplayCriteria =  scope.$root.selectedDisplayCriteria;

    	  	scope.updateDisplayCriteria = function(){
    	  			scope.displayCriteria =  scope.selecteddisplaycriteria;
    	  	};

    	  	/*Provide default images for the posts if there is no image from response for that category*/

    	  	if($rootScope.isStubbedMode){
		            if(scope.data.imgPath === ''){
		            	if(scope.data.postType === 'GENERAL'){
		            		scope.data.imgPath = "defaultgeneral.png";
		            	}else if(scope.data.postType === 'EVENT'){
		            		scope.data.imgPath = "defaultevent.png";
		            	}else if(scope.data.postType === 'VISUAL'){
		            		scope.data.imgPath = "defaultvisual.png";
		            	}else if(scope.data.postType === 'QUESTION'){
		            		scope.data.imgPath = "defaultquestion.png";
		            	}else if(scope.data.postType === 'LINK'){
		            		scope.data.imgPath = "defaultlink.png";
		            	}
		            }
		            scope.data.imgPathDisplay = scope.pathOfImage+scope.data.imgPath;//changed for Image issue fix on MainPage
    	  	}
    	  	else{


    	  		//console.log("scope.data.imgPath:"+scope.data.imgPath);
	        	if(scope.data.imgPath === '' || scope.data.imgPath === null){
	            	/*if(scope.data.postType === 'GENERAL'){
	            		scope.data.imgPath = "/resources/defaultImg/defaultgeneral.png";
	            	}else if(scope.data.postType === 'EVENT'){
	            		scope.data.imgPath = "/resources/defaultImg/defaultevent.png";
	            	}else if(scope.data.postType === 'VISUAL'){
	            		scope.data.imgPath = "/resources/defaultImg/defaultvisual.png";
	            	}else if(scope.data.postType === 'QUESTION'){
	            		scope.data.imgPath = "/resources/defaultImg/defaultquestion.png";
	            	}else if(scope.data.postType === 'LINK'){
	            		scope.data.imgPath = "/resources/defaultImg/defaultlink.png";
	            	}*/

	            	//scope.data.imgPathDisplay = $rootScope.createUrlForImage(scope.data.imgPath);
	            	//scope.data.imgPathDisplay = myProfileService.userProfilePictureImage;
	            	
	            	
	            	if(scope.data.userId === scope.$root.userData.loggedInUserId){
	            		scope.data.imgPathDisplay = myProfileService.userProfilePictureImage;
	            	}else{
	            		scope.data.imgPathDisplay = '';
	            	}
	        	}
	        	else{
	        		 scope.data.imgPathDisplay = scope.$root.createUrlForImage(scope.data.imgPath);
	        	}
    	  	}


	        scope.$watch(
	        function()//newValue
	        {
	        	return element.attr('expanded-post-id')
	        },
	        function(val) //oldValue
	        {
	          scope.data.expand = val==scope.data.id?true:false;
	        }
	        );



	        scope.formatDateToDisplay = function(fetchedDate){
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
	            	scope.displayTimeInfo = $filter('date')(fetchedDate, 'MMMM d, y');
	            }
	            else{
	            	var minChar = '';
                    var hourChar = '';
                    
	            	if(minutes > 60){
	            		var minsDisplay = minutes % 60;
	            		var hoursDisplay = (minutes - minsDisplay) / 60;
	            		if((hoursDisplay) <= 1){ hourChar  = " Hour "; } else { hourChar  = " Hours ";}
	            		if((minsDisplay) <= 1){ minChar = " Minute Ago"; } else { minChar = " Minutes Ago";}
	            		scope.displayTimeInfo = hoursDisplay + hourChar + minsDisplay+ minChar;
	            	}
	            	else{	
	            		if((minutes) <= 1){ minChar = " Minute Ago"; } else { minChar = " Minutes Ago";}
	            		if(minutes === 0){	
	            			scope.displayTimeInfo = "Just now";
	            		}
	            		else{
	            			scope.displayTimeInfo = minutes + minChar;
	            		}
	            	}            	
	            }
	        };

	        scope.doPushUpVote = function(postId,voteCount,index){

	            var dataToServer = {};
	            dataToServer.voteCount = voteCount + 2;
	            dataToServer.postid = postId;
	            dataToServer.voteType= 'NOT_VOTED';
	            dataToServer.upVote= false;
	            dataToServer.downVote= false;
	            dataToServer.pushActivate= true;
	            dataToServer.userId= scope.$root.userData.loggedInId;


	            postService.doPushUpVote(dataToServer).then(function(response){

	            			    scope.data.voteDetails.voteCount = response.voteCount;
	                			scope.data.voteDetails.upVote = response.upVote;
	                			scope.data.voteDetails.downVote = response.downVote;
	                			scope.data.voteDetails.pushCount = response.pushCount;
	                			scope.data.voteDetails.pushActivate = response.pushActivate;
	                			scope.data.voteDetails.pushActivated = response.pushActivated;
	                			scope.data.voteDetails.reportedNSFW = response.reportedNSFW;
	                			scope.data.voteDetails.reportedIsNSFW = response.reportedIsNSFW;
	                			scope.data.voteDetails.reportedNSFWReason = response.reportedNSFWReason;
	                			scope.data.voteDetails.upVotePushed = true; //response.upVote
	                			$rootScope.getProfileInformation();//inorder to update the push details in the userdetails section.
	   		        		    scope.resetPostErrors();//Reset if any errors are already displayed on screen.
	            		
	                },function(error){
			          	  //console.error("XXXXXX: ##inside post.js directive -- doPushUpVote method -- in ERROR response section");
			        	  //console.error(error);
			        
			        	  scope.postServerError = "Sorry.We were unable to push due to some issue. Please try again after sometime.";
			        	  scope.showPostServerError = true;
			        	  
	                 });

	         };

	         scope.doUpVote = function(postId,voteCount){

		            var dataToServer = {};
		            dataToServer.voteType= 'UP';
		            dataToServer.upVote= true;
		            dataToServer.downVote= false;
		            dataToServer.pushActivate= false;
		            dataToServer.userId= scope.$root.userData.loggedInId;
		            dataToServer.postid = postId;

		            postService.doUpVoteUpdate(dataToServer).then(function(response){

	            			if(scope.data.voteDetails !== undefined){
	            				scope.data.voteDetails ={};
	            			}
	            			scope.data.voteDetails.voteCount = response.voteCount;
	            			//scope.data.voteDetails.voteCount = voteCount+1;
	            			scope.data.voteDetails.upVotePushed = response.upVote;
	            			scope.data.voteDetails.upVotedTimeByMe = new Date(response.upVotedTimeByMe);

	            			scope.data.voteDetails.upVote = response.upVote;
	            			scope.data.voteDetails.pushActivate = response.pushActivate;
	            			scope.data.voteDetails.downVote = response.downVote;
	            			scope.data.voteDetails.reportedNSFW = response.reportedNSFW;
	            			scope.data.voteDetails.reportedNSFWReason = response.reportedNSFWReason;
	            			
	            			 scope.checkVoteDefaultCase();
	            			 
	            			 
   		        		    scope.resetPostErrors();//Reset if any errors are already displayed on screen.
	        			
		        	},function(error){
			        	  //console.error("XXXXXX: ##inside post.js directive -- doUpVote method -- in ERROR response section");
			        	  //console.error(error);
			        	  
			        	  scope.postServerError = "Sorry.We were unable to upvote due to some issue. Please try again after sometime.";
			        	  scope.showPostServerError = true;
		        	});
	         };

	         scope.doDownVote = function(postId,voteCount){
		            var dataToServer = {};
		            dataToServer.voteType= 'DOWN';
		            dataToServer.upVote= false;
		            dataToServer.downVote= true;
		            dataToServer.pushActivate= false;
		            dataToServer.userId= scope.$root.userData.loggedInId;
		            dataToServer.postid = postId;


		            postService.doDownVoteUpdate(dataToServer).then(function(response){

	        	            			if(scope.data.voteDetails !== undefined){
	        	            				scope.data.voteDetails ={};
	        	            			}
	        	            			scope.data.voteDetails.voteCount = response.voteCount;
	        	            			//scope.data.voteDetails.voteCount = voteCount-1;
	        	            			scope.data.voteDetails.downVotedTimeByMe = new Date(response.downVotedTimeByMe);
	                        			scope.data.voteDetails.downVote = response.downVote;
	                        			scope.data.voteDetails.reportedNSFW = response.reportedNSFW;

	                   				     scope.data.voteDetails.upVote = response.upVote;
	                   				     scope.data.voteDetails.pushCount = response.pushCount;
	                 	           		 scope.data.voteDetails.pushActivate = response.pushActivate;
	                   				     scope.data.voteDetails.pushActivated = response.pushActivated;
	                        			 scope.data.voteDetails.reportedIsNSFW = response.reportedIsNSFW;
	                        			 scope.data.voteDetails.reportedNSFWReason = response.reportedNSFWReason;
	                        			 
	                        			 scope.checkVoteDefaultCase();
	                        			 
	     	   		        		     scope.resetPostErrors();//Reset if any errors are already displayed on screen.
        	        			

		            }, function(error){
		            	 //console.error("XXXXXX : ##inside post.js directive -- doDownVote method -- in error response section");
		            	 //console.error(error);
		            	 
			        	 scope.postServerError = "Sorry.We were unable to downvote due to some issue. Please try again after sometime.";
			        	 scope.showPostServerError = true;
		            });

	         };

	         scope.doCommentUpVote = function(postId,commentId,index){

			            var dataToServer = {};
			        	dataToServer.commentId = commentId;
			        	dataToServer.postId = postId;
			        	dataToServer.userId= scope.$root.userData.loggedInId;
			        	  
			        	postService.doCommentUpVoteUpdate(dataToServer).then(function(response){
			        		
		        	 	    	if(scope.data.comments[index].commentVoteDetails === null){
		        	 	    		scope.data.comments[index].commentVoteDetails = {};
		        	 	    	}		        	 	  
			            		scope.data.comments[index].commentVoteDetails.voteCount =  response.voteCount;
			                    scope.data.comments[index].commentVoteDetails.upVote = response.upVote;
			                    scope.data.comments[index].commentVoteDetails.downVote = response.downVote;

	   		        		    scope.resetPostErrors();//Reset if any errors are already displayed on screen.
	   		        		    
			        		},function(error){
			        			//console.error("XXXXXX : ##inside post.js directive -- doCommentUpVote method -- in error response section");
			        			//console.error(error);

			        			scope.postServerError = "Sorry.We were unable to upvote due to some issue. Please try again after sometime.";
			        			scope.showPostServerError = true;
			            	 
			          });    
	         };

	        scope.doCommentDownVote = function(postId,commentId,index){
	        	
		            var dataToServer = {};
		        	dataToServer.commentId = commentId;
		        	dataToServer.postId = postId;
		        	dataToServer.userId= scope.$root.userData.loggedInId;

		        	postService.doCommentDownVoteUpdate(dataToServer).then(function(response){		        	 	
		        	 	    	if(scope.data.comments[index].commentVoteDetails === null){
		        	 	    		scope.data.comments[index].commentVoteDetails = {};
		        	 	    	}
			        			scope.data.comments[index].commentVoteDetails.voteCount = response.voteCount;
			                    scope.data.comments[index].commentVoteDetails.upVote = response.upVote;
			                    scope.data.comments[index].commentVoteDetails.downVote = response.downVote;		
			                    
	   		        		    scope.resetPostErrors();//Reset if any errors are already displayed on screen.
	    
		             }, function(error){
			         	 //console.error("XXXXXX : ##inside post.js directive -- doCommentDownVote method -- in error response section");
			        	 //console.error(error);
			        	 
		            	 scope.postServerError = "Sorry.We were unable to downvote due to some issue. Please try again after sometime.";
		            	 scope.showPostServerError = true;
		            	 
		             });    
	        };

	        scope.updateCommentSection = function(){
		        	scope.$root.showLoader();
		        	scope.$root.hideLoader();
	        };

	        scope.postComment = function(postId,newCommentMessage,postOwnerId){

			        	if(newCommentMessage !== ''){
			        		var showErrorResponseForPostComment = false;
		
				        	var dataToServer = {};
				        	dataToServer.postid = postId;
				        	dataToServer.postownerid = postOwnerId;
				        	dataToServer.commentContent = newCommentMessage;
				        	dataToServer.userId = $rootScope.userData.loggedInUserId;
				        	dataToServer.commentPostedBy = $rootScope.userData.loggedInUserName;
		
				        	postService.updatePostWithNewComment(dataToServer).then(function(response){
		
						        			scope.refreshCommentsSection(postId);
						        			scope.newComment = '';
						        			//showErrorResponseForPostComment = false;
						        			//scope.errorMessageForComment = '';
				   		        		    scope.resetPostErrors();//Reset if any errors are already displayed on screen.
		
						        	},function(error){
							           	 	//showErrorResponseForPostComment = true;
							           	 	//scope.errorMessageForComment = 'There was some issue while trying to post your comment';
							           	 
							           	 	scope.postServerError = "Sorry.We were unable to post your comment due to some issue. Please try again after sometime.";
							           	    scope.showPostServerError = true;
							           	 	
						        	});
		        			
			        	}
			   };

	       scope.showrepliesfunc = function(replyObj,showReplyCommand,commentId){
			        	//added for comment section for each reply - START
			        	scope.parentCommentIdForReply = commentId;
			        	scope.newReplyMsg="";
			        	//added for comment section for each reply - END

			        	if(showReplyCommand == false)
			        	{
			        		scope.showreplies = false;//show the post preview on the right hand side of the expanded comment window
			        	}
			        	else{
			        		if(replyObj == undefined || replyObj.length === 0){
			        			scope.showreplies = false;//show the post preview on the right hand side of the expanded comment window
			        		}
			        		else if(replyObj !== undefined){
			        			scope.showreplies = true;//show the replies for the comment clicked.
			        			scope.replyObj = replyObj;
			        		}
			        	}
	       		};

		        scope.closeRepliesSection = function(){
		          scope.showreplies = false;
		        };

		        scope.followUserUpdate = function(postId,userId,updateFollow){

		        	        var dataToServer = {};
				        	dataToServer.followUserStatus = updateFollow;
				        	dataToServer.postId = postId;
				        	dataToServer.userId = $rootScope.userData.loggedInId;

				        	postService.followUserUpdateToServer(dataToServer).then(function(response){
				        		
				        		scope.loggedInUserFollowing = (updateFollow === 'follow')? true : false;
				        		
				        		var loggedInUserName = preLoginService.loggedInUserName;
				        		loginService.getProfileInformation(loggedInUserName).then(function(response){
				        			scope.$root.userData.followers = response.data.followers;
				        		}, function(error){
				        			console.error(error);
				        		});
				        		
	   		        		    scope.resetPostErrors();//Reset if any errors are already displayed on screen.
				        		
				        	},function(error){				        		
				        		scope.postServerError = "Sorry. We were unable to follow the user due to some issue.Please try again after sometime.";
				        		scope.showPostServerError = true;
				        		
				        	});

		        };
		        
				scope.clearFollowSectionFlags = function(){
	        		scope.followSectionError = false;
					scope.followSectionErrorMsg = "";
				}
				
		        scope.updateNotificationForUserPost = function(userId,postNotification,postId){

				        	var dataToServer = {};
				        	dataToServer.userId = userId;
				        	dataToServer.postNotification = postNotification;
				        	dataToServer.postId = postId;
				        	
				        	postService.updateNotificationForUserPost(dataToServer).then(function(response){
				        			scope.data.postNotification = postNotification;
		   		        		    scope.resetPostErrors();//Reset if any errors are already displayed on screen.

				        	},function(error){
				        		//scope.followSectionError = true;
				        		//scope.followSectionErrorMsg = "Sorry something went wrong , Enable notification was not successful. Please try again after sometime!";
				        		scope.data.postNotification = !dataToServer.postNotification;
				        		
				        		scope.postServerError = "Sorry.We were unable to update your notification action due to some issue. Please try again after sometime.";
				        		scope.showPostServerError = true;
				        	});
		        };

		        scope.postReply = function(postId,newreplymsgcontent,parentCommentId,commentIndex){
                             if(newreplymsgcontent.length > 0){
				        	var showErrorResponseForCommentReply = false;

				        	var dataToServer={};
				    	    dataToServer = {

				    	    		"commentId" : parentCommentId,
				                    "postId":postId,
				    	    		"replies" : [
				    	    		             {
				    	    		            	 "replyCommentContent" : newreplymsgcontent,
				    	    		            	 "replyCommentBy": $rootScope.userData.loggedInUserName
				    	    		             }
				    	    		]

				    	    };

				    	    postService.postReplyForAComment(dataToServer).then(function(response){

				        		if(response === 'success') //on commenting a new reply - get the response as success and even get the new object of reply for appending it to that comment.
				        		{
						        	postService.fetchFreshComments(postId).then(function(response){
								        		scope.data.comments = response;
								        		scope.resetRepliesSectionOnReply(scope.data.comments[commentIndex].commentId,commentIndex);
						        	});
						        }

				        		scope.newreplymsgcontent = '';				        		
	   		        		    scope.resetPostErrors();//Reset if any errors are already displayed on screen.

				        	},function(error){
				           	 	
				        		scope.postServerError = "Sorry.There was some issue updating your reply section. Please try again after sometime.";
				        		scope.showPostServerError = true;
				        	});
		            }
		        };

		        scope.addTagsExpand = function(postOwnerId){
		        	/*if(scope.checkOwnerOfThePost(postOwnerId)){
		            	scope.openAddTagsExpansion = !scope.openAddTagsExpansion;
		            	scope.data.tagsAdded = scope.data.tags.join(',') ;
		        	}*/		        	
		        	scope.openAddTagsExpansion = !scope.openAddTagsExpansion;
		        	scope.data.tagsAdded = '';
		        };

		        
			    scope.updateTagsAdded = function(postId,newTags){

			        	var dataToServer = {};
			        	dataToServer.postId = postId;
			        	dataToServer.tags = [];
			        	dataToServer.tags.push(newTags);

			        	/*We are checking for any empty values entered in the tags - if there is any, we remove them and display others if available. - START*/
			        	var tagsSplitArray1 = new Array();
			        	var tagsSplitArray2 = new Array();
			        	var tagsSplitArray1 = newTags.split(',');
			        	var tagsSplitArray2 = [];

			        	for(var tagElement in tagsSplitArray1){
			        		//console.log("tagsSplitArray1["+tagElement+"]:"+tagsSplitArray1[tagElement]);
			        		if(tagsSplitArray1[tagElement] === ''){
			        			//don't do anything. Don't add this empty element in the array.
			        		}
			        		else{
			        		   tagsSplitArray2.push(tagsSplitArray1[tagElement]);
			        		}
			        	}
			        	
			           /*We are checking for any empty values entered in the tags - if there is any, we remove them and display others if available. - END*/
			           postService.updateTagsAddedToServer(dataToServer).then(function(response){
			        		//scope.data.tags = newTags.split(',');
			        		//scope.data.tags = tagsSplitArray2;//.split(',');
			        	   scope.data.tags=scope.data.tags.concat(tagsSplitArray2);
			        	   scope.openAddTagsExpansion = false;//close the section once after the update
   		        		   scope.resetPostErrors();//Reset if any errors are already displayed on screen.
			        	},function(error){
			        		//console.error("Tags are not updated successfully!");
			        		//console.error(error);
			        		
			        		scope.postServerError = "Sorry.We were unable to update your tags due to some issue. Please try again after sometime.";
			        		scope.showPostServerError = true;
			        		
			        	});
			        };

				   scope.clickedOnReplyCommentLikeButton = function(index,parentIndex,replyId){

				        	var dataToServer = {};
				        	dataToServer.replyId = replyId;
				        	dataToServer.userId= scope.$root.userData.loggedInId;
				        	dataToServer.commentId = parentIndex;
				            
				            postService.updateReplyCommentLikeToServer(dataToServer).then(function(response){

					        		if(scope.data.comments[parentIndex].replies[index] === undefined){
					        			scope.data.comments[parentIndex].replies[index] =[];
					        		}
					        		if(scope.data.comments[parentIndex].replies[index].postReplyCommentVote === null){
					        			scope.data.comments[parentIndex].replies[index].postReplyCommentVote ={};
					        		}
					        		scope.data.comments[parentIndex].replies[index].postReplyCommentVote.upVote = response.upVote;
					        		scope.data.comments[parentIndex].replies[index].postReplyCommentVote.voteCount = response.voteCount;
					        		
		   		        		    scope.resetPostErrors();//Reset if any errors are already displayed on screen.
				                
				        	},function(error){
				        		    
				        		    scope.postServerError = "Sorry.We were unable to upvote reply due to some issue. Please try again after sometime.";
				        		    scope.showPostServerError = true;				        		    
				        	});
				        	
				        };
        

				    scope.expandReplies = function (index,showrepliesStatus) {
				        	
				        	if(showrepliesStatus == true){
				        		 for(var i=0; i < scope.data.comments.length ; i++){
				        				if(i != index){
				        					  scope.data.comments[i].showcomments = false;
				        					  scope.data.comments[i].showcommentsreplies = false;
				        				}
				        				else{
				        					  scope.data.comments[i].showcomments = true;
				        					  scope.data.comments[i].showcommentsreplies = true;
				        				}
				        		}
				        	}
				        	else if(showrepliesStatus == false){ //this is when you are clicking the replies button again to minimize the contents.
				        		for(var i=0; i < scope.data.comments.length; i++){
				        					  scope.data.comments[i].showcomments = true;
				        					  scope.data.comments[i].showcommentsreplies = false;
				        		}
				        	}
				      };

			     scope.resetCommentsSection = function(){
			        	//console.log("##inside post.js -- resetCommentsSection method");
			    		for(var i=0; i < scope.data.comments.length ; i++){
							  scope.data.comments[i].showcomments = true;
							  scope.data.comments[i].showcommentsreplies = false;
							  scope.data.comments[i].showreplies = false;
			    		}
			    		scope.newComment='';
			      };


		        scope.checkOwnerOfThePost = function(postOwnerId){

		        	if(scope.$root.userData.loggedInUserId === postOwnerId){
		        		return true;
		        	}
		        	else {
		        		return false;
		        	}
		        };


		        scope.checkWhetherAFollower = function(postCreatedById){

		        	//Check whether this postCreatedById is present in the list of followers?
		        	
		        	if(scope.$root.userData.followers.length > 0 ){
			        	
			        	for(var i=0;i<scope.$root.userData.followers.length;i++){
			        		
			        		if(scope.$root.userData.followers[i].following === postCreatedById){
			        			scope.loggedInUserFollowing = true;
			        			return true;
			        		}
			        	}
			        	scope.loggedInUserFollowing = false;
			        	return false;
		        	}
		        };

		        scope.checkForSaved = function(postid){
		        	
		        	for(var i=0;i<scope.fulldata.posts.length;i++){
		        		
		        		if(scope.fulldata.posts[i].savedPostByUser.length !== 0)
		        			{
		        			   for(var j=0;j<scope.fulldata.posts[i].savedPostByUser.length;j++){
		        				   
				        		  if(scope.fulldata.posts[i].savedPostByUser[j].userId === scope.$root.userData.loggedInId &&
				        				  scope.fulldata.posts[i].savedPostByUser[j].postId === postid)
				        			{
				        			  	scope.data.postSavedbythisUser = true;
				        			  	return;
				        			}
					        		else
					        		{
					        			//console.log("no this user :"+scope.fulldata.posts[i].savedPostByUser[j].userId+" has not saved your post..");
					        		}
									
		        			   }

		        			}
		        		else{
		        			//console.log("This post doesnt have any users who have saved this.");
		        		}

		        		}
		        	scope.data.postSavedbythisUser = false;
		        }
		        
		        
		        scope.resetCommentsSectionOnComment = function(){
		    		for(var i=0; i < scope.data.comments.length ; i++){
						  scope.data.comments[i].showcomments = true;
						  scope.data.comments[i].showcommentsreplies = false;
						  scope.data.comments[i].showreplies = false;
		    		}
		    		scope.newComment='';
		        };

		        
		        scope.refreshCommentsSection = function(postId){
		        	postService.fetchFreshComments(postId).then(function(response){
		        		scope.data.comments = response;
		        		scope.resetCommentsSectionOnComment();
		        		
		        		scope.resetPostErrors();//Reset if any errors are already displayed on screen.
		        		    
		        	},function(error){
	        		    scope.postServerError = "Sorry.There was some issue updating your comments section. Please try again after sometime.";
	        		    scope.showPostServerError = true;
		        	});
		        };

		        scope.refreshCommentsReplySection = function(postId){
		        	postService.fetchFreshComments(postId).then(function(response){
		        		scope.data.comments = response;
		        		
		        		scope.resetPostErrors();//Reset if any errors are already displayed on screen.
		        		    
		        	},function(error){
	        		    scope.postServerError = "Sorry.There was some issue updating your comments section. Please try again after sometime.";
	        		    scope.showPostServerError = true;	
		        	});
		        };		        

		        scope.resetRepliesSectionOnReply = function(commentId,commentIndex){
		            	for(var i=0; i < scope.data.comments.length ; i++){
		            		//Make sure you hide all the comments while being replied for a particular comment.
		  				  scope.data.comments[i].showcomments = false;
		  				  scope.data.comments[i].showcommentsreplies = false;
		  				  scope.data.comments[i].showreplies = false;
		                       if(i === commentIndex)//Check which particular comment is being replied and don't hide it, display it.
		                       {
		           				  scope.data.comments[i].showcomments = true;
		          				  scope.data.comments[i].showcommentsreplies = true;
		          				  scope.data.comments[i].showreplies = true;
		                       }
		            	}
		    		scope.newComment='';
		        };

      	 }//end of LINK Function!
      };//END of RETURN
  }]);
