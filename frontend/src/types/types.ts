export interface Document {
  id: number
  title: string
  content: string
  createdAt: string
  updatedAt: string
}

export interface LoginResponse {
  token: string
  username: string
  email: string
}

export interface CurrentUser {
  id: number
  username: string
  email: string
}

export interface SearchResultItem {
  documentId: number
  title: string
  score: number
  userId: number
}

export interface SearchResponse {
  query: string
  totalResults: number
  results: SearchResultItem[]
}

export interface SearchDocument {
  id: number
  title: string
  score: number
  userId: number
}

export interface Notification {
  id: number
  type: string
  message: string
  read: boolean
  createdAt: string
}
