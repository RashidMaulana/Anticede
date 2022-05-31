const jwt = require('jsonwebtoken');

const requireAuth = (req, res, next) => {
    const token = req.cookies.jwt;

    // Check JWT exist & IS verified
    if (token) {
        jwt.verify(token, 'anticede secret string', (err, decodedToken) => {
            if (err) {
                return res.status(400).json({ message: 'This is JWT error message' });
            }
            console.log(decodedToken);
            return next();
        });
    }
    return res.status(400).json({ message: 'You dont have the authorization to access this request, please login first!' });
};

module.exports = { requireAuth };
