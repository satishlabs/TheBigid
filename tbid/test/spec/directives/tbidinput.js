'use strict';

describe('Directive: tbidInput', function () {

  // load the directive's module
  beforeEach(module('tbidApp'));

  var element,
    scope;

  beforeEach(inject(function ($rootScope) {
    scope = $rootScope.$new();
  }));

  it('should make hidden element visible', inject(function ($compile) {
    element = angular.element('<tbid-input></tbid-input>');
    element = $compile(element)(scope);
    expect(element.text()).toBe('this is the tbidInput directive');
  }));
});
