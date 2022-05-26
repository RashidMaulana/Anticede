const { CuteFFMPEG } = require('cute-ffmpeg');
const { FFMPEGRequest } = require('cute-ffmpeg');
const { nanoid } = require('nanoid');

const uploadController = (req, res) => {
    const { file } = req;
    res.send(`done! ${file.filename}`);

    const processedFile = nanoid();

    const ffmpeg = new CuteFFMPEG({
        overwrite: true,
    });

    const request = new FFMPEGRequest({
        input: {
            path: `./uploads/${file.filename}`,
        },
        output: {
            path: `./processed-audio/${processedFile}.flac`,
        },
    });

    ffmpeg.convert(request);
    // .then((filePath) => {
    // // Done
    // })
    // .catch((error) => {
    // // Something went wrong
    // });
    //
};

module.exports = uploadController;
