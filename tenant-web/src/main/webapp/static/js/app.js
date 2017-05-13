'use strict';

var app = angular.module('uiApp', ['ngRoute','ngSanitize']);

var queryParam ;
var pageNum;

app.config(['$routeProvider',function ($routeProvider) {
    $routeProvider
        .when('/home',
            {
                controller: 'HomeController',
                templateUrl: '/static/partials/home.html'
            })
        // .when('/search/:query',
        //     {
        //         controller: 'SearchController',
        //         templateUrl: '/static/partials/search.html'
        //     })
        .when('/search/:query/:page_num',
            {
                controller: 'SearchController',
                templateUrl: '/static/partials/search.html'
            })
        .when('/search/:searchid',
            {
                controller: 'Search1Controller',
                templateUrl: '/static/partials/search.html',
                url : '/static/partials/search.html'
            })
        .when('/empty',
            {
                controller: 'EmptyController',
                templateUrl: '/static/partials/empty.html'
            })
        .when('/donate',
            {
                templateUrl: '/static/static/partials/donate.html'
            })
        .when('/statistic',
            {

                templateUrl: '/static/partials/statistic.html'
            })
        .when('/echart',
            {
                controller: 'EchartsController',
                templateUrl: '/static/partials/echarts_distinct.html'
            })
        .otherwise(
            {
                redirectTo: '/'
            });
}]);

app.filter('escape', function($window) {
    return $window.encodeURIComponent;
});

// Handle error image
app.directive('errSrc', function() {
    return {
        link: function(scope, element, attrs) {
            element.bind('error', function() {
                if (attrs.src != attrs.errSrc) {
                    attrs.$set('src', attrs.errSrc);
                }
            });
        }
    }
});

app.directive('repeatFinish',function(){
    return {
        link: function(scope,element,attr){
            console.log(scope.$index);
            if(scope.$last == true){
                console.log('ng-repeat执行完毕');
                scope.$eval( attr.repeatFinish )
            }
        }
    }
});
