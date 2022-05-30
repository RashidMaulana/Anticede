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
} = require('./handler');

routes.get('/members', getAllMember);
routes.post('/members', signupPost);

routes.get('/members/:id', getMemberById);
routes.put('/members/:id', editMemberById);
routes.delete('/members/:id', deleteMemberById);

routes.get('/login', login);

routes.get('/logout', logout);

routes.post('/upload_audio', postAudio);

module.exports = routes;
