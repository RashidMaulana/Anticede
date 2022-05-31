FROM node:16

WORKDIR /usr/src/app

COPY package*.json ./
COPY . .

RUN npm ci --only=production
RUN apt-get update && apt-get install -y ffmpeg

EXPOSE 8080

CMD [ "node", "./src/server.js" ]