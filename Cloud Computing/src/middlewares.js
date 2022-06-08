const jwt = require('jsonwebtoken');
require('dotenv').config();

const requireAuthMember = (req, res, next) => {
    const token = req.cookies.jwt;

    if (!token) {
        return res.status(400).json({ message: 'Token tidak terdeteksi, harap login terlebih dahulu!' });
    }

    // Check JWT exist & is verified
    jwt.verify(token, (process.env.SECRET_STRING), (err) => {
        if (err) {
            return res.status(400).json({ message: 'Anda tidak memiliki hak untuk mengakses request ini!' });
        }
        // console.log(decodedToken);
        return next();
    });
    return 0;
};

const requireAuthAdmin = (req, res, next) => {
    const token = req.cookies.jwt;

    if (!token) {
        return res.status(400).json({ message: 'Token tidak terdeteksi, harap login terlebih dahulu!' });
    }

    // Check JWT exist & is verified
    jwt.verify(token, (process.env.SECRET_STRING_ADMIN), (err) => {
        if (err) {
            return res.status(400).json({ message: 'Request ini hanya bisa diakses oleh user!' });
        }
        // console.log(decodedToken);
        return next();
    });
    return 0;
};

module.exports = { requireAuthMember, requireAuthAdmin };
