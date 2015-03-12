// app.js
// create angular app


var validationApp = angular.module('validationApp',[]);

// configure our routes
var scotchApp = angular.module('scotchApp', ['ngRoute']);


// configure our routes
scotchApp.config(function($routeProvider, $httpProvider) {
    $httpProvider.defaults.withCredentials = true;

    $routeProvider
        // route for the home page
        .when('/', {
            templateUrl : 'home.html',
            controller  : 'NavBarController'
        })

        // route for the about page
        .when('/Login', {
            templateUrl : 'Login.html',
            controller  : 'loginController'
        })
        .when('/Register', {
            templateUrl : 'Register.html',
            controller  : 'RegisterController'
        })
        // route for the contact page
        .when('/ReceivedMessages', {
            templateUrl : 'ReceivedMessages.html',
            controller  : 'MessagesCtrlReceived'
        })
    .when('/SentMessages', {
        templateUrl : 'SentMessages.html',
        controller  : 'MessagesCtrlSent'
    });
});
scotchApp.controller('loginController', ['$scope', '$http','$rootScope','$window',
    function EventListController($scope, $http, $rootScope, $window) {

    // function to submit the form after all validation has occurred
    $scope.submitForm = function(isValid) {
        // check to make sure the form is completely valid
        if (!isValid) {

        } else {
            $scope.data = "username=" + $scope.user.username +
            "&password=" + $scope.user.password;

            $http({
                method: 'POST',
                url: 'http://localhost:8080/StudentShareProject/rest/User/Connect',
                data: $scope.data,  // pass in data as strings
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
            })
                .success(function (data, status, headers, config) {
                    $rootScope.isLoggedIn = true;
                    $rootScope.loggedUserName = $scope.user.username;

                    $window.location.href = '#';


                }).error(function (data, status, headers, config) {
                    // called asynchronously if an error occurs

                    alert(data);

                    // or server returns response with an error status.
                });

        }

    };

}]);
scotchApp.controller('NavBarController', function($scope, $rootScope) {
    // create a message to display in our view

});



scotchApp.directive('pwCheck', [function () {
    return {
        require: 'ngModel',
        link: function (scope, elem, attrs, ctrl) {

            var firstPassword = '#' + attrs.pwCheck;


            elem.
                append(firstPassword).
                on('keyup', function () {
                    scope.$apply(function () {
                        var v = elem.val()===$(firstPassword).val();
                        ctrl.$setValidity('pwmatch', v);
                    });
                });
        }
    }
}]);
    scotchApp.controller('RegisterController', ['$scope', '$http','$rootScope','$window',
        function EventListController($scope, $http, $rootScope, $window) {

    // function to submit the form after all validation has occurred
    $scope.submitForm = function(isValid) {
        // check to make sure the form is completely valid
        $scope.data =
            "first_Name="+ $scope.user.first_Name+
            "&last_name="+$scope.user.last_name+
            "&email="+$scope.user.email+
            "&image_url="+$scope.user.image_url+
           "&password="+ $scope.user.password+
            "&confirm_pwd=" +$scope.user.confirm_pwd+
            "&username="+ $scope.user.username+
            "&birthday="+ $scope.user.birthday
        ;
        if (isValid) {

            $http({
                method  : 'POST',
                url     : 'http://localhost:8080/StudentShareProject/rest/User/Register',
                data    : $scope.data,  // pass in data as strings
                headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
            })
                .success(function (data, status, headers, config) {
                    console.log(data);
                    $rootScope.isLoggedIn = true;
                    $rootScope.loggedUserName = $scope.user.username;
                    $window.location.href = '#';

                    return data;
                })
                .error(function(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    alert("bad")


                    // or server returns response with an error status.
                });
        }

    };

}]);
scotchApp.controller('MessagesCtrlReceived', ['$scope', '$http','$rootScope','$window',
    function EventListController($scope, $http, $rootScope, $window) {
        $scope.TotalRecivedMesseages ={}
        getMessagesById(function(data) {
            $scope.TotalRecivedMesseages.add(data);
            alert(data);

        }, $http);
    }]);

scotchApp.controller('MessagesCtrlSent', ['$scope', '$http','$rootScope','$window',

    function EventListController($scope, $http, $rootScope, $window) {
         $scope.TotalSentMesseages= {};
        getMessagesById($scope.TotalSentMesseages, $http, 'SentMesseages')


    }]);

function addNamesToObject(senderId, http, callback){

    var myData = getUserById(senderId, http, callback);




}

function getMessagesById(callback, $http) {
    $http({
        method: 'GET',
        url: 'http://localhost:8080/StudentShareProject/rest/Message/RecivedMesseages' ,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}

    }).success(function (data) {
            TotalMessagesArray = {}
            for (index=0; index < data.length; ++index) {
                senderId = data[index].senderId;
                TotalMessagesArray[index] = data[index];

                getUserById(senderId, $http, function(data1){
                        if (TotalMessagesArray[index]!=null) {
                            TotalMessagesArray[index].senderUserName = data1.userName;
                        }
                        callback(TotalMessagesArray[index]);
                    }
                );
            }
        }
    ).error(function (data) {
        });
};

function getUserById(id, http, callback) {
    requestParams = {userId: id};
    http({
        method: 'GET',
        url: 'http://localhost:8080/StudentShareProject/rest/User/getUserByID',
        params: requestParams,
        headers : { 'Content-Type': 'application/x-www-form-urlencoded'}
    }).success(function (data)
    {
        callback( data);

    });

}

