FROM node:14-alpine
WORKDIR /app
COPY package.json ./
# RUN npm cache clean --force
RUN npm install --no-package-lock
COPY ./ ./
EXPOSE 3000
CMD ["npm", "start"]
