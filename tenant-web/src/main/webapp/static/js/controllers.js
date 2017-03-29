'use strict';

var globledata ;

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

    House.search($routeParams.query, $routeParams.page_num, function(data) {
        alert("search")
        // if(!data.page_count)
        //     $location.path('empty');
        // $scope.query = data.query;
        // $scope.page_count = data.page_count;
        $scope.page_count = 20;
        // $scope.page_num = data.page_num;
        // $scope.page_next = data.page_next;
        // $scope.page_prev = data.page_prev;
        $scope.page_next = data.page_num + 1;
        $scope.page_prev = data.page_num - 1;
        $scope.houses = data.houses;


        $scope.renderFinish = function(){
            console.log('渲染完之后的操作');

            console.log($scope.houses);


            var arr = $("div[name='house_body']");
            for(var i = 0; i <arr.length; i ++){
                $("<div id='map" +i+ "' style='height:200px\;width:300px'>1111</div>").appendTo(arr[i]);
            }



            // console.log(document.getElementById("map1"));

            // var map = new BMap.Map("map0");    // 创建Map实例
            // map.centerAndZoom(new BMap.Point(116.404, 39.915), 11);  // 初始化地图,设置中心点坐标和地图级别
            // map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
            // map.setCurrentCity("北京");          // 设置地图显示的城市 此项是必须设置的
            // map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
            
            var index = 0;
            for(;index < $scope.houses.length;index ++){
                var house =  $scope.houses[index];
                var map = new BMap.Map("map" + index);
                console.log(house.latitude + " " + house.longitude);
                var point = new BMap.Point(house.longitude, house.latitude);
                var marker = new BMap.Marker(point);
                map.addOverlay(marker);
                map.centerAndZoom(point, 11);
                map.addControl(new BMap.MapTypeControl());
                map.enableScrollWheelZoom(true);
            }
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
