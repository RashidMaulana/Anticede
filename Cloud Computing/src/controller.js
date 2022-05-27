const { CuteFFMPEG } = require('cute-ffmpeg');
const { FFMPEGRequest } = require('cute-ffmpeg');
const { nanoid } = require('nanoid');
const { Storage } = require('@google-cloud/storage');
const { existsSync } = require('fs');
// const fs = require('fs');
// const path = require('path');

const uploadController = (req, res) => {
    const { file } = req;
    res.send(`done! ${file.filename}`);

    const processedFile = `${nanoid()}.flac`;
    const processedFilePath = `./processed-audio/${processedFile}`;

    const ffmpeg = new CuteFFMPEG({
        overwrite: true,
    });

    const request = new FFMPEGRequest({
        input: {
            path: `./uploads/${file.filename}`,
        },
        output: {
            path: processedFilePath,
        },
    });

    ffmpeg.convert(request);

    // .then((filePath) => {
    // // Done
    // })
    // .catch((error) => {
    // // Something went wrong
    // });

    const storage = new Storage();

    const bucketName = 'anticede-speech-test';

    async function uploadFile() {
        await storage.bucket(bucketName).upload(processedFilePath, {
            destination: `audio/${processedFile}`,
        });

        console.log(`${processedFilePath} uploaded successfully to ${bucketName}`);
    }

    const checkTime = 1000;

    const timerId = setInterval(() => {
        const isExists = existsSync(processedFilePath);
        if (isExists) {
            uploadFile().catch(console.error);
            clearInterval(timerId);
        }
    }, checkTime);
};

module.exports = uploadController;
