
function RegisterService($q,ajaxService,registerUrlService,base64Service) {
  return {
        doRegister : function(data){
            var deferred = $q.defer();
            var url = registerUrlService.getRegisterationUrl();
            
            /*Uncomment me to encrypt the value - START*/
            //data.password = base64Service.doEncryption(data.password);
            //data.passwordConfirm = base64Service.doEncryption(data.passwordConfirm);
            /*Uncomment me to encrypt the value - END*/
            
            var regRequest = buildRegistrationRequest(data);
            ajaxService.doPost(url,{},regRequest).then(
              function (response) {
                deferred.resolve(response);
              },
              function (rejectError) {
                deferred.reject(rejectError)
              }
            );
            return deferred.promise;
        }
  };

  function buildRegistrationRequest(data){
   var request = {
      username:data.username,
      password:data.password,
      passwordConfirm:data.passwordConfirm,
      email:data.email,
      dateOfBirth : data.dob,
      avatarImgPath : data.profilePicture,
      location: data.fullAddress,
      latitude : data.userlatitude,
      longitude: data.userlongitude,
      country: data.country
    };
   return request;
  }
}


angular
  .module('tbidApp')
  .factory('registerService', RegisterService);

RegisterService.$inject = ['$q','ajaxService','registerUrlService','base64Service'];
