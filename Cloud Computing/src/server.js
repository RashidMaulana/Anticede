const express = require('express');
const bodyParser = require('body-parser');

const { routes } = require('./routes.js'); 

const app = express();

const PORT = 5000;
app.use(bodyParser.json());

app.listen(PORT, () => {
    console.log(`Server running on port: http://localhost:${PORT}`);
});

app.route('routes');

app.get('/', (req, res) => {
    res.send(`Hello from Homepage.`)
});