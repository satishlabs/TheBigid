function changeAvatarUrlService(urlService) {
	return{
	      getchangeAvatarUrl : function(username){
	          return urlService.getSecurePath() + "/user/changeprofileimage/"+username;
	        }
	}
};

angular
.module('tbidApp')
.service('changeAvatarUrlService', changeAvatarUrlService);
