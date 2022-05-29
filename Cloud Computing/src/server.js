// require('dotenv').config();
const express = require('express');
const cors = require('cors');
// const bodyParser = require('body-parser');

const routes = require('./handler');

const app = express();
const PORT = 8080;

app.use(cors());
app.use(express.json());
app.use(express.urlencoded({
    extended: true,
}));

app.use(routes); // temporarily use routes.js ^_^

app.listen(PORT, () => {
    console.log(`Server running on port: http://localhost:${PORT}/`);
});

app.get('/', (req, res) => {
    res.send('Hello from Homepage.');
});
