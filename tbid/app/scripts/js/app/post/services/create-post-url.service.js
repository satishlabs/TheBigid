angular
  .module('tbidApp')
  .factory('createPostUrlService', CreatePostUrlService);


CreatePostUrlService.$inject = ['urlService'];

function CreatePostUrlService(urlService) {

  return {
	submitPostUrl: function() {
      return urlService.getSecurePath()+"/post/publish"
    },
    reportPostUrl: function(reportedInfo) {
        return urlService.getSecurePath()+"/post/"+reportedInfo.postId+"/isnsfw"
      }
  }
}
