// app.js
// create angular app


var validationApp = angular.module('validationApp',[] ,function($routeProvider, $locationProvider){
    $routeProvider
        .when('/Book/Edit', {
            template: '<div>Edit</div>'
        })
        .when('/Book/Delete', {
            template: '<div>Delete</div>'
        })
        .when('/Book/Show', {
            template: '<div>Show</div>'
        })
        .when('/Book/Add', {
            template: '<div>Add</div>'
        })
        .when('/Book/Error', {
            template: '<div>Error Path</div>'
        })
        .otherwise({redirectTo: '/Book/Error'});

    $locationProvider.html5Mode(true);

});

validationApp.config(function ($httpProvider) {
    $httpProvider.defaults.withCredentials = true;
});
validationApp.controller('MainCtrl', function($scope) {

        $scope.test = "123";

});


// configure our routes
var scotchApp = angular.module('scotchApp', ['ngRoute']);

// configure our routes
scotchApp.config(function($routeProvider) {
    $routeProvider

        // route for the home page
        .when('/', {
            templateUrl : 'home.html',
            controller  : 'mainController'
        })

        // route for the about page
        .when('/about', {
            templateUrl : 'login.html',
            controller  : 'loginController'
        })

        // route for the contact page
        .when('/contact', {
            templateUrl : 'contact.html',
            controller  : 'contactController'
        });
});

// create the controller and inject Angular's $scope
scotchApp.controller('mainController', function($scope) {
    // create a message to display in our view
    $scope.message = 'Everyone come and see how good I look!';
});

scotchApp.controller('aboutController', function($scope) {
    $scope.message = 'Look! I am an about page.';
});

scotchApp.controller('contactController', function($scope) {
    $scope.message = 'Contact us! JK. This is just a demo.';
});



validationApp.directive('pwCheck', [function () {
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
// create angular controller
validationApp.controller('mainController', ['$scope', '$http', function EventListController($scope, $http) {


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
                    alert("good")

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

validationApp.controller('MessagesCtrl', ['$scope', '$http', function EventListController($scope, $http) {

    $scope.viewReceivedMessages = function() {
        $http({
            method: 'GET',
            url: 'http://localhost:8080/StudentShareProject/rest/Message/RecivedMesseages',
            headers : { 'Content-Type': 'application/x-www-form-urlencoded'}
    })
            .success(function (data, status, headers, config) {
                $scope.totalData = data;
                for (index=0; index < $scope.totalData.length; ++index){
                    senderId = $scope.totalData[index].senderId;

                    addNamesToObject( senderId, $http, index, $scope);
                }
                $scope.TotalRecivedMesseages = $scope.totalData;
            }).error(function (data, status, headers, config) {
            });
    };
    $scope.viewSentMessages = function() {
        $http({
            method: 'GET',
            url: 'http://localhost:8080/StudentShareProject/rest/Message/SentMesseages',
            headers : { 'Content-Type': 'application/x-www-form-urlencoded'}
        })
            .success(function (data, status, headers, config) {
                for (index=0; index < data.length; ++index){
                    senderId = data[index].senderId;

                    addNamesToObject( senderId, $http, index, $scope);
                }
                $scope.TotalSentMesseages = data;
            }).error(function (data, status, headers, config) {
            });
    };
}]);

function addNamesToObject(senderId, http, counter, scope){

    var myDataPromise = getUserById(senderId, http);
    myDataPromise.then(function(result){
        if (result!= null){
            scope.TotalRecivedMesseages[counter].senderUserName=  result._userName;
        }

    });

}
function getUserById(id, http) {
        requestParams = {userId:id};
    return http({
        method: 'GET',
        url: 'http://localhost:8080/StudentShareProject/rest/User/getUserByID',
        params: requestParams,
        headers : { 'Content-Type': 'application/x-www-form-urlencoded'}
    }).then(function(result)
    {
    return result.data;

    });

}

validationApp.controller('loginController', ['$scope', '$http', function EventListController($scope, $http) {
$scope.isLoggedIn = false;
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
                    console.log(data);

                    $scope.isLoggedIn = true;
                    $scope.loggedUserName = $scope.user.username;
                    if (!data.success) {

                        // if not successful, bind errors to error variables
                        $scope.errorName = data.errors.name;
                        $scope.errorSuperhero = data.errors.superheroAlias;
                    } else {
                        // if successful, bind success message to message
                        $scope.message = data.message;
                    }
                }).error(function (data, status, headers, config) {
                    // called asynchronously if an error occurs

                alert(data);

                    // or server returns response with an error status.
                });

        }

    };

}]);