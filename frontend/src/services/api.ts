import type { Document, LoginResponse } from "../types/types"

// 🔹 MOCK DATABASE
let documents: Document[] = [
  {
    id: "1",
    filename: "documento1.txt",
    uploadDate: "2026-03-05",
    status: "Indicizzato",
    content: "Questo è il contenuto del documento 1."
  },
  {
    id: "2",
    filename: "report.txt",
    uploadDate: "2026-03-04",
    status: "In elaborazione",
    content: "Questo è il contenuto del report."
  }
]

/* =========================
   AUTH
========================= */

export async function login(
  email: string,
  password: string
): Promise<LoginResponse> {

  await new Promise((resolve) => setTimeout(resolve, 500))

  if (email === "test@test.com" && password === "daje") {
    return { token: "fake-jwt-token" }
  }

  throw new Error("Credenziali non valide")
}

/* =========================
   DOCUMENTS
========================= */

// 🔹 tutti i documenti
export async function getDocuments(): Promise<Document[]> {

  await new Promise((resolve) => setTimeout(resolve, 300))

  return documents
}

// 🔹 singolo documento
export async function getDocument(id: string): Promise<Document | undefined> {

  await new Promise((resolve) => setTimeout(resolve, 200))

  return documents.find((doc) => doc.id === id)
}

// 🔹 upload
export async function uploadDocument(file: File): Promise<Document> {

  await new Promise((resolve) => setTimeout(resolve, 500))

  let content = ""

  // solo txt leggibile
  if (file.type === "text/plain") {
    content = await file.text()
  } else {
    content = "Anteprima non disponibile per questo tipo di file"
  }

  const newDocument: Document = {
    id: Date.now().toString(),
    filename: file.name,
    uploadDate: new Date().toISOString().split("T")[0],
    status: "In elaborazione",
    content
  }

  documents.push(newDocument)

  return newDocument
}

// 🔹 modifica contenuto
export async function updateDocument(
  id: string,
  content: string
): Promise<void> {

  await new Promise((resolve) => setTimeout(resolve, 300))

  const doc = documents.find((d) => d.id === id)

  if (doc) {
    doc.content = content
  }
}

// 🔹 elimina
export async function deleteDocument(id: string): Promise<void> {

  await new Promise((resolve) => setTimeout(resolve, 300))

  documents = documents.filter((doc) => doc.id !== id)
}

/* =========================
   SEARCH
========================= */

export async function searchDocuments(query: string): Promise<Document[]> {

  await new Promise((resolve) => setTimeout(resolve, 300))

  if (!query) return []

  const lowerQuery = query.toLowerCase()

  return documents.filter((doc) =>
    doc.filename.toLowerCase().includes(lowerQuery) ||
    doc.content.toLowerCase().includes(lowerQuery)
  )
}