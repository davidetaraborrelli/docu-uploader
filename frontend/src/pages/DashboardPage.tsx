import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { toast } from "sonner"

import { deleteDocument, getDocuments, uploadDocument } from "../services/api"
import type { Document } from "../types/types"

import { Button } from "../components/ui/button"
import Navbar from "../components/Navbar"
import { Input } from "../components/ui/input"

import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "../components/ui/dialog"

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "../components/ui/table"

export default function DashboardPage() {
  const [documents, setDocuments] = useState<Document[]>([])
  const [file, setFile] = useState<File | null>(null)
  const [loading, setLoading] = useState(false)

  const navigate = useNavigate()

  useEffect(() => {
    const token = localStorage.getItem("token")
    if (!token) {
      navigate("/")
    }
  }, [navigate])

  const loadDocuments = async () => {
    try {
      setLoading(true)
      const data = await getDocuments()
      setDocuments(data)
    } catch (error) {
      console.error(error)
      toast.error("Errore nel caricamento documenti")
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadDocuments()
  }, [])

  const handleDelete = async (id: number) => {
    try {
      await deleteDocument(id)
      toast.success("Documento eliminato")
      loadDocuments()
    } catch (error) {
      console.error(error)
      toast.error("Errore eliminazione")
    }
  }

  const handleUpload = async () => {
    if (!file) {
      toast.error("Seleziona un file")
      return
    }

    if (file.type !== "text/plain") {
      toast.error("Il backend accetta solo contenuto testuale: usa file .txt")
      return
    }

    try {
      setLoading(true)
      const content = await file.text()

      await uploadDocument(file.name, content)

      toast.success("Upload completato")
      setFile(null)
      loadDocuments()
    } catch (error) {
      console.error(error)
      toast.error("Errore upload")
    } finally {
      setLoading(false)
    }
  }

  return (
    <>
      <Navbar />

      <div className="max-w-5xl mx-auto p-8">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-3xl font-bold">
            Documenti
          </h1>

          <Dialog>
            <DialogTrigger asChild>
              <Button>Upload documento</Button>
            </DialogTrigger>

            <DialogContent>
              <DialogHeader>
                <DialogTitle>Upload documento</DialogTitle>
              </DialogHeader>

              <div className="space-y-4">
                <Input
                  type="file"
                  accept=".txt"
                  onChange={(e) => {
                    if (e.target.files) {
                      setFile(e.target.files[0])
                    }
                  }}
                />

                <Button onClick={handleUpload} className="w-full" disabled={loading}>
                  {loading ? "Caricamento..." : "Upload"}
                </Button>
              </div>
            </DialogContent>
          </Dialog>
        </div>

        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Nome file</TableHead>
              <TableHead>Data upload</TableHead>
              <TableHead className="text-right">Azioni</TableHead>
            </TableRow>
          </TableHeader>

          <TableBody>
            {documents.map((doc) => (
              <TableRow key={doc.id}>
                <TableCell
                  className="cursor-pointer text-blue-600"
                  onClick={() => navigate(`/documents/${doc.id}`)}
                >
                  {doc.title}
                </TableCell>

                <TableCell>{new Date(doc.createdAt).toLocaleString()}</TableCell>

                <TableCell className="text-right space-x-2">
                  <Button
                    variant="destructive"
                    size="sm"
                    onClick={() => handleDelete(doc.id)}
                  >
                    Elimina
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
    </>
  )
}
