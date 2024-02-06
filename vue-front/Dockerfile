# Use an official Node image as the base image
FROM node:20.7.0
 
WORKDIR /app

# Copy the package to install the node's modules to the container
COPY package.json package.json
COPY package-lock.json package-lock.json

# Install the node's modules
RUN npm install

# Copy the sources to the container
COPY . .

# Expose the port this application is listen on
EXPOSE 8080

# Set the command to run the application
CMD [ "npm", "run", "serve" ]