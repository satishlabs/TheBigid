var appServices  = angular
                      .module("tbidApp");

appServices.factory('hostService', function () {

  function isXDomain() {
    var e = -1;
    if ("Microsoft Internet Explorer" == navigator.appName) {
      var r = navigator.userAgent,
        t = new RegExp("MSIE ([0-9]{1,}[.0-9]{0,})");
      null != t.exec(r) && (e = parseFloat(RegExp.$1));
    }
    return window.XDomainRequest && e <= 9;
  }

  return {
    getHost: function () {
      if (isXDomain) {
        return connection.iehost;
      }
      return connection.host;
    }
  };

});

appServices.factory('urlService', ['hostService', function (hostService) {

  return {
    getSecurePath: function () {
      return hostService.getHost();
    },

    getPublicPath: function () {
      return hostService.getHost()
    }

  };
}]);
