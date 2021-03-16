function changePasswordUrlService(urlService) {
	return{
	      getchangePasswordUrl : function(pwdDetails){
	          return urlService.getSecurePath() + "/user/changepassword/"+pwdDetails.userId+"/"+pwdDetails.oldpassword+"/"+pwdDetails.newpassword;
	        }
	}
};

angular
.module('tbidApp')
.service('changePasswordUrlService', changePasswordUrlService);
