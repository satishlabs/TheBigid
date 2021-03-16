//The connection properties for this environment
//
//Usually, siteminder will add the sms_user http header for non agent users
//
//For environments without siteminder, if you specify the property
//sms_user, the angular app will put the sms_user http header
//on all its outgoing http connections.

// The useMockData attributes allows mock data file to be used for
// development purposes. This attribute MUST NOT exist in connection
// properties on other environments/platforms.

var connection = {

    "host": "http://localhost:8080",
    "iehost": "http://localhost:8080"
};
