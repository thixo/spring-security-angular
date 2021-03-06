angular.module('hello', [ 'ngRoute' ]

    ).config(function($routeProvider, $httpProvider) {

        $routeProvider.when('/', {
            templateUrl : 'home.html',
            controller : 'home'
        }).when('/login', {
            templateUrl : 'login.html',
            controller : 'navigation'
        }).otherwise('/');

        $httpProvider.defaults.headers.common["X-Requested-With"] = "XMLHttpRequest";

    }).controller('navigation', function($rootScope, $scope, $http, $location, $route) {
        
        $scope.tab = function(route) {
            return $route.current && route === $route.current.controller 
        }
        
        var doAuthenticate = function(credentials, callback) {

            var headers = {};

            if (credentials) {
                var value = "Basic " + btoa(credentials.username + ":" + credentials.password);
                headers = { authorization : value };
            }

            $http.get('user', { headers : headers }
                ).success(function(data) {
                    if (data.name) {
                        $rootScope.authenticated = true;
                    } else {
                        $rootScope.authenticated = false;
                    }
                    callback && callback($rootScope.authenticated);
                }).error(function() {
                    $rootScope.authenticated = false;
                    callback && callback(false);
                });
        }

        doAuthenticate();

        $scope.credentials = {};

        $scope.login = function() {
            doAuthenticate(
                $scope.credentials,
                function(authenticated) {
                    if (authenticated) {
                        console.log("Login succeeded");
                        $location.path("/");
                        $scope.error = false;
                        $rootScope.authenticated = true;
                    } else {
                        console.log("Login failed");
                        $location.path("/login");
                        $scope.error = true;
                        $rootScope.authenticated = false;
                    }
                });
        }

        $scope.logout = function() {
            $http.post('logout', {}
                ).success(function() {
                    $rootScope.authenticated = false;
                    $location.path('/');
                }).error(function(data) {
                    $rootScope.authenticated = false;
                });
        }

    }).controller( 'home',  function($scope, $http) {
        $http.get('http://localhost:9000/'
            ).success(function(data) { $scope.greeting = data; } );
    });
