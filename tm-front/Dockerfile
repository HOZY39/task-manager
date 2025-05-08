FROM node:18 AS build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build --prod

FROM nginx:alpine
COPY --from=build-stage /app/dist/tm-front/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf