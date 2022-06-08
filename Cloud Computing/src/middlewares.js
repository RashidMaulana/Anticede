const jwt = require('jsonwebtoken');
require('dotenv').config();

const requireAuthMember = (req, res, next) => {
    const token = req.cookies.jwt;

    if (!token) {
        return res.status(400).json({ message: 'no token found' });
    }

    // Check JWT exist & is verified
    jwt.verify(token, (process.env.SECRET_STRING), (err) => {
        if (err) {
            return res.status(400).json({ message: 'This is JWT error message' });
        }
        // console.log(decodedToken);
        return next();
    });
    return 0;
};

const requireAuthAdmin = (req, res, next) => {
    const token = req.cookies.jwt;

    if (!token) {
        return res.status(400).json({ message: 'no token found' });
    }

    // Check JWT exist & is verified
    jwt.verify(token, (process.env.SECRET_STRING_ADMIN), (err) => {
        if (err) {
            return res.status(400).json({ message: "You don't have authority to access this request." });
        }
        // console.log(decodedToken);
        return next();
    });
    return 0;
};

module.exports = { requireAuthMember, requireAuthAdmin };
