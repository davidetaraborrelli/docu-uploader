import type {
  CurrentUser,
  Document,
  LoginResponse,
  Notification,
  SearchDocument,
  SearchResponse,
} from "../types/types"

const BASE_URL = "/api"

type ApiDocument = {
  id: number
  title: string
  content: string | null
  createdAt: string
  updatedAt: string
}

function getToken(): string | null {
  return localStorage.getItem("token")
}

function getAuthHeaders(): HeadersInit {
  const token = getToken()

  return {
    "Content-Type": "application/json",
    ...(token ? { Authorization: `Bearer ${token}` } : {}),
  }
}

async function getErrorMessage(response: Response, fallbackMessage: string) {
  try {
    const data = await response.json()

    if (typeof data?.error === "string") {
      return data.error
    }
  } catch {
    // Ignore invalid JSON and use fallback below.
  }

  return fallbackMessage
}

function mapDocument(document: ApiDocument): Document {
  return {
    id: document.id,
    title: document.title,
    content: document.content ?? "",
    createdAt: document.createdAt,
    updatedAt: document.updatedAt,
  }
}

export async function login(username: string, password: string): Promise<LoginResponse> {
  const response = await fetch(`${BASE_URL}/auth/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ username, password }),
  })

  if (!response.ok) {
    throw new Error(await getErrorMessage(response, "Login fallito"))
  }

  const data = (await response.json()) as LoginResponse
  localStorage.setItem("token", data.token)

  return data
}

export async function register(
  username: string,
  email: string,
  password: string,
): Promise<LoginResponse> {
  const response = await fetch(`${BASE_URL}/auth/register`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ username, email, password }),
  })

  if (!response.ok) {
    throw new Error(await getErrorMessage(response, "Registrazione fallita"))
  }

  return response.json()
}

export async function getCurrentUser(): Promise<CurrentUser> {
  const response = await fetch(`${BASE_URL}/users/me`, {
    headers: getAuthHeaders(),
  })

  if (!response.ok) {
    throw new Error(await getErrorMessage(response, "Errore recupero utente"))
  }

  return response.json()
}

export async function getDocuments(): Promise<Document[]> {
  const response = await fetch(`${BASE_URL}/documents`, {
    headers: getAuthHeaders(),
  })

  if (!response.ok) {
    throw new Error(await getErrorMessage(response, "Errore caricamento documenti"))
  }

  const data = (await response.json()) as ApiDocument[]
  return data.map(mapDocument)
}

export async function getDocument(id: number): Promise<Document> {
  const response = await fetch(`${BASE_URL}/documents/${id}`, {
    headers: getAuthHeaders(),
  })

  if (!response.ok) {
    throw new Error(await getErrorMessage(response, "Documento non trovato"))
  }

  const data = (await response.json()) as ApiDocument
  return mapDocument(data)
}

export async function uploadDocument(title: string, content: string): Promise<Document> {
  const response = await fetch(`${BASE_URL}/documents`, {
    method: "POST",
    headers: getAuthHeaders(),
    body: JSON.stringify({
      title,
      content,
    }),
  })

  if (!response.ok) {
    throw new Error(await getErrorMessage(response, "Errore upload documento"))
  }

  const data = (await response.json()) as ApiDocument
  return mapDocument(data)
}

export async function deleteDocument(id: number): Promise<void> {
  const response = await fetch(`${BASE_URL}/documents/${id}`, {
    method: "DELETE",
    headers: getAuthHeaders(),
  })

  if (!response.ok) {
    throw new Error(await getErrorMessage(response, "Errore eliminazione documento"))
  }
}

export async function searchDocuments(query: string, limit = 10): Promise<SearchDocument[]> {
  const params = new URLSearchParams({
    q: query,
    limit: String(limit),
  })

  const response = await fetch(`${BASE_URL}/search?${params.toString()}`, {
    method: "POST",
    headers: getAuthHeaders(),
  })

  if (!response.ok) {
    throw new Error(await getErrorMessage(response, "Errore nella ricerca"))
  }

  const data = (await response.json()) as SearchResponse

  return data.results.map((result) => ({
    id: result.documentId,
    title: result.title,
    score: result.score,
    userId: result.userId,
  }))
}

export async function getNotifications(): Promise<Notification[]> {
  const response = await fetch(`${BASE_URL}/notifications`, {
    headers: getAuthHeaders(),
  })

  if (!response.ok) {
    throw new Error(await getErrorMessage(response, "Errore caricamento notifiche"))
  }

  return response.json()
}

export function logout() {
  localStorage.removeItem("token")
}
