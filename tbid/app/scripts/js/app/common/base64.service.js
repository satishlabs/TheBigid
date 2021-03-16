angular.module("tbidApp")
.factory('base64Service', ['base64',function (base64) {
	
	return {
		
		doEncryption : function(data){			
			return base64.encode(data);
		},
		doDecryption : function(data){
			return base64.decode(data);
		}
		
	};
	
}]);

//bower install angular-utf8-base64