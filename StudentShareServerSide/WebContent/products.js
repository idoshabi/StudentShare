
scotchApp.directive('fallbackSrc', function () {
    var fallbackSrc = {
        link: function postLink(scope, iElement, iAttrs) {
            iElement.bind('error', function() {
                angular.element(this).attr("src", iAttrs.fallbackSrc);
            });
        }
    }
    return fallbackSrc;
});


scotchApp.controller('ShoppingCartCtrl', ['$scope', '$http','$location','ngDialog',
    function EventListController($scope, $http, $location, ngDialog) {

        $scope.CheckOutCart = function () {
            $http({
                method: 'GET',
                url: 'rest/User/CheckoutCart',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
            }).success(function (data) {

              if (data != 0){
                  $location.path('/ThankYouOrder');
              }
                else{
                  ngDialog.open({
                      template: '<h4>You don\'t have anything in your cart!</h4>',
                      className: 'ngdialog-theme-default',
                      plain: true,
                      overlay: true
                  });
              }
            }).error(function (data, status) {
                ngDialog.open({
                    template: '<h4>You don\'t have enough points!</h4>',
                    className: 'ngdialog-theme-default',
                    plain: true,
                    overlay: true
                });
            });
        }

        $scope.continueShopping = function(){
            $location.path('/recommendedProducts');

        }

        
        
    function loadCart(){

    $scope.deleteItemFromCart= function(id) {
        deleteItemFormCart($http, id, function(data){
            loadCart();
            })
        }
    showCart($http, function(data){
        $scope.cartItems = data;
        $scope.totalPrice = totalSum(data);

    })}

        loadCart();
        }]);
