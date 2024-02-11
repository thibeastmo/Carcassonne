FROM node:18-alpine AS build

#Set the working directory inside the container
WORKDIR /app

#Copy package.json and package-lock.json to the working directory
COPY package.json /app/package.json

#Install dependencies
RUN npm install

#Copy the entire project to the working directory
COPY . /app

#Build the React app
RUN npm run build

#Use Nginx as the base image for serving the static files
FROM nginx:1.21.6-alpine
#add nginx config
COPY ngnix.conf /etc/nginx/conf.d/default.conf
#Copy the built React app from the previous stage to the nginx directory
COPY --from=build /app/dist /usr/share/nginx/html
COPY env.sh /docker-entrypoint.d/env.sh
RUN chmod +x /docker-entrypoint.d/env.sh
#Expose port 80
EXPOSE 80

#The container runs the Nginx server to serve the React app
#CMD ["nginx", "-g", "daemon off;"]