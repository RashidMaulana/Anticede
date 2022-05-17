const { addUserHandler, getAllUsersHandler } = require('./handler');

const routes = [
    {
        method: 'GET',
        path: '/',
        handler: getAllUsersHandler,
    },
    {
        method: 'POST',
        path: '/users',
        handler: addUserHandler,
    },
];

module.exports = { routes };
