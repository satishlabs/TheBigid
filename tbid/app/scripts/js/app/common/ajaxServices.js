/**
 * Created by Amrish Kumar Gupta on 21/07/2017.
 */

/**
 * This ajax service is used for perform all the operation with server
 * it support below http protocol method
 * 1. GET
 * 2. POST
 * 3. PUT
 * 4. DELETE
 *
 */

var appServices = angular
  .module("tbidApp");

appServices.config(['$httpProvider', function ($httpProvider) {
  $httpProvider.interceptors.push('httpErrorInterceptor');
  $httpProvider.defaults.headers.post['Content-Type'] = 'application/json; charset=utf-8';
  $httpProvider.defaults.headers.put['Content-Type'] = 'application/json; charset=utf-8';
}]);

appServices.factory('httpErrorInterceptor', ['$q', 'urlService',
  function ($q, urlService) {

    function getLoginURL() {
      //return the login url
    }

    return {
      'responseError': function (rejection) {
        // Gets executed for all ajax errors.
        // Handle the error and return the error response for bespoke handling in services/controllers
        if (rejection.status === 401 && rejection.data && rejection.data.status === "302") {
          //Take the user to login page
          if (rejection.data.location.indexOf("accessdenied.html") > -1) {
            location.href = rejection.data.location;
          }
          else {
            //Take the user to login page
            window.top.location.href = getLoginURL();
          }
        }
        return $q.reject(rejection);
      }
    };
  }]);

appServices.factory('ajaxService', AjaxService);

AjaxService.$inject = ['$http', '$q'];

function AjaxService($http, $q) {

  var config = {
    timeout: 30000
  };

  function doPerform(method, url, params, postData, def) {
    $http({
      method: method,
      url: url,
      params: params,
      data: postData,
      timeout: config.timeout
    }).then(
      function (resolution) {
        def.resolve(resolution.data);
      },
      function (error) {
        def.reject(error);
      }
    );

  }

  function doLogin(method, url, params, postData, def) {
    $http({
      method: method,
      url: url,
      params: params,
      data: postData,
      headers:{
        "x-requested-with":"XMLHttpRequest"
      }
    }).then(
      function (resolution) {
        def.resolve(resolution.data);
      },
      function (error) {
        def.reject(error);
      }
    );

  }

  return {
    doGet: function (url, params) {
      var deferred = $q.defer();
      doPerform("GET", url, params, null, deferred);
      return deferred.promise;
    },
    doDelete: function (url, params) {
      var deferred = $q.defer();
      doPerform("DELETE", url, params, null, deferred);
      return deferred.promise;
    },
    doPost: function (url, params, data) {
      var deferred = $q.defer();
      doPerform("POST", url, params, data, deferred);
      return deferred.promise;
    },
    doLoginPost: function (url, params, data) {
      var deferred = $q.defer();
      doLogin("POST", url, params, data, deferred);
      return deferred.promise;
    },
    doPut: function (url, params, data) {
      var deferred = $q.defer();
      doPerform("PUT", url, params, data, deferred);
      return deferred.promise;
    }
  };
};

