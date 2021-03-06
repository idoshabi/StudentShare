
// app.js
// create angular app



// configure our routes
var scotchApp = angular.module('scotchApp', ['ngRoute', 'ngDialog']).run(function(localDbManager) {
    localDbManager.init();
});

angular.module('myApp.controllers', ['myApp.services'])
    .controller('DocumentCtrl', function($scope, Document) {
        $scope.documents = [];
        $scope.document = null;
        // Get all the documents
        Document.all().then(function(documents){
            $scope.documents = documents;
        });
        // Get one document, example with id = 2
        Document.getById(2).then(function(document) {
            $scope.document = document;
        });
    });


scotchApp.service('ShowUserService', function() {
    var user = 0

    var addProduct = function(newObj) {
        user = newObj;
    }

    var getProducts = function(){
        return user;
    }

    return {
        addProduct: addProduct,
        getProducts: getProducts
    };

});
scotchApp.service('productService', function() {
    var user = 0

    var addProduct = function(newObj) {
        user = newObj;
    }

    var getProducts = function(){
        return user;
    }

    return {
        addProduct: addProduct,
        getProducts: getProducts
    };

});

// configure our routes
scotchApp.config(function($routeProvider, $httpProvider) {
    $httpProvider.defaults.withCredentials = true;

    $routeProvider
        // route for the home page
        .when('/', {
            templateUrl : 'home.html',
            controller  : 'NavBarController'
        })
        .when('/newMessage', {
            templateUrl : 'newMessage.html',
            controller  : 'newMessageCtrl'
        })
        // route for the about page
        .when('/Login', {
            templateUrl : 'Login1.html',
            controller  : 'loginController'
        })
        .when('/TopSellers', {
            templateUrl : 'TopSellers.html',
            controller  : 'TopSellersCtrl'
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
        .when('/recommendedProducts', {
            templateUrl : 'recommendedProducts.html',
            controller  : 'recommendedProductsCtrl'
        })
        .when('/shoppingCart', {
            templateUrl : 'shoppingCart.html',
            controller  : 'ShoppingCartCtrl'
        })
        .when('/MyWishes', {
            templateUrl : 'WishList.html',
            controller  : 'WishListCtrl'
        })
        .when('/newProduct', {
            templateUrl : 'newProduct.html',
            controller  : 'newProductCtrl'
        }).when('/MyProducts', {
            templateUrl : 'MyProducts.html',
            controller  : 'myProductsCtrl'
        })
        .when('/ThankYouOrder',{
            templateUrl : 'ThankYouForOrder.html',
            controller  : 'ThankYouForOrderCtrl'
        })
    .when('/SentMessages', {
        templateUrl : 'SentMessages.html',
        controller  : 'MessagesCtrlSent'

    }).when('/TheServerIsDown', {
            templateUrl : 'TheServerIsDown.html',
            controller  : 'TheServerIsDownCtrl'
        }).when('/Account', {
        templateUrl : 'ShowUserProfile.html',
        controller  : 'showUserCtrl'
    });


});
scotchApp.controller('loginController', ['$scope', '$http','$rootScope','$window','ngDialog',
    function EventListController($scope, $http, $rootScope, $window, ngDialog) {
        // function to submit the form after all validation has occurred
    $scope.submitForm = function(isValid) {
        // check to make sure the form is completely valid
        if (!isValid) {

        } else {
            $scope.data = "username=" + $scope.user.username +
            "&password=" + $scope.user.password;

            $http({
                method: 'POST',
                url: 'rest/User/Connect',
                data: $scope.data,  // pass in data as strings
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
            })
                .success(function (data, status, headers, config) {
                    $rootScope.isLoggedIn = true;
                    $rootScope.loggedUserName = $scope.user.username;

                    $window.location.href = '#/';


                }).error(function (data, status, headers, config) {
                    // called asynchronously if an error occurs

                    ngDialog.open({
                        template: '<h4>'+data+'</h4>',
                        className: 'ngdialog-theme-default',
                        plain: true,
                        overlay: true
                    });

                    // or server returns response with an error status.
                });

        }

    };

}]);

function  getCurrentLoggedUser(http, callback){
    http({
        method  : 'GET',
        url     : 'rest/User/show',
        headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
    }).success(function(data){

        callback (data);
    }).error(function(data, status) {


        // called asynchronously if an error occurs
        if (status==403){
            callback(data);
        }
        // or server returns response with an error status.
    });
}

scotchApp.controller('NavBarController', ['$scope', '$http','$rootScope','$window', '$location',
    function EventListController($scope, $http, $rootScope, $window, $location) {
        function disconnect(){
            $rootScope.isLoggedIn = false;
            $location.path('/Login');

        }
        onRouteChangeOff = $scope.$on('$locationChangeStart', function (event, newUrl) {
                isUserConnected($http, function callback(data) {
                    if (data=='Connected') {
                        $rootScope.isLoggedIn = true;
                        getCurrentLoggedUser($http,function(user){
                                if (user != null){
                                $rootScope.loggedUserName = user._userName;
                                $rootScope.currentPoints = user._score;
                                }
                                else{
                                        disconnect()
                                }
                            }
                        )
                    }
                        if (data=='NotConnected') {

                            if (!isAllowedSite(newUrl)) {
                                disconnect(newUrl)
                            }
                        }
                    if(data=='Error'){
                        $rootScope.isLoggedIn = false;
                            $location.path('/TheServerIsDown');

                    }



                })
            }
        );
    }]);

function isAllowedSite(url){
    if (url.contains('/Register'))
        return true;

    if(url.contains('/Login'))
        return true;

    if (url.endsWith('index.html#'))
        return true;

    if (url.endsWith('index.html'))
        return true;

    if (url.endsWith('index.html#/'))
        return true;
    return false;
}
String.prototype.contains = function(it) { return this.indexOf(it) != -1; };
    String.prototype.endsWith = function(suffix) {
        return this.indexOf(suffix, this.length - suffix.length) !== -1;
    };
scotchApp.directive('isUserExist', ['$http',function ($http) {
    return {
        require: 'ngModel',
        link: function (scope, elem, attrs, ctrl) {
            elem.bind('keyup', function () {
                getIdByUser(elem.val(), $http, function(data){
                    v= true;
                    if(data != 0){
                        v= false;
                    }
                    ctrl.$setValidity('userExist', v);
                });

            });
        }
    }
}]);



scotchApp.directive('isUserNotExist', ['$http',function ($http) {
    return {
        require: 'ngModel',
        link: function (scope, elem, attrs, ctrl) {
            elem.bind('keyup', function () {
                getIdByUser(elem.val(), $http, function(data){
                    v= false;
                    if(data != 0){
                      v= true;
                    }
                    ctrl.$setValidity('userExist', v);
                });

            });
        }
    }
}]);

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
            "&username="+ $scope.user.username;
        if (isValid) {

            $http({
                method  : 'POST',
                url     : 'rest/User/Register',
                data    : $scope.data,  // pass in data as strings
                headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
            })
                .success(function (data, status, headers, config) {
                    console.log(data);
                    $rootScope.isLoggedIn = true;
                    $rootScope.loggedUserName = $scope.user.username;
                    $window.location.href = '#/';

                    return data;
                })
                .error(function(data, status, headers, config) {

                });
        }

    };

}]);

