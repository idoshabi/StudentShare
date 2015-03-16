scotchApp
    .constant('DB_CONFIG', {
        name: 'DB',
        tables: [
            {
                name: 'product',
                columns: [
                    {name: 'product_id', type: 'integer not null'},
                    {name: '_productName', type: 'text'},
                    {name: 'price', type: 'integer'},
                    {name: '_seller_id', type: 'integer'},
                    {name: '_quntity', type: 'integer'},
                    {name: '_sold', type: 'integer'},
                    {name: 'description', type: 'text'},
                    {name: 'imageUrl', type: 'text'},
                    {name: 'id', type: 'integer'},

                ],
                primaryKeys: ['product_id'],
                foreignKeys: [
                ]
            }, {
                name: 'Wish',
                columns: [
                    {name: 'wish_id', type: 'integer not null'},

                    {name: 'product_id', type: 'integer not null'},
                    {name: 'productId', type: 'integer'},
                    {name: 'userId', type: 'integer'},
                    {name: 'id', type: 'integer'},
                    {name: 'dataTime', type: 'datetime'},


                ],
                primaryKeys: ['wish_id'],
                foreignKeys: [
                    { field: 'wish_id', parentTable: 'rooms', foreignKey: 'id1' },
                ]
            }, {
                name: 'Message',
                columns: [
                    {name: 'message_id', type: 'integer not null'},
                    {name: 'title', type: 'text'},
                    {name: 'id', type: 'integer'},

                    {name: 'contant', type: 'text'},
                    {name: 'senderId', type: 'integer'},

                    {name: 'senderUserName', type: 'integer'},
                    {name: 'reciverUserName', type: 'integer'},
                    {name: 'reciverId', type: 'integer'},
                    {name: 'dataTime', type: 'datetime'},
                ],
                primaryKeys: ['message_id'],
                foreignKeys: [
                    { field: 'message_id', parentTable: 'rooms', foreignKey: 'id1' },
                ]
            }, {
                name: 'Search',
                columns: [
                    {name: 'search_id', type: 'integer not null'},
                    {name: 'query', type: 'text'},
                    {name: 'time', type: 'datetime'},
                ],
                primaryKeys: ['search_id'],
                foreignKeys: [
                    { field: 'search_id', parentTable: 'rooms', foreignKey: 'id1' },
                ]
            }

        ]
    });

//{
//    name: 'products',
//    columns: [
//        {name: 'productName', type: 'text not null'},
//        {name: 'price', type: 'integer not null'},
//        {name: 'sellerId', type: 'integer not null'},
//        {name: 'quantity', type: 'integer not null'},
//        {name: 'soldCount', type: 'integer not null'},
//        {name: 'description', type: 'text not null'},
//        {name: 'image_url', type: 'text not null'},
//    ],
//    primaryKeys: ['product_id'],
//    foreignKeys: [
//        { field: 'product_id', parentTable: 'products', foreignKey: 'id' },
//    ]
//}