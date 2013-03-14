if (efn == null) {
	var efn = {}
};
if (efn.util == null) {
	efn.util = {};
};

(function() {


efn.util.ajaxPage = function(params, page, callback) {
	if (params == null) {
		params = {};
	}
	params.page = page;
	callback(params);
};


})();