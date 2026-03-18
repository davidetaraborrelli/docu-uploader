import { useState } from "react"
import { useNavigate } from "react-router-dom"

import Navbar from "../components/Navbar"
import { Input } from "../components/ui/input"
import { Button } from "../components/ui/button"

import { searchDocuments } from "../services/api"
import type { Document } from "../types/types"

export default function SearchPage() {

  const [query, setQuery] = useState("")
  const [results, setResults] = useState<Document[]>([])
  const navigate = useNavigate()

  const handleSearch = async () => {
    const data = await searchDocuments(query)
    setResults(data)
  }

  return (
    <>
      <Navbar />

      <div className="max-w-3xl mx-auto p-8">

        <h1 className="text-2xl font-bold mb-6">
          Ricerca documenti
        </h1>

        <div className="flex gap-2 mb-6">

          <Input
            placeholder="Cerca documento..."
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            onKeyDown={(e) => {
              if (e.key === "Enter") handleSearch()
            }}
          />

          <Button onClick={handleSearch}>
            Cerca
          </Button>

        </div>

        <div className="space-y-3">

          {results.length === 0 && (
            <p className="text-gray-500">
              Nessun risultato
            </p>
          )}

          {results.map((doc) => (

            <div
              key={doc.id}
              className="border p-4 rounded cursor-pointer hover:bg-gray-50"
              onClick={() => navigate(`/documents/${doc.id}`)}
            >
              <p className="font-medium">{doc.filename}</p>
              <p className="text-sm text-gray-500 line-clamp-2">
                {doc.content}
              </p>
            </div>

          ))}

        </div>

      </div>
    </>
  )
}