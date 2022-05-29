const express = require('express');
const cors = require('cors');
const cookieParser = require('cookie-parser');
const routes = require('./handler');
const { requireAuth } = require('./authMiddleware');
// const bodyParser = require('body-parser');

const app = express();
const PORT = 8080;

// Middleware
app.use(cors());
app.use(express.json());
app.use(cookieParser());
app.use(express.urlencoded({
    extended: true,
}));

// Server
// app.use(requireAuth, routes);
app.use(routes);

app.listen(PORT, () => {
    console.log(`Server running on port: http://localhost:${PORT}/`);
});

app.get('/', (req, res) => {
    res.send('Hello from Homepage.');
});
