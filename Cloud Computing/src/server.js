const express = require('express');
const cors = require('cors');
const cookieParser = require('cookie-parser');
// const swaggerUi = require('swagger-ui-express');
const routes = require('./routes');

const app = express();
const PORT = 8080;

// Middleware
app.use(cors());
app.use(express.json());
app.use(cookieParser());
app.use(express.urlencoded({
    extended: true,
}));

// Routes
app.use(routes);

app.listen(PORT, () => {
    console.log(`Server running on port: http://localhost:${PORT}/`);
});

app.get('/', (req, res) => {
    res.redirect('https://documenter.getpostman.com/view/19923907/Uz5Nisvw');
});
