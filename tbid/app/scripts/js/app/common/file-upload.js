angular.module('tbidApp')
.controller('UploadCtrl', ['$scope', function ($scope) {
            $scope.data.profilePicture = "";    
            $scope.createPostGeneralPicture = "";
            $scope.createPostEventPicture="";
            $scope.createPostQuestionPicture="";
}])
.controller('UploadCtrlPostLogin', ['$scope', function ($scope) {
 

    $scope.profilePicture = "";
}]);