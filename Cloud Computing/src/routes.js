const multer = require('multer');
const { Router } = require('express');
const { nanoid } = require('nanoid');
const uploadController = require('./controller');

const router = Router();

const storage = multer.diskStorage({
    destination: './uploads',
    filename: (req, file, callback) => {
        callback(null, `${nanoid()}.aac`);
    },
});

const upload = multer({ storage });

router.post('/upload_audio', upload.single('audio'), uploadController);

module.exports = router;
