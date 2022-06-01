const express = require('express');

const routes = express.Router();

const {
    getAllMember,
    signupPost,
    getMemberById,
    editMemberById,
    deleteMemberById,
    login,
    logout,
    postAudio,
    uploadController,
} = require('./handler');

routes.get('/members', getAllMember);
routes.post('/members', signupPost);

routes.get('/members/:id', getMemberById);
routes.put('/members/:id', editMemberById);
routes.delete('/members/:id', deleteMemberById);

routes.post('/login', login);

routes.post('/logout', logout);

routes.post('/upload_audio', postAudio, uploadController);

module.exports = routes;
