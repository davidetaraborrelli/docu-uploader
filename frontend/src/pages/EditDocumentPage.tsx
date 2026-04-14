import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import { toast } from "sonner"

import Navbar from "../components/Navbar"
import { getDocument } from "../services/api"
import { Button } from "../components/ui/button"

export default function EditDocumentPage() {
  const { id } = useParams()
  const navigate = useNavigate()

  const [content, setContent] = useState("")
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)

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
        setContent(doc.content)
      } catch (error) {
        console.error(error)
        toast.error("Errore caricamento documento")
        navigate("/dashboard")
      } finally {
        setLoading(false)
      }
    }

    loadDocument()
  }, [id, navigate])

  const handleSave = async () => {
    setSaving(true)
    toast.error("Il backend non supporta ancora la modifica dei documenti")
    setSaving(false)
  }

  if (loading) {
    return <p className="p-8">Caricamento...</p>
  }

  return (
    <>
      <Navbar />

      <div className="max-w-3xl mx-auto p-8">
        <h1 className="text-2xl font-bold mb-6">
          Modifica documento
        </h1>

        <p className="mb-4 text-sm text-gray-500">
          Il documento viene letto dal microservizio reale, ma l'endpoint di aggiornamento non esiste ancora.
        </p>

        <textarea
          className="w-full border rounded p-3 h-64"
          value={content}
          onChange={(e) => setContent(e.target.value)}
        />

        <Button
          className="mt-4"
          onClick={handleSave}
          disabled={saving}
        >
          {saving ? "Verifica..." : "Salvataggio non disponibile"}
        </Button>
      </div>
    </>
  )
}
