'use strict';

/**
 * @ngdoc service
 * @name tbidApp.validation
 * @description
 * # validation
 * Service in the tbidApp.
 */
angular.module('tbidApp')
  .service('validation', function () {
    // AngularJS will instantiate a singleton by calling "new" on this function
    var validationObject = {
      login_userName:{
        minLength: -1,
        maxLength: -1,
        required: true
      },
      login_password:{
        minLength: -1,
        maxLength: -1,
        required: true
      },
      userName:{
        minLength: 2,
        maxLength: 31,
        required: true
      },
      password:{
        minLength: 2,
        maxLength: 31,
        required: true
      },
      email:{
        minLength: -1,
        maxLength: 50,
        pattern:/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
        required: true
      },
      compare:{
        minLength: -1,
        maxLength: -1,
        required:true,
        compare:true
      },
      postTitle:{
          minLength: -1,
          maxLength: -1,
          required: true
      },
      draftBody:{
          minLength: -1,
          maxLength: -1,
          required: true
      },
      draftDescription:{
          minLength: -1,
          maxLength: -1,
          required: true
      },
      draftThoughts:{
          minLength: -1,
          maxLength: -1,
          required: true
      },
   /*   questionDraftThoughts:{
          minLength: -1,
          maxLength: -1,
          required: true
      },*/
      question:{
          minLength: -1,
          maxLength: -1,
          required: true
      },
      link:{
          minLength: -1,
          maxLength: -1,
          required: true
      },
   /*   linkDescription:{
          minLength: -1,
          maxLength: -1,
          required: true
      },
      VisualCaption:{
          minLength: -1,
          maxLength: -1,
          required: true
      },*/
      newcategory:{
          minLength: -1,
          maxLength: -1,
          required: true
      },
/*      createPostto:{
          minLength: -1,
          maxLength: -1,
          required: true
      },
      createPostfrom:{
          minLength: -1,
          maxLength: -1,
          required: true
      },*/
      old_password:{
          minLength: -1,
          maxLength: -1,
          required: true
      },
      new_password:{
          minLength: -1,
          maxLength: -1,
          required: true 
      },
      reenter_new_password:{
          minLength: -1,
          maxLength: -1,
          required: true 
      }
      
    }

    this.getValidationObject = function(type){
      if(validationObject[type]){
        return validationObject[type];
      }
      return {
          minLength: -1,
          maxLength: -1
      }
    }
  });
