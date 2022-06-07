const jwt = require('jsonwebtoken');
require('dotenv').config();
// const db = require('./database');
// const tokenCompare = require('./handler');

const requireAuth = (req, res, next) => {
    const token = req.cookies.jwt;
    console.log(token);

    if (!token) {
        return res.status(400).json({ message: 'no token found' });
    }

    // Check JWT exist & is verified
    jwt.verify(token, process.env.SECRET_STRING, (err, decodedToken) => {
        if (err) {
            return res.status(400).json({ message: 'This is JWT error message' });
        }
        console.log(decodedToken);
        return next();
    });
};

// const checkUser = (req, res, next) => {
//     const token = req.cookies.jwt;

//     if (token) {
//         jwt.verify(token, 'anticede secret string', async (err, decodedToken) => {
//             if (err) {
//                 console.log('This is error of checkUser');
//                 // res.status(400).json({ message: 'This is JWT error message' });
//                 next();
//             }
//             console.log(decodedToken);
//             const [rows] = await db.promise().query('SELECT * FROM users WHERE id = ?', [decodedToken.id]);
//             res.locals.user = rows;
//             return next();
//         });
//     }
// };

// const createTokenCompare = (req, res, next) => {
//     const tokenCompare = createToken(rows[0].id);
//     const [rows]
// };

module.exports = { requireAuth };
