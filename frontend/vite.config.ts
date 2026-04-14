import path from "path"
import tailwindcss from "@tailwindcss/vite"
import react from "@vitejs/plugin-react"
import { defineConfig } from "vite"

export default defineConfig({
  plugins: [react(), tailwindcss()],
  server: {
    proxy: {
      "/api/auth": "http://localhost:8081",
      "/api/users": "http://localhost:8081",
      "/api/documents": "http://localhost:8082",
      "/api/notifications": "http://localhost:8083",
      "/api/search": "http://localhost:8084",
    },
  },
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
})
