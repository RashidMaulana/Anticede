const uuidv4 = require('uuid');

const users = require('./users');

const getAllUsersHandler = () => ({
    status: 'success',
    data: {
        users: users.map((user) => ({
            id: user.id,
            firstName: user.firstName,
            lastName: user.lastName,
            age: user.age,
        })),
    },
});

const addUserHandler = (req, h) => {
    const {
        firstName,
        lastName,
        age,
    } = req.payload;

    const id = uuidv4();

    const newUser = {
        id,
        firstName,
        lastName,
        age,
    };

    if (firstName === undefined) {
        const response = h.response({
            status: 'fail',
            message: 'Failed to add new member, please fill your first name.',
        });
        response.code(400);
        return response;
    }
    if (lastName === undefined) {
        const response = h.response({
            status: 'fail',
            message: 'Failed to add new member, please fill your last name.',
        });
        response.code(400);
        return response;
    }
    if (age === undefined) {
        const response = h.response({
            status: 'fail',
            message: 'Failed to add new member, please fill your age.',
        });
        response.code(400);
        return response;
    }

    users.push(newUser);

    const isSuccess = users.filter((user) => user.id === id).length > 0;
    if (isSuccess) {
        const response = h.response({
            status: 'success',
            message: 'New member is successfully added.',
            data: {
                userId: id,
            },
        });
        response.code(201);
        return response;
    }

    const response = h.response({
        status: 'fail',
        message: 'Failed to add new member.',
    });
    response.code(500);
    return response;
};

module.exports = { addUserHandler, getAllUsersHandler };

// router.get('/', (req, res) => {
//     console.log(users);

//     res.send(users);
// });

// router.post('/', (req, res) => {
//     const user = req.body;

//     users.push({ ...user, id: uuidv4() });

//     res.send(`User with the name ${user.firstName} added to the database!`);
// });

// router.get('/:id', (req, res) => {
//     const { id } = req.params;

//     const foundUser = users.find((user) => user.id === id);

//     res.send(foundUser);
// })

// router.delete('/:id', (req, res) => {
//     const { id } = req.params;

//     users = users.filter((user) => user.id !== id);
//     res.send(`User with the id ${id} deleted from the database.`);
// });

// router.put('/:id', (req, res) => {
//     const { id } = req.params;
//     const { firstName, lastName, age } = req.body;
    
//     const user = users.find((user) => user.id === id);

//     if (firstName) {
//         user.firstName = firstName
//     }

//     if (lastName) {
//         user.lastName = lastName
//     }

//     if (age) {
//         user.age = age
//     }

//     res.send(`User with the id ${id} has been updated.`)
// })

// export default router;