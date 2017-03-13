'use strict';

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
        console.log(data.houses);
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
