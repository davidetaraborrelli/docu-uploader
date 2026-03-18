import { BrowserRouter, Routes, Route } from "react-router-dom"
import LoginPage from "./pages/LoginPage"
import DashboardPage from "./pages/DashboardPage"
import DocumentPage from "./pages/DocumentPage"
import EditDocumentPage from "./pages/EditDocumentPage"
import SearchPage from "./pages/SearchPage"


function App() {
  return (
    <BrowserRouter>
      <Routes>

        <Route path="/" element={<LoginPage />} />
        <Route path="/dashboard" element={<DashboardPage/>} />
        <Route path="/documents/:id" element={<DocumentPage />} />
        <Route path="/documents/:id/edit" element={<EditDocumentPage />} />
        <Route path="/search" element={<SearchPage />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
