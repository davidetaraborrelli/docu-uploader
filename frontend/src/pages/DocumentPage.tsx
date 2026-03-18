import { useEffect, useState } from "react"
import { useParams } from "react-router-dom"
import Navbar from "../components/Navbar"
import { getDocument } from "../services/api"
import type { Document } from "../types/types"

export default function DocumentPage() {

  const { id } = useParams()
  const [document, setDocument] = useState<Document | null>(null)

  useEffect(() => {
    if (!id) return

    getDocument(id).then((doc) => {
      if (doc) setDocument(doc)
    })
  }, [id])

  if (!document) {
    return <p className="p-8">Caricamento...</p>
  }

  return (
    <>
      <Navbar />

      <div className="max-w-3xl mx-auto p-8">

        <h1 className="text-2xl font-bold mb-4">
          {document.filename}
        </h1>

        <div className="border rounded p-4 whitespace-pre-wrap">
          {document.content}
        </div>

      </div>
    </>
  )
}