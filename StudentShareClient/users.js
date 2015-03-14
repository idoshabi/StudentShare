
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


scotchApp.controller('TopSellersCtrl', ['$scope', '$http','$location','productService','ngDialog',
    function EventListController($scope, $http, $location, productService, ngDialog) {

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
            });
        };
        showUsers();
    }

]);
scotchApp.controller('showUserCtrl', ['$scope', '$http','$location','productService','ngDialog',
    function EventListController($scope, $http, $location, productService, ngDialog) {

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
            });
        };
        showUsers();
    }

]);