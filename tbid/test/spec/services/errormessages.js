'use strict';

describe('Service: errorMessages', function () {

  // load the service's module
  beforeEach(module('tbidApp'));

  // instantiate service
  var errorMessages;
  beforeEach(inject(function (_errorMessages_) {
    errorMessages = _errorMessages_;
  }));

  it('should do something', function () {
    expect(!!errorMessages).toBe(true);
  });

});
