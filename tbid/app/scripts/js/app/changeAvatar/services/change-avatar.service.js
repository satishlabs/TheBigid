
function changeAvatarService($q,ajaxService,changeAvatarUrlService) {
	return{
		doChangeAvatar : function(changedProPic,username){
            var deferred = $q.defer();
            var url = changeAvatarUrlService.getchangeAvatarUrl(username);
           
            ajaxService.doPut(url,{},{
            	 avatarImgPath : changedProPic
            }).then(
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
.service("changeAvatarService", changeAvatarService);

changeAvatarService.$inject["$q","ajaxService","changeAvatarUrlService"];
