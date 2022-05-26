const { CuteFFMPEG } = require('cute-ffmpeg');
const { FFMPEGRequest } = require('cute-ffmpeg');
const { nanoid } = require('nanoid');
const { Storage } = require('@google-cloud/storage');
const path = require('path');

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

    const bucketName = 'anticede-speech-test';
    const absoluteFilePath = path.join(__dirname, `../processed-audio/${processedFile}`);

    const storage = new Storage();
    async function uploadFile() {
        await storage.bucket(bucketName).upload(absoluteFilePath, {
            destination: processedFile,
        });

        console.log(`${absoluteFilePath} uploaded to ${bucketName}`);
    }

    uploadFile().catch(console.error);
};

module.exports = uploadController;
