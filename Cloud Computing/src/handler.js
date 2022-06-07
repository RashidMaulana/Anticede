const { nanoid } = require('nanoid');
const { Storage } = require('@google-cloud/storage');
const fs = require('fs-extra');
const multer = require('multer');
const jwt = require('jsonwebtoken');
const bcrypt = require('bcrypt');
const ffmpeg = require('fluent-ffmpeg');
const speech = require('@google-cloud/speech');
const axios = require('axios');
const db = require('./database');
const wordlist = require('./wordlist');
require('dotenv').config();

const maxExpire = 3 * 24 * 60 * 60;
const createToken = (id) => jwt.sign({ id }, process.env.SECRET_STRING, {
    expiresIn: maxExpire,
});
const createTokenAdmin = (id) => jwt.sign({ id }, process.env.SECRET_STRING_ADMIN, {
    expiresIn: maxExpire,
});

exports.getAllMember = async (req, res) => {
    const result = await db.promise().query('SELECT * FROM users');
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

    // Check if the id is exist in database.
    const [rows] = await db.promise().query('SELECT * FROM users WHERE id = ?', [req.params.id]);
    if (rows.length === 0) {
        return res.status(404).json({ message: 'User with id is not found' });
    }

    // Check if the username is not used by other user.
    const [check] = await db.promise().query('SELECT * FROM users WHERE username = ?', [req.body.username]);
    if (check.length === 0) {
        const salt = await bcrypt.genSalt();
        const hashedPassword = await bcrypt.hash(password, salt);

        await db.promise().query('UPDATE users SET username = ?, password = ?,  age = ? WHERE id = ?', [username, hashedPassword, age, req.params.id]);
        return res.status(200).json(
            { message: 'Data updated', id: req.params.id },
        );
    }

    // Check if the username is already used by other user.
    if (check.rows !== 0 && check[0].id !== req.params.id) {
        return res.status(500).json({ message: 'User with that username is already exist' });
    }

    const salt = await bcrypt.genSalt();
    const hashedPassword = await bcrypt.hash(password, salt);

    await db.promise().query('UPDATE users SET username = ?, password = ?,  age = ? WHERE id = ?', [username, hashedPassword, age, req.params.id]);
    return res.status(200).json(
        { message: 'Data updated', id: req.params.id },
    );
};

exports.deleteMemberById = async (req, res) => {
    const [rows] = await db.promise().query('SELECT * FROM users WHERE id = ?', [req.params.id]);

    // Check if the id is found in database or not
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

exports.loginAdmin = async (req, res) => {
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

    const [rows] = await db.promise().query(`SELECT * FROM admins WHERE username = '${req.body.username}'`);
    if (rows.length !== 0) {
        const auth = bcrypt.compareSync(password, rows[0].password);
        if (auth) {
            const token = createTokenAdmin(rows[0].id);
            res.cookie('jwt', token, { httpOnly: false, maxAge: maxExpire * 1000 });
            const response = res.status(200).json({
                message: 'Logged in as admin!',
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

// TODO
const limits = {
    files: 1,
    fileSize: 1024 * 1024 * 5,
};

const fileFilter = (req, file, cb) => {
    if (file.mimetype !== 'audio/x-aac') {
        cb(new Error('invalid file type: only .aac audio file is allowed.'));
    }

    cb(null, true);
};

const storage = multer.diskStorage({
    destination: './uploads',
});

const upload = multer({ storage, limits, fileFilter });

exports.postAudio = upload.single('audio');

exports.uploadController = async (req, res) => {
    const { file } = req;

    // res.status(200).send({ message: 'Upload finished!' });

    const processedFile = `${nanoid()}.flac`;
    const processedFilePath = `./processed-audio/${processedFile}`;

    let transcription = null;
    const servingInput = [];

    // string to index
    const tokenize = () => {
        const userInputString = transcription.toLowerCase().split(' ');

        userInputString.forEach((element, index) => {
            if (wordlist.indexOf(userInputString[index]) === -1) {
                servingInput.push(0);
            } else {
                servingInput.push(wordlist.indexOf(userInputString[index]));
            }
        });
    };

    // post to tf-serving
    const postToModel = () => {
        axios.post('http://localhost:8501/v1/models/anticede:predict', {
            inputs: [servingInput],
        }).then((axiosRes) => {
            console.log(`statusCode: ${axiosRes.status}`);
            console.log(axiosRes.data);
            let { outputs } = axiosRes.data;
            outputs = outputs.flat();
            const index = outputs.indexOf(Math.max(...outputs));
            console.log(outputs, index);

            let responseMessage = null;
            switch (index) {
            case 0:
                responseMessage = 'text is type A';
                break;
            case 1:
                responseMessage = 'text is type B';
                break;
            case 2:
                responseMessage = 'text is type C';
                break;
            case 3:
                responseMessage = 'text is type D';
                break;
            default:
                responseMessage = 'this is default response message';
            }
            res.status(200).send({ message: responseMessage });
        })
            .catch((error) => {
                console.error(error);
            });
    };

    // speech-to-text
    const speechClient = new speech.SpeechClient();
    const bucketName = 'anticede-speech-test';

    const speechToText = async () => {
        const gcsUri = `gs://${bucketName}/audio/${processedFile}`;

        const audio = {
            uri: gcsUri,
        };
        const config = {
            encoding: 'FLAC',
            languageCode: 'id-ID',
        };
        const speechRequest = {
            audio,
            config,
        };

        const [speechResponse] = await speechClient.recognize(speechRequest);
        transcription = speechResponse.results
            .map((result) => result.alternatives[0].transcript)
            .join('\n');
        console.log(`Transcription: ${transcription}`);
    };

    // upload to GCS
    const gcs = new Storage();

    const uploadFile = async () => {
        await gcs.bucket(bucketName).upload(processedFilePath, {
            destination: `audio/${processedFile}`,
        });
        console.log(`${processedFilePath} uploaded successfully to ${bucketName}`);
        await speechToText();
        tokenize();
        console.log(servingInput);
        postToModel();
    };

    // convert from AAC to FLAC
    ffmpeg()
        .input(`./uploads/${file.filename}`)
        .audioChannels(1)
        .on('error', (err) => {
            console.log(`error: ${err.message}`);
            fs.emptyDir('./uploads');
        })
        .on('end', () => {
            console.log('audio processing finished!');
            uploadFile().catch(console.error);
            fs.emptyDir('./uploads');
            fs.emptyDir('./processed-audio');
        })
        .save(processedFilePath);

    // wait for audio process to finish
    // const interval = 1000;

    // const checkLocalFile = setInterval(() => {
    //     const isExists = fs.existsSync(processedFilePath);
    //     if (isExists) {
    //         uploadFile().catch(console.error);
    //         fs.emptyDir('./uploads');
    //         fs.emptyDir('./processed-audio');
    //         clearInterval(checkLocalFile);
    //     }
    // }, interval);
};
