'use strict';
var appFilters = angular.module('appFilters', []);

appFilters.filter('capitalize', function() {
    return function(input) {
      return (!!input) ? input.charAt(0).toUpperCase() + input.substr(1).toLowerCase() : '';
    }
  });


appFilters.filter('truncate', function () {
    return function (text, length, end) {
      if (!text) {
        return;
      }
      if (isNaN(length)) {
        length = 10;
      }

      end = end || "...";
      if (text.length <= length || text.length - end.length <= length) {
        return text;
      } else {
        return String(text).substring(0, length - end.length) + end;
      }
    };
 });

appFilters.filter('truncateToFrom', function () {
    return function (text, length, end) {
    	
      if (!text) {
        return;
      }
      if (isNaN(length)) {
        length = 10;
      }

      end = end || "...";

      if (text.length <= length || text.length - end.length <= length) {
        return text;
      } else {
        return String(text).substring(0, length - end.length) + end;
      }
    };
 });
