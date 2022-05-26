FROM node:16

WORKDIR /usr/src/app

COPY package*.json ./
COPY . .

RUN npm ci --only=production

EXPOSE 8080

CMD [ "node", "./src/server.js" ]