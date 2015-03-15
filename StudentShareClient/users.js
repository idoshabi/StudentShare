
scotchApp.controller('InsideCtrl', ['$scope', '$http','$location','productService','ngDialog',
    function EventListController($scope, $http, $location, productService, ngDialog) {



        $scope.openSecond = function () {

            $http({
                method  : 'GET',
                url     : 'http://localhost:8080/StudentShareProject/rest/User/delete',
                headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
            }).success(function(data){
                openDeleteMsg(ngDialog);
            }).error(function(data, status) {
                // called asynchronously if an error occurs
                openDeleteMsg(ngDialog);
                // or server returns response with an error status.
            });
        }
    }
]);

function openDeleteMsg(ngDialog){
    ngDialog.open({
        template: '<h4>Delete, bey bet</h4>',
        className: 'ngdialog-theme-default',
        plain: true
    });
}


scotchApp.controller('TopSellersCtrl', ['$scope', '$http','$location','productService','ngDialog','ShowUserService',
    function EventListController($scope,$http, $location, productService, ngDialog, ShowUserService) {

        $scope.sendMessage = function(username){
            productService.addProduct(username)
            $location.path("/newMessage");
        }



        $scope.showProfile = function(id){
            ShowUserService.addProduct(id);
            $location.path('/Account');
        }

        getTopSeller($http, 100, function(data){
            $scope.TopSellerss ={}
            for(index = 0; index < data.length; index++){
                if (data[index]!=null){
                    $scope.TopSellerss[index] = data[index];
                }
            }
        })

    }

]);

function getTopSeller(http, num, callback){
    data_get = {max:num};
    http({
        method: 'GET',
        url: 'http://localhost:8080/StudentShareProject/rest/User/getTop',
        params: data_get,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)

    }).success(function (data) {

        callback(data);
    }).error(function (data, status) {
        // called asynchronously if an error occurs
        callback(data);
        // or server returns response with an error status.
    });
}
scotchApp.controller('showUserCtrl', ['$scope', '$http','$location','productService','ngDialog', 'ShowUserService',
    function EventListController($scope, $http, $location, productService, ngDialog, ShowUserService) {

        $scope.deleteMyAccount= function(){
            ngDialog.open({
                template: 'firstDialogId',
                controller: 'InsideCtrl',
                className: 'ngdialog-theme-default'
            });
        }
        $scope.sendMessage = function(username){

            productService.addProduct(username)
            $location.path("/newMessage");
        }
        function showUsers() {

            if  (ShowUserService.getProducts()!=0) {
                userId = ShowUserService.getProducts();
                getUserData($http, userId, function(data){
                    $scope.myDeatils = data;
                })
                ShowUserService.addProduct(null);
                $scope.isMyUser = false;

            }
            else{
                $scope.isMyUser = true;

                $http({
                method: 'GET',
                url: 'http://localhost:8080/StudentShareProject/rest/User/show',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
            }).success(function (data) {

                $scope.myDeatils = data;
            }).error(function (data, status) {
                // called asynchronously if an error occurs
                alert(data);
                // or server returns response with an error status.
            });}
        };
        showUsers();
    }

]);

function getUserData(http, userid, callback){
    data_get ={id:userid}
    http({
        method: 'GET',
        url: 'http://localhost:8080/StudentShareProject/rest/User/showUserSomeData',
        params: data_get,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)

    }).success(function (data) {

        callback(data);
    }).error(function (data, status) {
        // called asynchronously if an error occurs
        callback(data);
        // or server returns response with an error status.
    });
}