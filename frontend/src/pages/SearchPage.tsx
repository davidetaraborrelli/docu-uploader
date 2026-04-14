import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { toast } from "sonner"

import Navbar from "../components/Navbar"
import { Button } from "../components/ui/button"
import { Input } from "../components/ui/input"
import { getCurrentUser, searchDocuments } from "../services/api"
import type { SearchDocument } from "../types/types"

export default function SearchPage() {
  const [query, setQuery] = useState("")
  const [results, setResults] = useState<SearchDocument[]>([])
  const [loading, setLoading] = useState(false)
  const [currentUserId, setCurrentUserId] = useState<number | null>(null)

  const navigate = useNavigate()

  useEffect(() => {
    const token = localStorage.getItem("token")
    if (!token) {
      navigate("/")
      return
    }

    const loadCurrentUser = async () => {
      try {
        const user = await getCurrentUser()
        setCurrentUserId(user.id)
      } catch (error) {
        console.error(error)
        toast.error("Sessione non valida")
        localStorage.removeItem("token")
        navigate("/")
      }
    }

    loadCurrentUser()
  }, [navigate])

  const handleSearch = async () => {
    if (!query.trim()) {
      toast.error("Inserisci un testo di ricerca")
      return
    }

    try {
      setLoading(true)

      const data = await searchDocuments(query)
      const filteredResults = currentUserId === null
        ? data
        : data.filter((document) => document.userId === currentUserId)

      setResults(filteredResults)
    } catch (error) {
      console.error(error)
      toast.error("Errore nella ricerca")
    } finally {
      setLoading(false)
    }
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
              if (e.key === "Enter") {
                handleSearch()
              }
            }}
          />

          <Button onClick={handleSearch} disabled={loading}>
            {loading ? "Ricerca..." : "Cerca"}
          </Button>
        </div>

        <div className="space-y-3">
          {!loading && results.length === 0 && (
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
              <p className="font-medium">{doc.title}</p>
              <p className="text-sm text-gray-500 line-clamp-2">
                Rilevanza: {doc.score.toFixed(3)}
              </p>
            </div>
          ))}
        </div>
      </div>
    </>
  )
}
