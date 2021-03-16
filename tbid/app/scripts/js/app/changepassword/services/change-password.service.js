
function changePasswordService($q,ajaxService,changePasswordUrlService,base64Service) {
	return{
		submitChangePassword : function(pwdDetails){
            var deferred = $q.defer();
            
            /*Uncomment me to encrypt the value - START*/
            //pwdDetails.oldpassword = base64Service.doEncryption(pwdDetails.oldpassword);
            //pwdDetails.newpassword = base64Service.doEncryption(pwdDetails.newpassword);
            //pwdDetails.reenternewpassword = base64Service.doEncryption(pwdDetails.reenternewpassword);
            /*Uncomment me to encrypt the value - END*/
            
            var url = changePasswordUrlService.getchangePasswordUrl(pwdDetails);
           
            ajaxService.doPut(url,{},'').then(
              function (response) {
                deferred.resolve(response);
              },
              function (rejectError) {
                deferred.reject(rejectError)
              }
            );
            return deferred.promise;
        }
	}
};

angular
.module('tbidApp')
.service("changePasswordService", changePasswordService);

changePasswordService.$inject["$q","ajaxService","changePasswordUrlService","base64Service"];
