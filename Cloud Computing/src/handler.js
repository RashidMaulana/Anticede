const express = require('express');
const { nanoid } = require('nanoid');
const multer = require('multer');
const db = require('./database');

const jwt = require('jsonwebtoken');

const uploadController = require('./controller');

const users = require('./users');

const router = express.Router();

function authenticateToken(req, res, next) {
    const authHeader = req.headers.authorization;

    const token = authHeader && authHeader.split(' ')[1];
    if (token == null) {
        return res.sendStatus(401);
    }

    jwt.verify(token, process.env.ACCESS_TOKEN_SECRET, (err, user) => {
        if (err) {
            return res.sendStatus(403);
        }
        req.user = user;
        next();
    });
}

router
    .route('/test')
    .get((req, res) => {
        res.send(users);
    })
    .post((req, res) => {
        const {
            username,
            password,
            age,
        } = req.body;

        const id = nanoid(16);
        const newUser = {
            id,
            username,
            password,
            age,
        };
        users.push(newUser);
        db.promise().query(`INSERT INTO users VALUES('${newUser.id}', '${newUser.username}', '${newUser.password}', '${newUser.age}')`);
        const isSuccess = users.filter((user) => user.id === id).length > 0;
        // if (username && password && age) {
        //     db.promise().query(`INSERT INTO users VALUES('${newUser.id}', '${newUser.username}', '${newUser.password}', '${newUser.age}')`);
        //     res.status(201).send({ msg: 'Created user' });
        // }
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
    .route('/members')
    .get(async (req, res) => {
        const result = await db.promise().query('SELECT * FROM USERS');
        res.send(result[0]);
    })
    .post((req, res) => {
        const {
            username,
            password,
            age,
        } = req.body;

        const id = nanoid(16);

        const newUser = {
            id,
            username,
            password,
            age,
        };

        if (username === '') {
            const response = res.send({
                status: 'fail',
                message: 'Failed to add new member, please fill your username.',
            });
            response.status(400);
            return response;
        }
        if (password === '') {
            const response = res.send({
                status: 'fail',
                message: 'Failed to add new member, please fill your password.',
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

        // res.json(users.filter(post => post.firstName === req.user.firstName));
        // const accessToken = jwt.sign(newUser, process.env.ACCESS_TOKEN_SECRET);
        // res.json({ accessToken: accessToken });
        users.push(newUser);
        db.promise().query(`INSERT INTO users VALUES('${newUser.id}', '${newUser.username}', '${newUser.password}', '${newUser.age}')`);

        const isSuccess = users.filter((user) => user.id === id).length > 0;
        if (isSuccess) {
            const response = res.send({
                status: 'success',
                message: 'New user is successfully added.',
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
    .get(async (req, res) => {
        const [rows] = await db.promise().query('SELECT * FROM users WHERE id = ?', [req.params.id]);

        if (rows.length === 0) {
            return res.status(404).json({ message: 'User with id is not found' });
        }

        const response = res.status(200).json({ message: 'data found', data: rows[0] });
        return response;
    })

    .put(async (req, res) => {
        const {
            username,
            password,
            age,
        } = req.body;

        const [rows] = await db.promise().query('SELECT * FROM users WHERE id = ?', [req.params.id]);
        if (rows.length === 0) {
            return res.status(404).json({ message: 'User with id is not found' });
        }

        await db.promise().query('UPDATE users SET username = ?, password = ?,  age = ? WHERE id = ?', [username, password, age, req.params.id]);
        const [result] = await db.promise().query('SELECT * FROM users WHERE id = ?', [req.params.id]);
        return res.status(200).json(
            { message: 'Data updated', data: result },
        );
        // const index = users.findIndex((u) => u.id === id);
        // if (index !== -1) {
        //     users[index] = {
        //         ...users[index],
        //         username,
        //         password,
        //         age,
        //     };
        //     if (username === '') {
        //         const response = res.send({
        //             status: 'fail',
        //             message: 'Failed to add new member, please fill your first name.',
        //         });
        //         response.status(400);
        //         return response;
        //     }
        //     if (password === '') {
        //         const response = res.send({
        //             status: 'fail',
        //             message: 'Failed to add new member, please fill your last name.',
        //         });
        //         response.status(400);
        //         return response;
        //     }
        //     if (age === '') {
        //         const response = res.send({
        //             status: 'fail',
        //             message: 'Failed to add new member, please fill your age.',
        //         });
        //         response.status(400);
        //         return response;
        //     }
        //     const response = res.send({
        //         status: 'success',
        //         message: 'User berhasil diperbarui',
        //         id_User: id,
        //     });
        //     response.status(200);
        //     return response;
        // }

        // const response = res.send({
        //     status: 'fail',
        //     message: 'Failed to update the member. Id not found',
        //     id: req.params.id,
        // });
        // response.status(404);
        // return response;
    })

    .delete(async (req, res) => {
        const [rows] = await db.promise().query('SELECT * FROM users WHERE id = ?', [req.params.id]);
        if (rows.length === 0) {
            return res.status(404).json({ message: 'User with id is not found' });
        }

        db.promise().query('DELETE from users WHERE id = ?', [req.params.id]);
        return res.status(200).json({ message: 'User with the data below successfully deleted from database', data: rows });
        // db.getConnection((err, connection) => {
        //     if (err) throw err;

        //     connection.query('DELETE from users WHERE id = ?', [req.params.id], (err, rows) => {
        //         if (!err) {
        //             res.send(`User with the record ID: ${req.params.id} has been removed`);
        //         } else {
        //             console.log(err);
        //         }
        //     });
        // });
    });

    // .delete((req, res) => {
    //     const { id } = req.params;

    //     const index = users.findIndex((u) => u.id === id);

    //     if (index !== -1) {
    //         users.splice(index, 1);
    //         const response = res.send({
    //             status: 'success',
    //             message: 'Member successfully deleted',
    //         });
    //         response.status(200);
    //         return response;
    //     }
    //     const response = res.send({
    //         status: 'fail',
    //         message: 'Failed to delete member. Id not found',
    //         id: req.params.id,
    //     });
    //     response.status(404);
    //     return response;
    // });

const storage = multer.diskStorage({
    destination: './uploads',
    // uncomment below if filename is actually needed
    // filename: (req, file, callback) => {
    //     callback(null, `${nanoid()}.aac`);
    // },
});

const upload = multer({ storage });

router.post('/upload_audio', upload.single('audio'), uploadController);

module.exports = router;
