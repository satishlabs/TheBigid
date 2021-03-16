
angular.module('tbidApp')
.controller("myModalController", ["$scope","modalService",function($scope,modalService){
	$scope.reportedItem='';
	$scope.reportItemModel = 'NSFW';

	$scope.callUpdateToModerator = function(reportItemValue){
		$scope.reportedItem = reportItemValue;
	};
}]);

angular
	.module("tbidApp")
	.factory("modalService", ["$uibModal", function ($uibModal) {
		function show(customModalDefaults,customModalOptions){
	
			var itemsReport = ["NSFW","ABUSIVE"];
            var modalDefaults = {
                    backdrop: true,
                    keyboard: true,
                    modalFade: true,
                    templateUrl: 'views/templates/modal.html'
                };

                var modalOptions = {
                    closeButtonText: 'Close',
                    actionButtonText: 'OK',
                    headerText: 'Proceed?',
                    bodyText: 'Perform this action?',
                    modalCustomOptions : ["NSFW","Abusive","Hateful","Profane"]
                };
            var tempModalDefaults = {};
            var tempModalOptions = {};
            //Map angular-ui modal custom defaults to modal defaults defined in service
            angular.extend(tempModalDefaults, modalDefaults, customModalDefaults);

            //Map modal.html $scope custom properties to defaults defined in service
            angular.extend(tempModalOptions, modalOptions, customModalOptions);
           
           

                      
            if (!tempModalDefaults.controller) {
            	
                tempModalDefaults.controller = ["$scope", "$uibModalInstance",function ($scope, $uibModalInstance) {
                	
                    $scope.modalOptions = tempModalOptions;
                    $scope.modalOptions.ok = function (result) {
                    	
                    	$uibModalInstance.close(result);
                    };
                    $scope.modalOptions.close = function (result) {
                    	
                    	$uibModalInstance.dismiss('cancel');
                    };
                }];
            }
            

            return $uibModal.open(tempModalDefaults).result;
		}

     return {
	    showModal : function (customModalDefaults, customModalOptions) {
	    	  
	            if (!customModalDefaults) customModalDefaults = {};
	            customModalDefaults.backdrop = 'static';
	            return show(customModalDefaults, customModalOptions);
	     }
	     
     }
    }]);