function isUserConnected(http, callback){
    http({
        method  : 'GET',
        url     : 'rest/User/show',
        headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
    }).success(function(data){
        if (data!=null) {
            callback('Connected');
        }
        else{
            callback('NotConnected');

        }
    }).error(function(data, status) {
        // called asynchronously if an error occurs
        if (status==403){
            callback('NotConnected');
        }
        else{
            callback('Error');

        }
        // or server returns response with an error status.
    });

}
function getIdByUser(user, http, callback){
    requestParams = {username: user};
    http({
        method  : 'GET',
        url     : 'rest/User/getIdByUsername',
        params     : requestParams,  // pass in data as strings
        headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
    }).success(function(data){

        callback(data);
    }).error(function(data) {
        // called asynchronously if an error occurs
        alert(data)


        // or server returns response with an error status.
    });

}
scotchApp.controller('newMessageCtrl', ['$scope', '$http','$rootScope','$location','productService',
    function EventListController($scope, $http, $rootScope, $location, productService) {
        if  (productService.getProducts()!=0) {
            $scope.username = productService.getProducts();
            productService.addProduct(null);
        }
        $scope.submitForm = function(isValid){
            if(isValid){
            getIdByUser ($scope.username,$http, function(data){

                    $scope.data = "title=" + $scope.message.title +
                    "&contant=" + $scope.message.conant+"&recipientId="+data;

                $http({
                    method: 'POST',
                    url: 'rest/Message/send',
                    data: $scope.data,  // pass in data as strings
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
                })
                    .success(function (data) {
                        $location.path('/SentMessages');

                    }).error(function (data) {
                        $location.path('/SentMessages');

                    });
            });
        }


    }}]);

