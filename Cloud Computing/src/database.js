const mysql = require('mysql2');

module.exports = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password: '',
    database: 'anticede',
}); // Sesuaikan dengan data untuk bisa akses mySQL pada device sendiri
