const jwt = require('jsonwebtoken');

const requireAuth = (req, res, next) => {
    const token = req.cookies.jwt;

    // Check JWT exist & is verified
    if (token) {
        console.log(token);
        jwt.verify(token, 'anticede secret string', (err, decodedToken) => {
            if (err) {
                return res.status(400).json({ message: 'This is JWT error message' });
            }
            console.log(decodedToken);
            return next();
        });
    }
    return res.status(400).json({
        message: 'Token is not detected.',
        token_jwt: token,
    });
};

// const checkUser = (req, res, next) => {
//     const token = req.cookies.jwt;

//     if (token) {
//         jwt.verify(token, 'anticede secret string', async (err, decodedToken) => {
//             if (err) {
//                 console.log('This is error of checkUser');
//                 // res.status(400).json({ message: 'This is JWT error message' });
//                 return next();
//             }
//             console.log(decodedToken);
//             const [rows] = await db.promise().query('SELECT * FROM users WHERE id = ?', [decodedToken.id]);
//             res.locals.user = rows;
//             return next();
//         });
//     }
// };

module.exports = { requireAuth };