scotchApp.controller('MessagesCtrlReceived', ['$scope', '$http','$rootScope','ShowUserService','$location', 'myDataService',
    function EventListController($scope, $http, $rootScope, ShowUserService,$location, myDataService) {
        $scope.showProfile = function(id){
            ShowUserService.addProduct(id);
            $location.path('/Account');
        }

        getMessagesById($http, $scope,'RecivedMesseages', myDataService);
        $scope.deleteMessage= function(id){
    deleteMessage($http,id, function(status){
        getMessagesById($http, $scope,'RecivedMesseages')
    });
        }
    }]);

function deleteMessage(http, id, callback){
    requestParams = "messeageId=" + id;
    http({
        method  : 'POST',
        url     : 'rest/Message/delete',
        data     : requestParams,  // pass in data as strings
        headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
    }).success(function(data){

        callback(data);
    }).error(function(data) {
        // called asynchronously if an error occurs
        callback(data);


        // or server returns response with an error status.
    });
}

function addToCart(http, id, callback){
    requestParams = "product_id=" + id;
    http({
        method  : 'POST',
        url     : 'rest/User/addToCart',
        data     : requestParams,  // pass in data as strings
        headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
    }).success(function(data){

        callback(data);
    }).error(function(data) {
        // called asynchronously if an error occurs
        callback(data);


        // or server returns response with an error status.
    });
}
scotchApp.controller('MessagesCtrlSent', ['$scope', '$http','$rootScope','ShowUserService','myDataService',

    function EventListController($scope, $http, $rootScope, ShowUserService, $sqlite, myDataService) {
        getMessagesById($http, $scope,'SentMesseages')
        $scope.deleteMessage= function(id){
            deleteMessage($http,id, function(status){
                getMessagesById($http, $scope,'SentMesseages', myDataService)
            });
        }

    }]);

function getMessagesById( $http, scope, type, myDataService) {
    var array;
    eval("scope."+type+"={}")
    eval("array = scope."+type);

    $http({
        method: 'GET',
        url: 'rest/Message/'+ type,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}

    }).success(function (data) {
            for (index=0; index < data.length; ++index) {
                senderId = data[index].senderId;
                array[index] = data[index];
                reciverId = data[index].reciverId;

                getUserById(senderId, $http,index, function(result, index){
                if (result!= null){

                    array[index].senderUserName=  result._userName;
                    myDataService.addMessageToDB(array[index]);
                }

            }
            );
                getUserById(reciverId, $http,index, function(result, index){
                    if (result!= null){

                        array[index].reciverUserName=  result._userName;
                        myDataService.addMessageToDB(array[index]);

                    }
                    }
                );
            }

            scope.arrayLength = index;
        }
    ).error(function (data, status) {
            if (status==403){
                alert("Please connect first!");
            }

        });
};


function sentMessage(http, id, title, contant){


}

function getUserById(id, http,counter, callback) {
    requestParams = {userId: id};
    return http({
        method: 'GET',
        url: 'rest/User/getUserByID',
        params: requestParams,
        headers : { 'Content-Type': 'application/x-www-form-urlencoded'}
    }).then(function(result)
    {
        callback(result.data, counter);

    });

}

