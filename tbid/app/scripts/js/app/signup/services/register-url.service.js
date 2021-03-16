


function RegisterUrlService(urlService) {
    return {
      getRegisterationUrl : function(){
        return urlService.getSecurePath() + "/user/signup";
      }

    }
  }


RegisterUrlService.$inject = ['urlService'];


angular
  .module('tbidApp')
  .factory('registerUrlService', RegisterUrlService);


