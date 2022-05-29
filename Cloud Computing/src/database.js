const mysql = require('mysql2');
require('dotenv').config();

module.exports = mysql.createConnection({
    host: process.env.host,
    user: process.env.user,
    password: process.env.password,
    database: process.env.database,
}); // Sesuaikan dengan data untuk bisa akses mySQL pada device sendiri
