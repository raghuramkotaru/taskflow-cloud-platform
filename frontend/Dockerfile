# --- Build stage ---
FROM node:20-alpine AS build
WORKDIR /app
COPY package.json package-lock.json* ./
RUN npm install
COPY . .
# Vite bakes these in at build time; the browser reaches the services on the host.
ARG VITE_CORE_API=http://localhost:8080
ARG VITE_AI_API=http://localhost:8000
ENV VITE_CORE_API=$VITE_CORE_API
ENV VITE_AI_API=$VITE_AI_API
RUN npm run build

# --- Runtime stage ---
FROM nginx:1.27-alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
