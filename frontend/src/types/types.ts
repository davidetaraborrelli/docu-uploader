export interface Document {
  id: string
  filename: string
  uploadDate: string
  content: string
}

export interface LoginResponse {
  token: string
}