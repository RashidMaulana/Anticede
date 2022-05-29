const ffmpeg = require('fluent-ffmpeg');
const { nanoid } = require('nanoid');
const { Storage } = require('@google-cloud/storage');
const { existsSync } = require('fs');
const speech = require('@google-cloud/speech');

const uploadController = (req, res) => {
    const { file } = req;
    res.send(`done! ${file.filename}`);

    const processedFile = `${nanoid()}.flac`;
    const processedFilePath = `./processed-audio/${processedFile}`;

    // convert from AAC to FLAC
    ffmpeg()
        .input(`./uploads/${file.filename}`)
        .audioChannels(1)
        .save(processedFilePath);

    // speech-to-text
    const speechClient = new speech.SpeechClient();
    const bucketName = 'anticede-speech-test';

    async function speechToText() {
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

        const [response] = await speechClient.recognize(speechRequest);
        const transcription = response.results
            .map((result) => result.alternatives[0].transcript)
            .join('\n');
        console.log(`Transcription: ${transcription}`);
    }

    // upload to GCS
    const storage = new Storage();

    async function uploadFile() {
        await storage.bucket(bucketName).upload(processedFilePath, {
            destination: `audio/${processedFile}`,
        });
        console.log(`${processedFilePath} uploaded successfully to ${bucketName}`);
        await speechToText();
    }

    const interval = 1000;

    const checkLocalFile = setInterval(() => {
        const isExists = existsSync(processedFilePath);
        if (isExists) {
            uploadFile().catch(console.error);
            clearInterval(checkLocalFile);
        }
    }, interval);
};

module.exports = uploadController;
