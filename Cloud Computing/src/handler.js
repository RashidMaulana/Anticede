// const express = require('express');
const { nanoid } = require('nanoid');
const multer = require('multer');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');

const db = require('./database');

const uploadController = require('./controller');

// const routerAudio = express.Router();

const maxExpire = 3 * 24 * 60 * 60;
const createToken = (id) => {
    return jwt.sign({ id }, 'anticede secret string', {
        expiresIn: maxExpire,
    });
};

exports.getAllMember = async (req, res) => {
    const result = await db.promise().query('SELECT * FROM USERS');
    res.send(result[0]);
};

exports.signupPost = async (req, res) => {
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
};

exports.getMemberById = async (req, res) => {
    const [rows] = await db.promise().query('SELECT * FROM users WHERE id = ?', [req.params.id]);

    if (rows.length === 0) {
        return res.status(404).json({ message: 'User with id is not found' });
    }

    const response = res.status(200).json({ message: 'data found', data: rows[0] });
    return response;
};

exports.editMemberById = async (req, res) => {
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

    // Check if the id is exist in database
    const [rows] = await db.promise().query('SELECT * FROM users WHERE id = ?', [req.params.id]);
    if (rows.length === 0) {
        return res.status(404).json({ message: 'User with id is not found' });
    }

    // Check if the user just want to update the password or age
    if ((rows[0].username) === req.body.username && rows[0].id === req.params.id) {
        console.log(rows[0].username);
        const salt = await bcrypt.genSalt();
        const hashedPassword = await bcrypt.hash(password, salt);

        await db.promise().query('UPDATE users SET username = ?, password = ?,  age = ? WHERE id = ?', [username, hashedPassword, age, req.params.id]);
        return res.status(200).json(
            { message: 'Data updated', id: req.params.id },
        );
    }

    // Check if the username is already exist in database
    const [check] = await db.promise().query('SELECT * FROM users WHERE username = ?', [req.body.username]);
    if ((check[0].username) === req.body.username && !rows[0].id !== req.params.id) {
        return res.status(500).json({ message: 'User with that username is already exist' });
    }
    return res.status(500).json({ message: 'Error when requesting the order' });
};

exports.deleteMemberById = async (req, res) => {
    const [rows] = await db.promise().query('SELECT * FROM users WHERE id = ?', [req.params.id]);
    if (rows.length === 0) {
        return res.status(404).json({ message: 'User with id is not found' });
    }

    db.promise().query('DELETE from users WHERE id = ?', [req.params.id]);
    return res.status(200).json({ message: 'User with the data below successfully deleted from database', data: rows });
};

exports.login = async (req, res) => {
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
};

exports.logout = (req, res) => {
    res.cookie('jwt', '', { maxAge: 1 });
    const response = res.status(200).json({ message: 'Logout success' });
    return response;
};

const storage = multer.diskStorage({
    destination: './uploads',
    // uncomment below if filename is actually needed
    // filename: (req, file, callback) => {
    //     callback(null, `${nanoid()}.aac`);
    // },
});

const upload = multer({ storage });

exports.postAudio = (upload.single('audio'), uploadController);
