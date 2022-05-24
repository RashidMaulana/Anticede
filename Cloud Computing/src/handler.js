const express = require('express');
const { nanoid } = require('nanoid');
const multer = require('multer');
const uploadController = require('./controller');

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

        const id = nanoid(16);

        const newUser = {
            id,
            firstName,
            lastName,
            age,
        };

        if (firstName === '') {
            const response = res.send({
                status: 'fail',
                message: 'Failed to add new member, please fill your first name.',
            });
            response.status(400);
            return response;
        }
        if (lastName === '') {
            const response = res.send({
                status: 'fail',
                message: 'Failed to add new member, please fill your last name.',
            });
            response.status(400);
            return response;
        }
        if (age === '') {
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

router
    .route('/members/:id')
    .get((req, res) => {
        const { id } = req.params;

        const user = users.filter((b) => b.id === id);

        if (user.length > 0) {
            return res.json({
                status: 'success',
                data: {
                    user,
                },
            });
        }

        const response = res.send({
            status: 'fail',
            message: 'Member with id is not found',
        });
        response.status(404);
        return response;
    })

    .put((req, res) => {
        const { id } = req.params;

        const {
            firstName,
            lastName,
            age,
        } = req.body;

        const index = users.findIndex((u) => u.id === id);
        if (index !== -1) {
            users[index] = {
                ...users[index],
                firstName,
                lastName,
                age,
            };
            if (firstName === '') {
                const response = res.send({
                    status: 'fail',
                    message: 'Failed to add new member, please fill your first name.',
                });
                response.status(400);
                return response;
            }
            if (lastName === '') {
                const response = res.send({
                    status: 'fail',
                    message: 'Failed to add new member, please fill your last name.',
                });
                response.status(400);
                return response;
            }
            if (age === '') {
                const response = res.send({
                    status: 'fail',
                    message: 'Failed to add new member, please fill your age.',
                });
                response.status(400);
                return response;
            }
            const response = res.send({
                status: 'success',
                message: 'User berhasil diperbarui',
                id_User: id,
            });
            response.status(200);
            return response;
        }

        const response = res.send({
            status: 'fail',
            message: 'Failed to update the member. Id not found',
            id: req.params.id,
        });
        response.status(404);
        return response;
    })

    .delete((req, res) => {
        const { id } = req.params;

        const index = users.findIndex((u) => u.id === id);

        if (index !== -1) {
            users.splice(index, 1);
            const response = res.send({
                status: 'success',
                message: 'Member successfully deleted',
            });
            response.status(200);
            return response;
        }
        const response = res.send({
            status: 'fail',
            message: 'Failed to delete member. Id not found',
            id: req.params.id,
        });
        response.status(404);
        return response;
    });

const storage = multer.diskStorage({
    destination: './uploads',
    filename: (req, file, callback) => {
        callback(null, `${nanoid()}.aac`);
    },
});

const upload = multer({ storage });

router.post('/upload_audio', upload.single('audio'), uploadController);

module.exports = router;
