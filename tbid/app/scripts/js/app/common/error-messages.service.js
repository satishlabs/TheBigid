'use strict';

/**
 * @ngdoc service
 * @name tbidApp.errorMessages
 * @description
 * # errorMessages
 * Service in the tbidApp.
 */
angular.module('tbidApp')
  .service('errorMessages', function () {
    var replaceCharacter = "#*#";
    var replaceRegex = /<([^>]*)>/;
    var errorMessageObject = {
      "minLengthValidation":"The entered value does not have enough characters.",
      "maxLengthValidation":"The entered value has too many characters.",
      "requiredValidation":"This field cannot be left empty.",
      "userName":{
        "minLengthValidation":"Username should be at least 2 characters",
        "maxLengthValidation":"Username can be maximum of 31 characters",
        "requiredValidation":"User name cannot be left empty."
      },
      "password":{
        "minLengthValidation":"Password should be at least 6 characters",
        "maxLengthValidation":"Password can be maximum of 31 characters",
        "requiredValidation":"Password cannot be left empty."
      },
      "email":{
        "patternValidation":"Please ensure that you have entered a valid e-mail id.",
        "requiredValidation":"E-mail cannot be left empty."
      },
      "compare":{
        "compareValidation":"The entered password does not match the original.",
        "requiredValidation":"Password field cannot be left empty."
      },
      "login_userName":{
        "requiredValidation":"User name cannot be left empty."
      },
      "login_password":{
        "requiredValidation":"Password cannot be left empty."
      },
      "postTitle":{
    	  "requiredValidation":"Title needs to be entered."
      },
      "draftBody":{
    	  "requiredValidation":"Body needs to be filled."
      },
      "draftDescription":{
    	  "requiredValidation":"Description needs to be filled."
      },
      "draftThoughts":{
    	  "requiredValidation":"Thoughts need to be filled."
      },
      "question":{
    	  "requiredValidation":"Question needs to be filled."
      },
      "link":{
    	  "requiredValidation":"Link needs to be filled."
      } ,
      "newcategory":{
    	  "requiredValidation":"Mention category name"
      },
      "old_password":{
    	  "requiredValidation":"Old Password needs to be filled."
      },
      "new_password":{
    	  "requiredValidation":"New Password needs to be filled."
      },
      "reenter_new_password":{
          "compareValidation":"The entered password does not match the original.",  	  
    	  "requiredValidation":"Re-enter New Password needs to be filled."
      }
      
    }

    var genericErrorMessage = "Oops, there was some problem"

    this.getErrorMessage = function (errorType, resourceType, messageOptions){
      var errorMessage;
      var errorMessageSet;
      if(errorMessageObject[resourceType]){
        errorMessageSet = errorMessageObject[resourceType]
      }else{
        errorMessageSet = errorMessageObject;
      }
      errorMessage = errorMessageSet[errorType];
      if(!errorMessage){
        return genericErrorMessage;
      }
      if (messageOptions) {
        var match = replaceRegex.exec(errorMessage);
        while (match != null) {
          var param = match[1];
          var replaceValue = messageOptions[param];

          var index = match.index;
          var strToReplace = errorMessage.slice(index, index + match[0].length);
          errorMessage = errorMessage.replace(strToReplace, replaceValue);
          match = replaceRegex.exec(errorMessage);
        }
      }
      /*if(arrParams){
        for (var index = 0; index < arrParams.length; index++ ){
          errorMessage.replace(replaceCharacter, arrParams[index]);
        }
      }*/
      return errorMessage;
    }
  });
