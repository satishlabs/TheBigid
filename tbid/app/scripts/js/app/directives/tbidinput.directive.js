'use strict';


angular.module('tbidApp')
  .directive('tbidInput', function (validation, errorMessages) {
    return {
      require:"ngModel",
      scope:{
        check:"&",
        compareValue:"=",
        placeholder:"@",
        resourceType:"@",
        resourceName:"@",
        focusMe:'@',
        showtbidtextfield:'@',
        showtbidtextfieldcreatepost:'@',
        showtbidtextfieldcategory :'@',
        showtbidtextarea:'@',
        showtbidtextfieldtofrom : '@',
        showtbidtextfieldemail:'@',
        showtbidtextfieldtitle:'@',
        showrows : '@'
      },
      //template: '<div class="{{errorClass}} tbid-input" ><label>{{placeholder}}</label><br><input class="form-control" placeholder="{{placeholder}}" ng-change="change()" ng-model="value"><div class="error-message" ng-show="error">{{errorMessage}}</div></div>',
      templateUrl: 'views/templates/tbinput.html',
      restrict: 'E',
      link: function postLink(scope, element, attrs, ngModelCtrl) {
        //error ids for various validation errors.
        var minLengthValidation = "minLengthValidation",
            maxLengthValidation = "maxLengthValidation",
            patternValidation = "patternValidation",
            requiredValidation = "requiredValidation",
            compareValidation = "compareValidation";

        var validationObject = validation.getValidationObject(scope.resourceType);
        var isRequired = attrs.isRequired === "true";
        scope.inputType = (attrs.isPasssword==="true")?"password":"text";

        scope.showtbidtextfield = (attrs.showtbidtextfield === 'yes')?true:false;
        scope.showtbidtextarea = (attrs.showtbidtextarea === 'yes')?true:false;
        scope.showtbidtextfieldcreatepost = (attrs.showtbidtextfieldcreatepost === 'yes')?true:false;
        scope.showtbidtextfieldcategory= (attrs.showtbidtextfieldcategory === 'yes')?true:false;
        scope.showtbidtextfieldemail = (attrs.showtbidtextfieldemail === 'yes')?true:false;
        scope.showtbidtextfieldtitle = (attrs.showtbidtextfieldtitle === 'yes')?true:false;
        scope.errorClass = "";

        //$parsers functions would be called after data has been added to this directive from outside.
        ngModelCtrl.$formatters.push(function(modelValue) {
          return modelValue;
        });
        //This would be called after formatters are called successfully
        ngModelCtrl.$render = function() {
            scope.value   = ngModelCtrl.$viewValue;
        };
        //On change set the value properly. after set view value parsers are called.
        scope.change = function(){
          if(!validate()){
          scope.check();
            return;
          }
            scope.errorClass = "";
            scope.error = false;
            scope.errorMessage = "";
            ngModelCtrl.$setViewValue(scope.value);
            scope.check();
        }
        //$parsers functions would be called before sending the data outside the directive
        ngModelCtrl.$parsers.push(function(viewValue){
          return viewValue;
        });

        scope.blurHandler = function(){
        	validateAll();
        	scope.check();
        }


        scope.changeCreatePost = function(typeInfo){

            if(!validateCreatePostInitialScreen()){
                  return ;
                }
                  scope.errorClass = "";
                  scope.error = false;
                  scope.errorMessage = "";
                  ngModelCtrl.$setViewValue(scope.value);
                 // scope.checkMandateFields(typeInfo);
        };

        scope.$on('showErrorUnderCreatePostInputs', function(e) {
                scope.changeCreatePost();
        });

        scope.$on('showErrorInChangePasswordScreen', function(e){
        	 scope.checkChangePasswordFields();
        });

        scope.checkChangePasswordFields = function(){

            if(!validateChangePasswordScreen()){
                //scope.checkMandateFields(typeInfo);
                  return ;
                }
                  scope.errorClass = "";
                  scope.error = false;
                  scope.errorMessage = "";
                  ngModelCtrl.$setViewValue(scope.value);

        };


        if(scope.focusMe === "true"){
          //TODO: hack for focus find a better way!
          $(element).find("input").focus();
        }

        //These are utility functions used by input field
        var showError = function(errorType){
          scope.errorMessage = errorMessages.getErrorMessage(errorType, scope.resourceType);
          scope.errorClass = "input-error";
          scope.error = true;
        }

        //This function validates the
        var validate = function(){
          if(checkForMinLength() && checkForMaxLength()){
            return true;
          }
          return false;
        }

        var validateChangePasswordScreen = function(){
            if(checkForRequired() && checkForCompare()){
              return true;
            }
            return false;
          }

        var validateCreatePostInitialScreen = function(){
            if(checkForRequired()){
              return true;
            }
            return false;
          }

        //This function will jist validate for scenarios while user is typeing.
        var validateAll = function(){
          if(checkForRequired() && checkForCompare() && checkForMinLength() && checkForMaxLength() && checkForPattern()){
            return true;
          }
          return false;
        }

        //function to compare the entered value with another value
        function checkForCompare(){
          if(!validationObject.compare){
            return true;
          }

          if(scope.compareValue === scope.value){
            return true;
          }
          showError(compareValidation);
          ngModelCtrl.$setViewValue("");
          return false;
        }
        //Function to test if the entered value matches the pattern
        function checkForPattern(){
          if(!validationObject.pattern){
            return true;
          }
          if(validationObject.pattern.test(scope.value)){
            return true;
          }
          showError(patternValidation);
          ngModelCtrl.$setViewValue("");
          return false;
        }

        function checkForMinLength(){
          if(!scope.value || validationObject.minLength < 0 || scope.value.length >= validationObject.minLength){
            return true;
          }
          showError(minLengthValidation);
          ngModelCtrl.$setViewValue("");
          return false;
        }

        function checkForMaxLength(){
          if(!scope.value || validationObject.maxLength < 0 || scope.value.length <= validationObject.maxLength){
            return true;
          }
          showError(maxLengthValidation);
          ngModelCtrl.$setViewValue("");
          return false;
        }

        function checkForRequired(){
          if(validationObject.required && !scope.value){
            showError(requiredValidation);
            ngModelCtrl.$setViewValue("");
            return false;
          }
          return true;
        }
      }
    };
  });
