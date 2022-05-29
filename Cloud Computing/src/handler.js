const express = require('express');
const { nanoid } = require('nanoid');
const multer = require('multer');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');

const db = require('./database');

const uploadController = require('./controller');

const router = express.Router();

const maxExpire = 3 * 24 * 60 * 60;
const createToken = (id) => {
    return jwt.sign({ id }, 'anticede secret string', {
        expiresIn: maxExpire,
    });
};

router
    .route('/members')
    .get(async (req, res) => {
        const result = await db.promise().query('SELECT * FROM USERS');
        res.send(result[0]);
    })
    .post(async (req, res) => {
        const {
            username,
            password,
            age,
        } = req.body;

        const id = nanoid(16);

        if (username === '') {
            const response = res.send({
                status: 'fail',
                message: 'Failed to add new member, please fill your username.',
            });
            response.status(400);
            return response;
        }
        if (username.length < 6) {
            const response = res.send({
                status: 'fail',
                message: 'Username length must be atleast 6 characters!',
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
        if (password.length < 6) {
            const response = res.send({
                status: 'fail',
                message: 'Password length must be atleast 6 characters!',
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

        const [rows] = await db.promise().query(`SELECT * FROM users WHERE username = '${req.body.username}'`);
        if (rows.length !== 0) {
            return res.status(500).json({ message: 'User with that username is already exist' });
        }

        const salt = await bcrypt.genSalt();
        const hashedPassword = await bcrypt.hash(password, salt);

        await db.promise().query(`INSERT INTO users VALUES('${id}', '${username}', '${hashedPassword}', '${age}')`);
        const token = createToken(id);
        res.cookie('jwt', token, { httpOnly: false, maxAge: maxExpire * 1000 });

        const response = res.send({
            status: 'success',
            message: 'New user is successfully added.',
            data: {
                userId: id,
            },
        });
        response.status(201);
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

        if (username === '') {
            const response = res.send({
                status: 'fail',
                message: 'Failed to add new member, please fill your username.',
            });
            response.status(400);
            return response;
        }
        if (username.length < 6) {
            const response = res.send({
                status: 'fail',
                message: 'Username length must be atleast 6 characters!',
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
        if (password.length < 6) {
            const response = res.send({
                status: 'fail',
                message: 'Password length must be atleast 6 characters!',
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

        const [rows] = await db.promise().query('SELECT * FROM users WHERE id = ?', [req.params.id]);
        if (rows.length === 0) {
            return res.status(404).json({ message: 'User with id is not found' });
        }

        await db.promise().query('UPDATE users SET username = ?, password = ?,  age = ? WHERE id = ?', [username, password, age, req.params.id]);
        const [result] = await db.promise().query('SELECT * FROM users WHERE id = ?', [req.params.id]);
        return res.status(200).json(
            { message: 'Data updated', data: result },
        );
    })

    .delete(async (req, res) => {
        const [rows] = await db.promise().query('SELECT * FROM users WHERE id = ?', [req.params.id]);
        if (rows.length === 0) {
            return res.status(404).json({ message: 'User with id is not found' });
        }

        db.promise().query('DELETE from users WHERE id = ?', [req.params.id]);
        return res.status(200).json({ message: 'User with the data below successfully deleted from database', data: rows });
    });

router
    .route('/login')
    .get(async (req, res) => {
        const {
            username,
            password,
        } = req.body;

        if (username === '') {
            const response = res.send({
                status: 'fail',
                message: 'Failed to add new member, please fill your username.',
            });
            response.status(400);
            return response;
        }
        if (username.length < 6) {
            const response = res.send({
                status: 'fail',
                message: 'Username length must be atleast 6 characters!',
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
        if (password.length < 6) {
            const response = res.send({
                status: 'fail',
                message: 'Password length must be atleast 6 characters!',
            });
            response.status(400);
            return response;
        }

        const [rows] = await db.promise().query(`SELECT * FROM users WHERE username = '${req.body.username}'`);
        if (rows.length !== 0) {
            const auth = bcrypt.compareSync(password, rows[0].password);
            if (auth) {
                const token = createToken(rows[0].id);
                res.cookie('jwt', token, { httpOnly: false, maxAge: maxExpire * 1000 });
                const response = res.status(200).json({
                    message: 'Logged in!',
                    user_id: rows[0].id,
                });
                return response;
            }
            const response = res.status(404).json({ message: 'Incorrect password!' });
            return response;
        }
        const response = res.status(404).json({ message: 'Username not found!' });
        return response;
    });

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
