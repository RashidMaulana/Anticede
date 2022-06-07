const express = require('express');
const { requireAuth } = require('./authMiddleware');

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

routes.get('/members', requireAuth, getAllMember);
routes.post('/members', signupPost);

routes.get('/members/:id', requireAuth, getMemberById);
routes.put('/members/:id', requireAuth, editMemberById);
routes.delete('/members/:id', requireAuth, deleteMemberById);

routes.post('/login', login);
// routes.post('/login_admin', login_admin)

routes.post('/logout', logout);

routes.post('/upload_audio', requireAuth, postAudio, uploadController);

module.exports = routes;
