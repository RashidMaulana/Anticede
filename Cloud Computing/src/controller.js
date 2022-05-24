const CuteFFMPEG = require("cute-ffmpeg").CuteFFMPEG;
const FFMPEGRequest = require("cute-ffmpeg").FFMPEGRequest;
const { nanoid } = require('nanoid');

const uploadController = (req, res) => {
    let file = req.file;
    res.send(`done! ${file.filename}`);

    const ffmpeg = new CuteFFMPEG({
        overwrite: true
      });
      
      const request = new FFMPEGRequest({
        input: {
          path: `./uploads/${file.filename}`
        },
        output: {
          path: `./processed-audio/$(nanoid)`,
          bitrate: 320,
        }
      });
      
      ffmpeg.convert(request)
      .then(filePath => {
        // Done
      })
      .catch(error => {
        // Something went wrong
      })
      ;
};

module.exports = uploadController;
