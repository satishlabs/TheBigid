
function preLoginService(){
	 this.loggedInUserName = "";
}

angular.module('tbidApp')
       .service('preLoginService', preLoginService);