angular.module('tbidApp')
.controller('imageFileUploadController', ['$scope', function ($scope){
	
  $scope.image = null;
  $scope.imageFileName = '';

  $scope.uploadme = {};
  $scope.uploadme.src = '';
}]);


angular.module('tbidApp')
.directive("fileread", [function ($rootScope) {
    return {
        scope: {
            fileread: "="
        },
        link: function (scope, element, attributes) {
            element.bind("change", function (changeEvent) {
                var reader = new FileReader();
                reader.onload = function (loadEvent) {
                    scope.$apply(function () {
                        scope.fileread = loadEvent.target.result;
                        scope.filereadtype = changeEvent.target.files[0].type;
                        
                        scope.$root.dragUploadedFileRead = scope.fileread;
                        scope.$root.dragUploadedFileType = scope.filereadtype;
                      

                    });
                }
                reader.readAsDataURL(changeEvent.target.files[0]);
            });
        }
    }
}]);
