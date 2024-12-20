# Use the official Node.js image
FROM node:18 AS build

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the Angular application
RUN npm run build --prod

# Use Nginx to serve the application
FROM nginx:alpine

# Copy the built application from the previous stage
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html

# Expose the port Nginx is running on
EXPOSE 80