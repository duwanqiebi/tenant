'use strict';

var globledata ;


app.filter('to_trusted', ['$sce', function($sce){
        return function(text) {
            return $sce.trustAsHtml(text);
        };
    }]);

app.controller('NavbarController', function($scope, $routeParams, $location) {
    if($routeParams.query < 2 | $routeParams > 50) {
        $location.path('/home');
        return;
    }

    $scope.$on('$routeChangeSuccess', function (event, current, previous) {
        $scope.query = current.pathParams.query;
     });

    $scope.go = function(query) {
        $location.path(query);
    };
});

app.controller('HomeController', function($rootScope, $scope, $location) {
    $scope.go = function(query) {
        $location.path(query);
    };

    $rootScope.is_homepage = function() {
        return true;
    }
});

app.controller('EmptyController', function($rootScope, $scope, $location) {
    $scope.go = function(query) {
        $location.path(query);
    };

    $rootScope.is_homepage = function() {
        return true;
    }
});

app.controller('SearchController', function($rootScope, $scope, $location, $routeParams, House) {

    if($routeParams.query < 2 | $routeParams > 50) {
        console.log("1");
        $location.path('/home');
        return;
    }
    
    //区域
    var area = $("#secitem-area").find(".select").html();
    //price
    var price = $("#secitem-rent").find(".select").html();
    //厅室
    var room = $("#secitem-room").find(".select").html();
    //租房类型
    var type = $("#secitem-type").find(".select").html();
    //品牌
    var brand = $("#secitem-brand").find(".select").html();
    //地铁
    var subway = $("#secitem-subway").find(".select").html();
    var space = $("#secitem-space").find(".select").html();

    var json = {};
    json.area=area;
    json.room = room;
    json.type=type;
    json.brand=brand;
    json.subway=subway;
    json.price = price;
    json.space= space;


    House.search($routeParams.query,json, $routeParams.page_num, function(data) {
        // if(!data.page_count)
        //     $location.path('empty');


        $scope.query = $routeParams.query;
        queryParam = $routeParams.query;
        // $scope.page_count = data.page_count;
        $scope.page_count = 10;
        $scope.page_num = $routeParams.page_num;
        pageNum = $routeParams.page_num;
        $scope.page_next = parseInt(pageNum) + 1;
        if(pageNum == 1){
            $scope.page_prev = 1;
        }else{
            $scope.page_prev = parseInt(pageNum) - 1;
        }
        console.log(data);
        globledata = data;
        if(data == ""){
            $scope.houses = null;
        }else{
            $scope.houses = data.houses;
        }



        // $scope.renderFinish = function(){
        //     var arr = $("div[name='house_body']");
        //     for(var i = 0; i <arr.length; i ++){
        //         $("<div id='map" +i+ "' style='height:200px\;width:300px'>1111</div>").appendTo(arr[i]);
        //     }
        //
        //     var index = 0;
        //     for(;index < $scope.houses.length;index ++){
        //         var house =  $scope.houses[index];
        //         var map = new BMap.Map("map" + index);
        //         console.log(house.latitude + " " + house.longitude);
        //         var point = new BMap.Point(house.longitude, house.latitude);
        //         var marker = new BMap.Marker(point);
        //         map.addOverlay(marker);
        //         map.centerAndZoom(point, 11);
        //         map.addControl(new BMap.MapTypeControl());
        //         map.enableScrollWheelZoom(true);
        //     }
        // }

        $scope.detail = function(id,house){
            console.log(id);
            console.log(house);
            sessionStorage.setItem(id,JSON.stringify(house));
            window.open("/detail.html?id=" + id);
        }


    });

    $rootScope.is_homepage = function() {
        return false;
    }
});


app.controller('Search1Controller', function($rootScope, $scope, $location, $routeParams, House) {
    // if($routeParams.query < 2 | $routeParams > 50) {
    //     console.log("1");
    //     $location.path('/home');
    //     return;
    // }

    //区域
    var area = $("#secitem-area").find(".select").html();
    //price
    var price = $("#secitem-rent").find(".select").html();
    //厅室
    var room = $("#secitem-room").find(".select").html();
    //租房类型
    var type = $("#secitem-type").find(".select").html();
    //品牌
    var brand = $("#secitem-brand").find(".select").html();
    //地铁
    var subway = $("#secitem-subway").find(".select").html();
    var space = $("#secitem-space").find(".select").html();

    var json = {};
    json.area=area;
    json.room = room;
    json.type=type;
    json.brand=brand;
    json.subway=subway;
    json.price = price;
    json.space= space;

    House.search($routeParams.query,json, $routeParams.page_num, function(data) {
        console.log($routeParams);
        // if(!data.page_count)
        //     $location.path('empty');
        $scope.query = $routeParams.query;
        // $scope.page_count = data.page_count;
        $scope.page_count = 10;
        $scope.page_num = 1;
        // $scope.page_next = data.page_next;
        // $scope.page_prev = data.page_prev;
        pageNum = $routeParams.page_num;
        $scope.page_next = parseInt(pageNum) + 1;
        if(pageNum == 1){
            $scope.page_prev = 1;
        }else{
            $scope.page_prev = parseInt(pageNum) - 1;
        }
        $scope.houses = data.houses;


        // $scope.renderFinish = function(){
        //     var arr = $("div[name='house_body']");
        //     for(var i = 0; i <arr.length; i ++){
        //         $("<div id='map" +i+ "' style='height:200px\;width:300px'>1111</div>").appendTo(arr[i]);
        //     }
        //
        //     setTimeout(function(){
        //         var index = 0;
        //         for(;index < $scope.houses.length;index ++){
        //             var house =  $scope.houses[index];
        //             var map = new BMap.Map("map" + index);
        //             console.log(house.latitude + " " + house.longitude);
        //             var point = new BMap.Point(house.longitude, house.latitude);
        //             var marker = new BMap.Marker(point);
        //             map.addOverlay(marker);
        //             map.centerAndZoom(point, 11);
        //             map.addControl(new BMap.MapTypeControl());
        //             map.enableScrollWheelZoom(true);
        //         }
        //     },200);
        //
        //
        // }

        $scope.detail = function(id,house){
            console.log(id);
            console.log(house);
            sessionStorage.setItem(id,  JSON.stringify(house));
            window.open("/detail.html?id=" + id);
        }


    });

    $rootScope.is_homepage = function() {
        return false;
    }
});




app.controller('EchartsController', function(House){
    House.echart(function(data){
        var myChart = echarts.init(document.getElementById('main'));
        var option =
        {
            tooltip : {
                trigger: 'axis',
                axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                    type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: {
                data: []
            },
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : [
            ]
        };
        myChart.setOption(option);
        myChart.setOption(data);
       console.log("callback");
    });
});
