import { useState } from "react"
import { useNavigate } from "react-router-dom"
import { toast } from "sonner"

import { Button } from "../components/ui/button"
import { Input } from "../components/ui/input"
import { login } from "../services/api"

export default function LoginPage() {
  const [username, setUsername] = useState("")
  const [password, setPassword] = useState("")
  const [loading, setLoading] = useState(false)

  const navigate = useNavigate()

  const handleLogin = async () => {
    if (!username || !password) {
      toast.error("Inserisci username e password")
      return
    }

    try {
      setLoading(true)
      await login(username, password)
      navigate("/dashboard")
    } catch (error: unknown) {
      console.error(error)
      const message = error instanceof Error ? error.message : "Credenziali non valide"
      toast.error(message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="flex h-screen items-center justify-center">
      <div className="w-87.5 space-y-4">
        <h1 className="text-2xl font-bold text-center">
          Login
        </h1>

        <Input
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />

        <Input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <Button
          className="w-full"
          onClick={handleLogin}
          disabled={loading}
        >
          {loading ? "Accesso..." : "Login"}
        </Button>
      </div>
    </div>
  )
}
