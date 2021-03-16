angular.module('tbidApp').
directive('myUpload', [function () {
            return {
                restrict: 'A',
                link: function (scope, elem, attrs) {
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        scope.data.profilePicture = e.target.result;
                        scope.$apply();
                    }

                    elem.on('change', function() {
                        reader.readAsDataURL(elem[0].files[0]);
                    });
                }
            };
        }])
.directive('myUploadPostlogin', [function () {
            return {
                restrict: 'A',
                link: function (scope, elem, attrs) {
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        scope.profilepicture = e.target.result;
                        scope.$root.changedProfilePicture = e.target.result;
                        scope.$apply();
                    }

                    elem.on('change', function() {
                        reader.readAsDataURL(elem[0].files[0]);
                    });
                }
            };
        }])        
.directive('myUploadPostGeneral', ['$rootScope',function ($rootScope) {
    return {
        restrict: 'EA',
        link: function (scope, elem, attrs,rootScope) {
        	
            var reader = new FileReader();
            reader.onload = function (e) {
            	
                scope.createPostGeneralPicture = e.target.result;
                scope.$root.createPostGeneralPicture =  scope.createPostGeneralPicture;
                scope.$apply();
            }

            elem.on('change', function() {
                reader.readAsDataURL(elem[0].files[0]);
            });
                        
        }
    };
}])
.directive('myUploadPostEvent', [function () {
    return {
        restrict: 'A',
        link: function (scope, elem, attrs) {
        	
            var reader = new FileReader();
            reader.onload = function (e) {
            	
                scope.createPostEventPicture = e.target.result;
                scope.$root.createPostEventPicture =  scope.createPostEventPicture;
                scope.$apply();
            }

            elem.on('change', function() {
                reader.readAsDataURL(elem[0].files[0]);
            });
        }
    };
}])
.directive('myUploadPostQuestion', [function () {
    return {
        restrict: 'A',    
        link: function (scope, elem, attrs) {

            var reader = new FileReader();
            reader.onload = function (e) {
                scope.createPostQuestionPicture = e.target.result;
                scope.$root.createPostQuestionPicture =  scope.createPostQuestionPicture;
                scope.$apply();
            }

            elem.on('change', function() {
                reader.readAsDataURL(elem[0].files[0]);
            });
        }
    };
}]);