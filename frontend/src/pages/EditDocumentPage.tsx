import { useEffect, useState } from "react"
import { useParams, useNavigate } from "react-router-dom"

import Navbar from "../components/Navbar"
import { getDocument, updateDocument } from "../services/api"
import { Button } from "../components/ui/button"

export default function EditDocumentPage() {

  const { id } = useParams()
  const navigate = useNavigate()

  const [content, setContent] = useState("")

  useEffect(() => {
    if (!id) return

    getDocument(id).then((doc) => {
      if (doc) setContent(doc.content)
    })
  }, [id])

  const handleSave = async () => {
    if (!id) return

    await updateDocument(id, content)

    navigate("/dashboard")
  }

  return (
    <>
      <Navbar />

      <div className="max-w-3xl mx-auto p-8">

        <h1 className="text-2xl font-bold mb-6">
          Modifica documento
        </h1>

        <textarea
          className="w-full border rounded p-3 h-64"
          value={content}
          onChange={(e) => setContent(e.target.value)}
        />

        <Button className="mt-4" onClick={handleSave}>
          Salva modifiche
        </Button>

      </div>
    </>
  )
}