angular.module('tbidApp')
.directive('userEnterKeyPress', function () {
		return {
			restrict : 'A',
			scope : {
				performSubmit : '&ngClick'
			},
			controller : function($scope){
				 
				$scope.callEnterFunc = function(event){
					 if(event.keyCode === 13){
						 $scope.performSubmit();
					 }
				}
			}
		};
});
