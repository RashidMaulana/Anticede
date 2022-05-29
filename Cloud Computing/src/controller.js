const { CuteFFMPEG } = require('cute-ffmpeg');
const { FFMPEGRequest } = require('cute-ffmpeg');
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
    const ffmpeg = new CuteFFMPEG({
        overwrite: true,
    });

    const ffmpegRequest = new FFMPEGRequest({
        input: {
            path: `./uploads/${file.filename}`,
        },
        output: {
            path: processedFilePath,
        },
    });

    ffmpeg.convert(ffmpegRequest)
        // .then((filePath) => {
        //
        // })
        .catch((error) => {
            console.log(`error: ${error}`);
        });
    // speech-to-text
    const speechClient = new speech.SpeechClient();
    const bucketName = 'anticede-speech-test';

    async function speechToText() {
        // The path to the remote LINEAR16 file
        const gcsUri = `gs://${bucketName}/audio/${processedFile}`;

        // The audio file's encoding, sample rate in hertz, and BCP-47 language code
        const audio = {
            uri: gcsUri,
        };
        const config = {
            encoding: 'FLAC',
            languageCode: 'id-ID',
            audioChannelCount: 2,
        };
        const speechRequest = {
            audio,
            config,
        };

        // Detects speech in the audio file
        console.log('start speech to text');
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
        await speechToText();

        console.log(`${processedFilePath} uploaded successfully to ${bucketName}`);
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
