    'use strict';

app.factory('House', function($http) {
    
    // var api = "http://112.74.79.166:8888/api/atp/java/v1/tenant";
    var api = "http://127.0.0.1:8888/api/atp/java/v1/tenant"
    return {
        search: function(query,json, page_num, callback) {
            // var uri = api + '/search/' + query;
            // if(page_num == undefined) uri += '/1';
            // else uri += '/' + page_num;
            console.log("service");
            console.log("query1 = " + query)
            if(query==undefined){
                query=null
            }
            if(page_num ==undefined){
                page_num=1
            }
            return $http.post(api + "/search/" + query + "/" + page_num,json).success(callback);
            
        },

        echart : function(callback){
            console.log("echart");
            return $http.get(api + "/echart/getRegionPrice").success(callback);
        }
    };
});