function deleteWish(http, id, callback){
    requestParams = "wish_id=" + id;
    http({
        method  : 'POST',
        url     : 'rest/Wish/delete',
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
scotchApp.controller('WishListCtrl', ['$scope', '$http','$rootScope','$window','myDataService',
    function EventListController($scope, $http, $rootScope, $window, WishListCtrl) {

        $scope.deleteWish= function(id){

            deleteWish($http,id, function(status){
                showMyWishes();
            });
        }

        function showMyWishes() {
            $http({
                method: 'GET',
                url: 'rest/Wish/show',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
            }).success(function (data) {
                $scope.wishList = data;

                for (index=0; index < data.length; ++index) {
                    $scope.wishList[index].id = data[index].id;
                    //WishListCtrl.addWishToDB($scope.wishList[index]);

                    getProductById(data[index].productId, $http, index, function(info, counter ){
                        $scope.wishList[counter].product = info;


                    });
                }
            }).error(function (data, status) {
                // called asynchronously if an error occurs
                alert(data);
                // or server returns response with an error status.
            });
        }
        showMyWishes();
    }

]);
scotchApp.controller('myProductsCtrl', ['$scope', '$http','$rootScope','$window',
    function EventListController($scope, $http, $rootScope, $window) {

        function showMyProducts() {
            $http({
                method: 'GET',
                url: 'rest/Product/myProducts',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}  // set the headers so angular passing info as form data (not request payload)
            }).success(function (data) {

                $scope.myProducts = data;
            }).error(function (data, status) {
                // called asynchronously if an error occurs
                alert(data);
                // or server returns response with an error status.
            });
        }
        showMyProducts();
    }

]);

    scotchApp.controller('newProductCtrl', ['$scope', '$http','$rootScope','ngDialog','$location',
        function EventListController($scope, $http, $rootScope, ngDialog, $location) {
            $scope.submitForm = function(isValid){

                if(isValid){
            $scope.data =
                "productName="+ $scope.product.productname+
                "&price="+$scope.product.price+
                "&quntity="+$scope.product.quantity+
                "&description="+$scope.product.description+
                "&image_url="+ $scope.product.image;

            $http({
                method  : 'POST',
                url     : 'rest/Product/add',
                data    : $scope.data,  // pass in data as strings
                headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
            })
                .success(function (data) {

                    ngDialog.open({
                        template: '<h4>'+data+'</h4>',
                        className: 'ngdialog-theme-default',
                        plain: true,
                        overlay: true
                    });
                    $location.path('/MyProducts');
                })
                .error(function(data) {
                });
        }


        }}]);


scotchApp.controller('ThankYouForOrderCtrl', ['$scope', '$http','$rootScope','$location',
    function EventListController($scope, $http, $rootScope, $location) {

        $scope.continueShopping = function (){
            $location.path('/recommendedProducts');

        }
    }]);
function addWish(http, productId, ngDialog){
    myData = "product_id="+productId;

    http({
        method  : 'POST',
        url     : 'rest/Wish/add',
        data    : myData,  // pass in data as strings
        headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
    })
        .success(function (data) {
            ngDialog.open({
                template: '<h4>Wish added</h4>',
                className: 'ngdialog-theme-default',
                plain: true,
                overlay: true

            });
        })
        .error(function(data) {
            ngDialog.open({
                template: '<h4>Wish added</h4>',
                className: 'ngdialog-theme-default',
                plain: true,
                overlay: true

            });
        });
}
scotchApp.controller('recommendedProductsCtrl', ['$scope', '$http','$rootScope','ngDialog', 'myDataService',
    function EventListController($scope, $http, $rootScope, ngDialog, myDataService) {
        data = {max:200};
        $scope.addToCart = function(id){
            addItemToCart($http, id, function(data){
                ngDialog.open({
                    template: '<h4>'+data+'  <i class="glyphicon glyphicon-shopping-cart"></i></h4>',
                    className: 'ngdialog-theme-default',
                    plain: true,
                    overlay: true

            });
        })}

        $scope.addWish= function(id){addWish($http, id, ngDialog);}

        $http({
            method  : 'GET',
            url     : 'rest/Product/getTop',
            params: data,
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
        }).success(function(data){

            $rootScope.recommendedProducts = data;

            for (i = 0 ;i<data.length;i++ )
            {
                getUserById(data[i]._seller_id, $http, i, function(data, index){
                    $rootScope.recommendedProducts[index].sellerName = data._userName;
                })
            }

            addProductsToDB(myDataService, data);

        }).error(function(data, status) {
            // called asynchronously if an error occurs

            // or server returns response with an error status.
        });
    }]);

    function addProductsToDB(Service, data){
        for (i = 0 ;i<data.length;i++ )
        {
            Service.addProductToDB(data[i]);
        }
    }

    function GetSellerDetails(http, id, callback){


    }

    function SearchProducts(http, key, callback){

        dataToSend = {searchPattren:key};
        http({
            method  : 'GET',
            url     : 'rest/Product/search',
            params: dataToSend,
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
        }).success(function(data){

            callback(data);
        }).error(function(data, status) {
            // called asynchronously if an error occurs
            callback(data);
            // or server returns response with an error status.
        });
    }
    function addItemToCart(http, id, callback){
        dataToSend = {product_id: id}
        http({
            method  : 'GET',
            url     : 'rest/User/addToCart',
            params: dataToSend,
            headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
        }).success(function(data){

          callback(data);
        }).error(function(data, status) {
            // called asynchronously if an error occurs
            callback(data);
            // or server returns response with an error status.
        });
    }
scotchApp.directive('searchProducts', ['$http', '$rootScope','myDataService',function ($http, $rootScope, myDataService) {
    return {
        require: 'ngModel',
        link: function (scope, elem, attrs, ctrl) {
            elem.bind('keyup', function () {
                SearchProducts( $http, elem.val(), function(data){

                        $rootScope.recommendedProducts = data;
                    myDataService.addSearchToDB(elem.val());
                });

            });
        }
    }
}]);
function deleteItemFormCart(http, id, callback){
    dataToSend = {product_id:id};
    http({
        method  : 'GET',
        url     : 'rest/User/deleteItemFromCart',
        params: dataToSend,
        headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
    }).success(function(data){

        callback(data);
    }).error(function(data, status) {
        // called asynchronously if an error occurs
        callback(data);
        // or server returns response with an error status.
    });

}
function totalSum(array){
    sum = 0;
    for(index = 0; index <array.length ;++index){
        try{
            sum +=array[index].price;
        }
        catch (e){
            console.error(e)
        }

    }

    return sum;
}
    function showCart(http, callback){
            http({
                method  : 'GET',
                url     : 'rest/User/showCart',
                headers : { 'Content-Type': 'application/x-www-form-urlencoded' }  // set the headers so angular passing info as form data (not request payload)
            }).success(function(data){

                callback(data);
            }).error(function(data, status) {
                // called asynchronously if an error occurs
                callback(data);
                // or server returns response with an error status.
            });
    }

function getProductById(id, http,counter, callback) {
    requestParams = 'product_id='+id;
    return http({
        method: 'POST',
        url: 'rest/Product/show',
        data: requestParams,
        headers : { 'Content-Type': 'application/x-www-form-urlencoded'}
    }).then(function(result)
    {
        callback(result.data, counter);

    });

}

