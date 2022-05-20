const express = require('express');

const routes = require('./routes');

const app = express();

const PORT = 5000;

app.use(express.json());
app.use(routes); // temporarily use routes.js ^_^

app.listen(PORT, () => {
    console.log(`Server running on port: http://localhost:${PORT}`);
});

app.get('/', (req, res) => {
    res.send('Hello from Homepage.');
});
