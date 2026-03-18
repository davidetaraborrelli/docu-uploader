import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"

import { deleteDocument, getDocuments, uploadDocument } from "../services/api"
import type { Document } from "../types/types"

import { Button } from "../components/ui/button"
import Navbar from "../components/Navbar"
import { Input } from "../components/ui/input"
import { toast } from "sonner"

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

  const navigate = useNavigate()

  const loadDocuments = async () => {
    const data = await getDocuments()
    setDocuments(data)
  }

  useEffect(() => {
    loadDocuments()
  }, [])

  const handleDelete = async (id: string) => {
    await deleteDocument(id)
    loadDocuments()
  }

  const handleUpload = async () => {

    if (!file) {
      toast.error("Seleziona un file")
      return
    }

    const allowedTypes = [
      "text/plain",
      "application/pdf",
      "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
    ]

    if (!allowedTypes.includes(file.type)) {
      toast.error("Solo file .txt, .pdf, .docx")
      return
    }

    await uploadDocument(file)

    toast.success("Upload completato")

    setFile(null)
    loadDocuments()
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
                  accept=".txt,.pdf,.docx"
                  onChange={(e) => {
                    if (e.target.files) {
                      setFile(e.target.files[0])
                    }
                  }}
                />

                <Button onClick={handleUpload} className="w-full">
                  Upload
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
                  {doc.filename}
                </TableCell>

                <TableCell>{doc.uploadDate}</TableCell>


                <TableCell className="text-right space-x-2">

                  <Button size="sm" onClick={() => navigate(`/documents/${doc.id}/edit`)}>
                    Modifica
                  </Button>

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