'use strict';

/**
 * @ngdoc service
 * @name tbidApp.login
 * @description
 * # login
 * Service in the tbidApp.
 */
angular
  .module('tbidApp')
  .factory('loginUrlService', LoginUrlService);


LoginUrlService.$inject = ['urlService'];

function LoginUrlService(urlService) {

  return {
    getLoginUrl : function () {
      return urlService.getSecurePath() + "/login";
    },

    getLogoutUrl : function(userId) {
      return urlService.getSecurePath() + "/user/logout/"+userId;
    },

    getcheckLoggedStatusUrl : function(){
      return urlService.getSecurePath() + "/user/followers"
    }
  }
}






