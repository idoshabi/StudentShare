'use strict';

(function() {
    scotchApp
        .service(
        'localDbManager',
        function(DB_CONFIG, $sqlite, $q) {
            var self = this;

            var createDB = function() {
                angular.forEach(DB_CONFIG.tables, function(table) {
                    var columns = [];
                    var foreignKeys = [];
                    var foreignKeySection = '';
                    var primaryKeySection = '';

                    angular.forEach(table.columns,
                        function(column) {
                            columns.push(column.name + ' '
                            + column.type);
                        });

                    angular.forEach(table.foreignKeys, function(
                        foreignKeyData) {
                        foreignKeys.push('Foreign Key ('
                        + foreignKeyData.field
                        + ') references '
                        + foreignKeyData.parentTable + '('
                        + foreignKeyData.foreignKey
                        + ') on delete cascade');
                    });

                    if (foreignKeys.length > 0) {
                        foreignKeySection = ',' + foreignKeySection
                        + foreignKeys.join(',');
                    }

                    if (table.primaryKeys.length > 0) {
                        primaryKeySection = ',' + ' primary key '
                        + '(' + table.primaryKeys.join(',')
                        + ')';
                    }

                    var query = 'CREATE TABLE IF NOT EXISTS '
                        + table.name + ' (' + columns.join(',')
                        + primaryKeySection + foreignKeySection
                        + ')';

                    self.query(query);
                    console.log('Table ' + table.name
                    + ' initialized');
                });

                angular.forEach(DB_CONFIG.indices, function(index) {
                    self.query(index);
                });
            }

            var initDatabase = function(shouldCreate) {
                if(shouldCreate) {
                    createDB();
                }


            }

            self.init = function() {
                if(window.openDatabase) {
                    self.dbService = $sqlite.openDatabase(
                        DB_CONFIG.name, '1.0', DB_CONFIG.name,
                        65536);
                    self.db = self.dbService.db;

                    // Check whether db initialization is required.
                    self.query("SELECT * FROM db_metadata", []).then(function(result) {
                        var metadata = self.fetch(result);
                        var now = new Date();
                        var lastUpdate = new Date(metadata.last_update);

                        // if the last populate occured more than 2 hours ago.
                        if(((now - lastUpdate) / (3600*1000)) > 2) {
                            initDatabase(false)
                        }
                    }, function(result) {
                        initDatabase(true);
                    });

                    self.isOpen = true;
                } else {
                    self.isOpen = false;
                }
            };

            self.query = function(query, bindings) {
                bindings = typeof bindings !== 'undefined' ? bindings
                    : [];
                var deferred = $q.defer();

                self.db.transaction(function(transaction) {
                    transaction.executeSql(query, bindings,
                        function(transaction, result) {
                            deferred.resolve(result);
                        }, function(transaction, error) {
                            deferred.reject(error);
                        });
                });

                return deferred.promise;
            };

            self.fetchAll = function(result) {
                var output = [];

                for (var i = 0; i < result.rows.length; i++) {
                    output.push(result.rows.item(i));
                }

                return output;
            };

            self.fetch = function(result) {
                return result.rows.item(0);
            };


        });
})();