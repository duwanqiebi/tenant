'use strict';

app.factory('House', function($http) {
    
    var api = "http://localhost:8888/api/atp/java/v1/tenant/search/111/1";

    return {
        search: function(query, page_num, callback) {
            // var uri = api + '/search/' + query;
            // if(page_num == undefined) uri += '/1';
            // else uri += '/' + page_num;
            console.log("service")
            return $http.get(api).success(callback);
            
        },
    };
});
