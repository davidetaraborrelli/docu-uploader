import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import { toast } from "sonner"

import Navbar from "../components/Navbar"
import { getDocument } from "../services/api"
import type { Document } from "../types/types"

export default function DocumentPage() {
  const { id } = useParams()
  const navigate = useNavigate()

  const [document, setDocument] = useState<Document | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const token = localStorage.getItem("token")
    if (!token) {
      navigate("/")
    }
  }, [navigate])

  useEffect(() => {
    const loadDocument = async () => {
      if (!id) {
        return
      }

      try {
        setLoading(true)
        const doc = await getDocument(Number(id))
        setDocument(doc)
      } catch (error) {
        console.error(error)
        toast.error("Documento non trovato")
        navigate("/dashboard")
      } finally {
        setLoading(false)
      }
    }

    loadDocument()
  }, [id, navigate])

  if (loading) {
    return <p className="p-8">Caricamento...</p>
  }

  if (!document) {
    return <p className="p-8">Documento non trovato</p>
  }

  return (
    <>
      <Navbar />

      <div className="max-w-3xl mx-auto p-8">
        <h1 className="text-2xl font-bold mb-4">
          {document.title}
        </h1>

        <p className="mb-4 text-sm text-gray-500">
          Creato il {new Date(document.createdAt).toLocaleString()}
        </p>

        <div className="border rounded p-4 whitespace-pre-wrap">
          {document.content}
        </div>
      </div>
    </>
  )
}
