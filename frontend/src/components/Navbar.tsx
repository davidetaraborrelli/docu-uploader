import { useNavigate } from "react-router-dom"
import { Button } from "./ui/button"

export default function Navbar() {

  const navigate = useNavigate()

  const handleLogout = () => {
    localStorage.removeItem("token")
    navigate("/")
  }

  return (

    <div className="border-b">

      <div className="max-w-5xl mx-auto flex items-center justify-between p-4">

        <h1
          className="text-xl font-bold cursor-pointer"
          onClick={() => navigate("/dashboard")}
        >
          DocPlatform
        </h1>

        <div className="flex gap-2">

          <Button
            variant="ghost"
            onClick={() => navigate("/dashboard")}
          >
            Dashboard
          </Button>

          <Button
            variant="ghost"
            onClick={() => navigate("/upload")}
          >
            Upload
          </Button>

          <Button
            variant="ghost"
            onClick={() => navigate("/search")}
          >
            Search
          </Button>

          <Button
            variant="destructive"
            onClick={handleLogout}
          >
            Logout
          </Button>

        </div>

      </div>

    </div>
  )
}