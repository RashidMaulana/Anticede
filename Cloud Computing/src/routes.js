const express = require('express');
const { requireAuthMember, requireAuthAdmin } = require('./middlewares');

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
    loginAdmin,
} = require('./handler');

routes.get('/members', requireAuthAdmin, getAllMember);
routes.post('/members', signupPost);

routes.get('/members/:id', requireAuthMember, getMemberById);
routes.put('/members/:id', requireAuthMember, editMemberById);
routes.delete('/members/:id', requireAuthAdmin, deleteMemberById);

routes.post('/login', login);

routes.post('/login_admin', loginAdmin);

routes.post('/logout', logout);

routes.post('/upload_audio', requireAuthMember, postAudio, uploadController);

module.exports = routes;
