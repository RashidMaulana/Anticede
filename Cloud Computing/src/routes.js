const express = require('express');
const swaggerUi = require('swagger-ui-express');
const swaggerDocument = require('../docs/anticede-openapi.json');

const { requireAuthMember, requireAuthAdmin } = require('./middlewares');

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

const routes = express.Router();

routes.use('/', swaggerUi.serve);
routes.get('/', swaggerUi.setup(swaggerDocument));

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
