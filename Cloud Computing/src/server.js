require('dotenv').config();
const express = require('express');
// const bodyParser = require('body-parser');

const routes = require('./handler');

const app = express();

const PORT = 5000;

app.use(express.json());
app.use(routes); // temporarily use routes.js ^_^

app.listen(PORT, () => {
    console.log(`Server running on port: http://localhost:${PORT}/`);
});

app.get('/', (req, res) => {
    res.send('Hello from Homepage.');
});
