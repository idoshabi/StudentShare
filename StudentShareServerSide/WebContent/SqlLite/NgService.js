(function() {

    scotchApp.service("myDataService", function(localDbManager) {
        var self = this;

        var tableName = "my_data";
        var userStateService = null;

        self.addSearchToDB = function(query){
            localDbManager.dbService.insert('Search',{
                query:query
            } , false);
        }

        self.getProductsFromDB = function(callback)
        {
            localDbManager.dbService.selectAll('product').then(
                function(result) {
                    if (result.rows.length > 0) {
                        var rslt = localDbManager.fetchAll(result);
                        callback(rslt)
                    }})};

        self.addProductToDB = function (product) {
            localDbManager.dbService.insert('product',product , false);
        }
        self.getMessagesFromDB = function(callback)
        {
            localDbManager.dbService.selectAll('Message').then(
                function(result) {
                    if (result.rows.length > 0) {
                        var rslt = localDbManager.fetchAll(result);
                        callback(rslt)
                    }})};

        self.addMessageToDB = function (message) {
            localDbManager.dbService.insert('Message',message , false);
        }
        self.addWishToDB = function (product) {
            localDbManager.dbService.insert('Wish',product , false);
        }
        self.getWishFromDB = function(callback)
        {
            localDbManager.dbService.selectAll('Wish').then(
                function(result) {
                    if (result.rows.length > 0) {
                        var rslt = localDbManager.fetchAll(result);
                        callback(rslt)
                    }})};

    });
})();