FROM node:16

WORKDIR /usr/src/app

COPY package*.json ./
COPY . .

RUN npm ci --only=production
RUN apt-get -y update && apt-get -y install ffmpeg

EXPOSE 8080

CMD [ "node", "./src/server.js" ]