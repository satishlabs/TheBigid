'use strict';

/**
 * @ngdoc directive
 * @name tbidApp.directive:createPost
 * @description
 * # createPost logics
 */
angular.module('tbidApp')
    .directive('createPost', ['ID_TYPES','ALL_TYPES','ID_CATEGORIES','errorMessages', function(ID_TYPES,ALL_TYPES,ID_CATEGORIES,errorMessages){

        var controller = ['$scope','createPostService','$route','$rootScope','$log','$window', function($scope,createPostService,$route,$rootScope,$log,$window) {

            $scope.types = ID_TYPES;
            $scope.allTypes = ALL_TYPES;
            $scope.categoryListPostLabel = ID_CATEGORIES;

            $scope.generalIdType = [{
                    name: "General",
                    type: "GENERAL",
                    icon: "glyphicon-info-sign"
                }];

            $scope.idType = {
                name: "General",
                type: "GENERAL",
                icon: "glyphicon-info-sign"
            };
            $scope.draft = {};
            $scope.flag = "";

            $scope.isEditingContent = true;
            $scope.isEditingMeta = false;

            $scope.updateIdType = function(idType) {
                $scope.idType = idType;
            };

            $scope.isIdType = function(idType) {
                return $scope.idType.type == idType;
            };

            $scope.hideCreation = function() {
                $scope.isEditingContent = true;
                $scope.isEditingMeta = false;
                $scope.toggle();
            }

            $scope.editPostMeta = function() {
                    $scope.isEditingContent = false;
                    $scope.isEditingMeta = true;
            };

            $scope.goBackToEditingContent = function(){
                $scope.isEditingContent = true;
                $scope.isEditingMeta = false;
            };

            $scope.setPostOption = function(postOption) {
                switch(postOption) {
                    case 'nsfw':
                        $scope.draft.postOption = "nsfw";
                        $scope.clearToFrom();
                        break;
                    case 'tofrom':
                        $scope.draft.postOption = 'tofrom';
                        break;
                    case 'anon':
                        $scope.draft.postOption = 'anon';
                        $scope.clearToFrom();
                        break;
                    default:
                        $scope.draft.postOption = '';
                        $scope.clearToFrom();
                }
            };

            $scope.toggleNsfw = function() {
                $scope.draft.isNsfw = !$scope.draft.isNsfw;
            };

            $scope.toggleAnon = function() {
                $scope.draft.isAnon = !$scope.draft.isAnon;
            }

            $scope.toggleToFrom = function() {
                $scope.draft.isToFrom = !$scope.draft.isToFrom;
            }

            $scope.clearToFrom = function() {
                $scope.draft.to = "";
                $scope.draft.from = "";
            }


            $scope.resetAllPreviousCreatePostFields = function(){
            	
            	
            	/*$scope.draft.title="";
            	$scope.draft.body="";
            	$scope.draft.label="";
            	$scope.draft.to="";
            	$scope.draft.from="";
            	$scope.draft.when="";
            	$scope.draft.categoryUserInput="";*/
            	$scope.draft = {};
            	$scope.draft.category=$scope.categoryListPostLabel[0];
            	//Set back to General on re-entering Create Post - not working properly           	
            	$scope.draft.isNsfw=false;
            	$scope.draft.isToFrom=false;       
            	$scope.draft.isAnon=false;

   			 	$scope.errorDropdownMessage = "";
   			 	$scope.error = false;
   			 	$scope.whenNotSet = false;
   			    $scope.whenImageNotSet = false;
   			    
   			    $scope.mandateCheckForGeneralPostFlag = false;

				$scope.errorPostToFrom = false;
				$scope.errorMessagePostToFromLabel = "";
            };

            $scope.packagingDataToBeSentToServer = function(){

            	var dataPack = {};

            	if($scope.idType.name == "General"){
            		dataPack.title = $scope.draft.title;
            		dataPack.body = $scope.draft.body;
            		dataPack.imgPath = $rootScope.createPostGeneralPicture;
            	}
            	else if($scope.idType.name == "Event"){
            		dataPack.title = $scope.draft.title;
            		dataPack.body=$scope.draft.body;
            		dataPack.imgPath = $rootScope.createPostEventPicture;
            		dataPack.eventDate=$scope.draft.when;
            	}
            	else if($scope.idType.name == "Visual"){
            		dataPack.title = $scope.draft.title;
            		dataPack.body=$scope.draft.body;//caption
            		dataPack.imgPath = $rootScope.dragUploadedFileRead;
            		dataPack.imgType = $rootScope.dragUploadedFileType;
            	}
            	else if($scope.idType.name == "Question"){
            		dataPack.title=$scope.draft.title;
            		dataPack.body=$scope.draft.body;
            		dataPack.imgPath = $rootScope.createPostQuestionPicture;
            		//dataPack.imgType = $rootScope.createPostQuestionPictureType;
            	}
            	else if($scope.idType.name == "Link"){
            		dataPack.title=$scope.draft.title;
            		dataPack.body=$scope.draft.body;
            	}


            	if($scope.draft.isNsfw === true){
            		dataPack.isNSWF = $scope.draft.isNsfw;
            	}
            	else{
            		dataPack.isNSWF = $scope.draft.isNsfw;
            	}

            	if($scope.draft.isToFrom === true){
            		//dataPack.postIsToFrom = $scope.draft.isToFrom;
            		dataPack.toField = $scope.draft.to;
            		dataPack.fromField = $scope.draft.from;
            	}
            	else{
            		dataPack.toField = '';
            		dataPack.fromField = '';
            	}

            	if($scope.draft.isAnon == true){
            		dataPack.isAnonymous = $scope.draft.isAnon;
            	}
            	else{
            		dataPack.isAnonymous = $scope.draft.isAnon;
            	}

            	dataPack.postType = $scope.idType.name;
            	dataPack.postCategory = $scope.draft.category;
            	dataPack.postUserInputCategory = '';
            	
            	if($scope.draft.category === "____"){
            		$scope.draft.userInputCategory=$scope.draft.categoryUserInput;
            		dataPack.postUserInputCategory = $scope.draft.userInputCategory;
            	}

            	dataPack.tags = $scope.draft.label;

            	dataPack.location = $window.locationGlobalVariable;
             	dataPack.longitude = $window.longitudeGlobalVariable;
             	dataPack.latitude = $window.latitudeGlobalVariable;
             	dataPack.country = $window.locationCountryGlobalVariable;


             	if($scope.draft.location === undefined || $scope.draft.location === ''){
             		dataPack.location = $rootScope.userData.location;
                 	dataPack.longitude = $rootScope.userData.longitude;
                 	dataPack.latitude = $rootScope.userData.latitude;
                 	dataPack.country = $rootScope.userData.country;
             	}

             	/*values to be updated - since user is creating a new post  - START */
             	dataPack.expand = false;
             	dataPack.followUser = false;
             	dataPack.enableNotificationFromUser = false;
             	dataPack.postCreatedTime = new Date();
             	dataPack.username = $rootScope.userData.loggedInUserName;
             	dataPack.userId = $rootScope.userData.loggedInUserId;
             	/*values to be updated - since user is creating a new post  - END */
             	
            	return dataPack;
            	
            	
            };


            $scope.submitPost = function() {
            	$rootScope.$broadcast("showLoader","intiatingBackendCall");
            	var dataObject = {};
            	dataObject = $scope.packagingDataToBeSentToServer();

               	createPostService.submitPost(dataObject).then(function(data){
            		 $route.reload();//reload/make a call to the main page again.. post - updating at server side
            		 $rootScope.$broadcast("hideLoader","BackendCallDone");
            	}, function(error){
            		   
            	       $route.reload();
            	       
            	       var errorMessage = "Sorry, We are having trouble posting Id to your TheBigId wall, please try again later.";
            		   $rootScope.$broadcast("showErrorMessageOnMainScreen",errorMessage);
            		   $rootScope.$broadcast("hideLoader","BackendCallDone");
            	});
            };


            $scope.checkMandatePostlabelScreen = function(){


            	/*Just make initializes - to reset the error condition*/
            	 $scope.errorPostTo = false;
        		 $scope.errorPostFrom = false;
        		 $scope.error = false;
        		 $scope.errorPostToFrom = false;
        		 $scope.errorMessagePostToFromLabel = '';

            	if(($scope.draft.category === "____" || $scope.draft.category === "") &&
            	   ($scope.draft.categoryUserInput === "" || $scope.draft.categoryUserInput === undefined)   ){
 
            		if($scope.draft.category=== ""){
            			 $scope.errorDropdownMessage = "Kindly select a valid category";
            			 $scope.error = true;
            		}
            		else{
            			 $scope.errorMessage = "Kindly mention a valid category";
            			 $scope.error = true;
            		}

            		if($scope.draft.isToFrom){

            			if($scope.draft.to === '' ||  $scope.draft.to === undefined){
               			 $scope.errorMessageToPostLabel = "Mention To";
            			// $scope.errorPostTo = true;
            			}

            			if($scope.draft.from === '' ||  $scope.draft.from === undefined){
               			 $scope.errorMessageFromPostLabel = "Mention From";
            			// $scope.errorPostFrom = true;
            			}


            			if( ($scope.draft.to === '' ||  $scope.draft.to === undefined)	&&
            				($scope.draft.from === '' ||  $scope.draft.from === undefined)
            			)
            			{
            				$scope.errorPostToFrom = true;
            				$scope.errorMessagePostToFromLabel = "Mention To or From";
            			}
            			else if( ($scope.draft.to !== '' ||  $scope.draft.to !== undefined)	||
                 				 ($scope.draft.from !== '' ||  $scope.draft.from !== undefined)
            			)
            			{
            				$scope.errorPostToFrom = false;
            				$scope.errorMessagePostToFromLabel = "";
            			}

            		}
            		else{
            			$scope.errorPostToFrom = false;
            			$scope.errorMessagePostToFromLabel = "";
            		}

            	}
            	else{

            		if($scope.draft.isToFrom){
            			if($scope.draft.to === '' ||  $scope.draft.to === undefined){
               			 $scope.errorMessageToPostLabel = "Mention To";
            			 //$scope.errorPostTo = true;
            			}
            			if($scope.draft.from === '' ||  $scope.draft.from === undefined){
               			 $scope.errorMessageFromPostLabel = "Mention From";
            			 //$scope.errorPostFrom = true;
            			}

            			if( ($scope.draft.to === '' ||  $scope.draft.to === undefined)	&&
                			($scope.draft.from === '' ||  $scope.draft.from === undefined)
                		   )
            			   {
                				$scope.errorPostToFrom = true;
                				$scope.errorMessagePostToFromLabel = "Mention To or From";
                		   }
            			   else if(($scope.draft.to !== '' ||  $scope.draft.to !== undefined)	||
                 				   ($scope.draft.from !== '' ||  $scope.draft.from !== undefined)
                    			  )
            			    {
            				   $scope.errorPostToFrom = false;
            				   $scope.errorMessagePostToFromLabel = "";
            			    }

            		}
            		else{
            			$scope.errorPostToFrom = false;
            			$scope.errorMessagePostToFromLabel = "";
            		}

            		if( ($scope.errorPostTo === false && $scope.errorPostFrom === false) &&  $scope.errorPostToFrom === false){
            			$scope.submitPost();// Proceed for server call
                        $scope.errorMessage = "";
                        $scope.errorDropdownMessage = "";
                        $scope.error = false;
            		}

            	}
            }

            $scope.checkWhen = function(){
            	//console.log("##inside create-post.directive.js -- Date was selected!!");
            	$scope.whenNotSet = false;
            };

            $scope.checkMandateFields = function(typeInfo){

				$scope.errorPostToFrom = false;
				$scope.errorMessagePostToFromLabel = "";


            	if(typeInfo == 'GENERAL'){
            		//console.log("##inside createPosts.js -- inside GENERAL loop!");
            	//	if($scope.draft.title === undefined || $scope.draft.title === '' ||  $scope.draft.body === '' ||  $scope.draft.body === undefined ||
            	//	 ($scope.draft.isToFrom === true && ($scope.draft.to === '' || $scope.draft.to === undefined || $scope.draft.from === '' || $scope.draft.from === undefined)))

            		//Check whether
            		//1. the TITLE or BODY is entered
            		//and
            		//2. Whether ToFrom option is selected by user; if yes check Whether To or From field is entered.
            	  if(
            		 ($scope.draft.title === undefined || $scope.draft.title === '') &&  ($scope.draft.body === '' ||  $scope.draft.body === undefined	)
            		 ||
            		 $scope.draft.isToFrom === true && (($scope.draft.to === '' || $scope.draft.to === undefined) && ($scope.draft.from === '' || $scope.draft.from === undefined))
             	    )
            		{
            		        //check whether the Title and Body fields are not entered
            		  		if(($scope.draft.title === undefined || $scope.draft.title === '') &&  ($scope.draft.body === '' ||  $scope.draft.body === undefined))
            		  		{
            		  			$scope.mandateCheckForGeneralPostFlag = true;
            		  		}
            		  		else
            		  		{
            		  			$scope.mandateCheckForGeneralPostFlag = false;
            		  		}

            		  		//check whether the To and From fields are not entered
            		  		if($scope.draft.isToFrom === true && (($scope.draft.to === '' || $scope.draft.to === undefined) && ($scope.draft.from === '' || $scope.draft.from === undefined)))
            		  		{
            		  			$scope.errorPostToFrom = true;
            		  			$scope.errorMessagePostToFromLabel = "Mention To or From";
            		  		}
            		  		else
            		  		{
            		  			$scope.errorPostToFrom = false;
            		  			$scope.errorMessagePostToFromLabel = "";
            		  		}
            		  		//display errors - stop the load of next screen
            		  		$scope.$broadcast ('showErrorUnderCreatePostInputs');
            		}
            		else
            		{
            				$scope.mandateCheckForGeneralPostFlag = false;

        		  			$scope.errorPostToFrom = false;
        		  			$scope.errorMessagePostToFromLabel = "";

            				$scope.editPostMeta();//proceed for next screen
            		}
            	}
            	else if(typeInfo == 'EVENT'){
            		//console.log("##inside createPosts.js -- inside EVENT loop!");
            	//	if($scope.draft.title === undefined || $scope.draft.title === '' || $scope.draft.description == undefined || $scope.draft.description === '' || $scope.draft.when === '' || $scope.draft.when === undefined ||
                //     ($scope.draft.isToFrom === true && ($scope.draft.to === '' || $scope.draft.to === undefined || $scope.draft.from === '' || $scope.draft.from === undefined)))

            		if(
            		   ($scope.draft.when === '' || $scope.draft.when === undefined || $scope.draft.when === null)
            		    ||
            		   ($scope.draft.isToFrom === true && (($scope.draft.to === '' || $scope.draft.to === undefined) && ($scope.draft.from === '' || $scope.draft.from === undefined)))
            		  )
            		{
            			if($scope.draft.when === undefined || $scope.draft.when === null || $scope.draft.when === ''){
            				$scope.whenNotSet = true;
            			}

        		  		//check whether the To and From fields are not entered
        		  		if($scope.draft.isToFrom === true && (($scope.draft.to === '' || $scope.draft.to === undefined) && ($scope.draft.from === '' || $scope.draft.from === undefined)))
        		  		{
        		  			$scope.errorPostToFrom = true;
        		  			$scope.errorMessagePostToFromLabel = "Mention To or From";
        		  		}
        		  		else
        		  		{
        		  			$scope.errorPostToFrom = false;
        		  			$scope.errorMessagePostToFromLabel = "";
        		  		}
        		  		$scope.$broadcast ('showErrorUnderCreatePostInputs');
            		}
            		else{
            			$scope.editPostMeta();//proceed for next screen
            			$scope.whenNotSet = false;
            		}
            	}
            	else if(typeInfo == 'VISUAL'){

        //          if($scope.draft.title === undefined || $scope.draft.title === '' || $scope.draft.thoughts === undefined ||  $scope.draft.thoughts === '' ||
        //    		$rootScope.dragUploadedFileRead === undefined || $rootScope.dragUploadedFileRead === '' ||
        //    	    ($scope.draft.isToFrom === true && ($scope.draft.to === '' || $scope.draft.to === undefined || $scope.draft.from === '' || $scope.draft.from === undefined)))

            		if(
            			($rootScope.dragUploadedFileRead === undefined || $rootScope.dragUploadedFileRead === '')
            			||
            			($scope.draft.isToFrom === true && (($scope.draft.to === '' || $scope.draft.to === undefined) && ($scope.draft.from === '' || $scope.draft.from === undefined)))
            				)
            		{
            			if($rootScope.dragUploadedFileRead === undefined || $rootScope.dragUploadedFileRead === ''){
            				$scope.whenImageNotSet = true;
            			}
            			//console.log("$scope.whenImageNotSet :"+$scope.whenImageNotSet);

        		  		//check whether the To and From fields are not entered
        		  		if($scope.draft.isToFrom === true && (($scope.draft.to === '' || $scope.draft.to === undefined) && ($scope.draft.from === '' || $scope.draft.from === undefined)))
        		  		{
        		  			$scope.errorPostToFrom = true;
        		  			$scope.errorMessagePostToFromLabel = "Mention To or From";
        		  		}
        		  		else
        		  		{
        		  			$scope.errorPostToFrom = false;
        		  			$scope.errorMessagePostToFromLabel = "";
        		  		}

        		  		$scope.$broadcast ('showErrorUnderCreatePostInputs');
            		}
            		else
            		{
            			$scope.whenImageNotSet = false;
            			$scope.editPostMeta();//proceed for next screen
            		}
            	}
            	else if(typeInfo == 'QUESTION'){

            	   //if($scope.draft.question === undefined || $scope.draft.question === '' ||  $scope.draft.thoughts === '' ||  $scope.draft.thoughts === undefined ||
                   //		 ($scope.draft.isToFrom === true && ($scope.draft.to === '' || $scope.draft.to === undefined || $scope.draft.from === '' || $scope.draft.from === undefined)))

            		if(
            			($scope.draft.title === undefined || $scope.draft.title === '')
            			||
            			($scope.draft.isToFrom === true && (($scope.draft.to === '' || $scope.draft.to === undefined) && ($scope.draft.from === '' || $scope.draft.from === undefined)))
            		  )
            		{
        		  		//check whether the To and From fields are not entered
        		  		if($scope.draft.isToFrom === true && (($scope.draft.to === '' || $scope.draft.to === undefined) && ($scope.draft.from === '' || $scope.draft.from === undefined)))
        		  		{
        		  			$scope.errorPostToFrom = true;
        		  			$scope.errorMessagePostToFromLabel = "Mention To or From";
        		  		}
        		  		else
        		  		{
        		  			$scope.errorPostToFrom = false;
        		  			$scope.errorMessagePostToFromLabel = "";
        		  		}
            			//dont do anything - just display errors
            			$scope.$broadcast ('showErrorUnderCreatePostInputs');
            		}
            		else
            		{
            			$scope.editPostMeta();//proceed for next screen
            		}
            	}
            	else if(typeInfo == 'LINK'){

            	//	if($scope.draft.link === undefined || $scope.draft.link === '' ||  $scope.draft.description === '' ||  $scope.draft.description === undefined ||
                //   		 ($scope.draft.isToFrom === true && ($scope.draft.to === '' || $scope.draft.to === undefined || $scope.draft.from === '' || $scope.draft.from === undefined)))
            		if(($scope.draft.title === undefined || $scope.draft.title === '')
            		    ||
            			($scope.draft.isToFrom === true && (($scope.draft.to === '' || $scope.draft.to === undefined) && ($scope.draft.from === '' || $scope.draft.from === undefined)))
            		   )
            		{
        		  		//check whether the To and From fields are not entered
        		  		if($scope.draft.isToFrom === true && (($scope.draft.to === '' || $scope.draft.to === undefined) && ($scope.draft.from === '' || $scope.draft.from === undefined)))
        		  		{
        		  			$scope.errorPostToFrom = true;
        		  			$scope.errorMessagePostToFromLabel = "Mention To or From";
        		  		}
        		  		else
        		  		{
        		  			$scope.errorPostToFrom = false;
        		  			$scope.errorMessagePostToFromLabel = "";
        		  		}

            			//dont do anything - just display errors
            			$scope.$broadcast ('showErrorUnderCreatePostInputs');
            		}
            		else
            		{
            			$scope.editPostMeta();//proceed for next screen
            		}
            	}
            };

        /*Datepicker related functions - START */
		var currentDate = new Date();

		$scope.dateOptions = {
					formatYear: 'yy',
					maxDate: new Date(),
					initDate:new Date ( currentDate.getFullYear() - 18, currentDate.getMonth(), currentDate.getDate() ),
					startingDay: 1
		};
		$scope.popup = {
				opened:false
		}

		$scope.open = function(){
			$scope.popup.opened = true;
		}

        /*Datepicker related functions - END */

		$scope.clearOptions = function(){
			$scope.draft.isToFrom = false;
			$scope.draft.to = "";
			$scope.draft.from = "";
			$scope.draft.isNsfw = false;
			$scope.draft.isAnon = false;
			
			$scope.draft.title = "";
			$scope.draft.body = "";
			
			$scope.whenImageNotSet = false;
			
			$rootScope.dragUploadedFileRead = undefined;//today
		};


        }];

        return {
            templateUrl: 'views/templates/createPost.html',
            restrict: 'E',
            replace: true,
            controller: controller,
            scope: {
                toggle: '&'
            },
            link: function postLink(scope, element, attrs) {
                scope.draft.isNsfw = false;
                scope.draft.isAnon = false;
                scope.draft.isToFrom = false;

            }
        };
    }]);
