'use strict';

angular.module('tbidApp')
    .directive('categoryFilter', [ 'ID_CATEGORIES', function(ID_CATEGORIES) {
        var controller = ['$scope','$rootScope', function($scope,$rootScope) {
            $scope.categoryList = ID_CATEGORIES;

            $scope.checkSelected = function(category) {
                return $scope.categoriesChosen.indexOf(category) != -1;
            };

            $scope.toggleSelection = function(category) {

                var indexOfSelectedCat = $scope.categoriesChosen.indexOf(category);

                if(indexOfSelectedCat>-1) {
                    $scope.categoriesChosen.splice(indexOfSelectedCat, 1);
                } else {
                    $scope.categoriesChosen.push(category);
                }
            }
        }];

        return {
            restrict: 'E',
            scope: {
                categoriesChosen: "="
            },
            controller: controller,
            templateUrl: 'views/templates/categoryFilter.html',
            replace: true
        };
    }]);
