const express = require('express');

const router = require('./handler');

const app = express();

const PORT = 5000;
app.use(express.json());

app.listen(PORT, () => {
    console.log(`Server running on port: http://localhost:${PORT}`);
});

app.use('/members', router);

app.get('/', (req, res) => {
    res.send('Hello from Homepage.');
});
