const express = require('express');
const { v4: uuidv4 } = require('uuid');
const users = require('./users');

const router = express.Router();

router
    .route('/members')
    .get((req, res) => {
        res.send(users);
    })
    .post((req, res) => {
        const {
            firstName,
            lastName,
            age,
        } = req.body;

        const id = uuidv4();

        const newUser = {
            id,
            firstName,
            lastName,
            age,
        };

        if (firstName === undefined) {
            const response = res.send({
                status: 'fail',
                message: 'Failed to add new member, please fill your first name.',
            });
            response.status(400);
            return response;
        }
        if (lastName === undefined) {
            const response = res.send({
                status: 'fail',
                message: 'Failed to add new member, please fill your last name.',
            });
            response.status(400);
            return response;
        }
        if (age === undefined) {
            const response = res.send({
                status: 'fail',
                message: 'Failed to add new member, please fill your age.',
            });
            response.status(400);
            return response;
        }

        users.push(newUser);

        const isSuccess = users.filter((user) => user.id === id).length > 0;
        if (isSuccess) {
            const response = res.send({
                status: 'success',
                message: 'New member is successfully added.',
                data: {
                    userId: id,
                },
            });
            response.status(201);
            return response;
        }

        const response = res.send({
            status: 'fail',
            message: 'Failed to add new member.',
        });
        response.status(500);
        return response;
    });

module.exports = router;
