import { useState } from "react"
import { useNavigate } from "react-router-dom"

import { Button } from "../components/ui/button"
import { Input } from "../components/ui/input"
import { login } from "../services/api"

export default function LoginPage() {

  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const navigate = useNavigate()

  const handleLogin = async () => {
    try {
      await login(email, password)
      navigate("/dashboard")
    } catch {
      alert("Credenziali non valide")
    }
  }

  return (
    <div className="flex h-screen items-center justify-center">

      <div className="w-87.5 space-y-4">

        <h1 className="text-2xl font-bold text-center">
          Login
        </h1>

        <Input
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <Input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <Button className="w-full" onClick={handleLogin}>
          Login
        </Button>

      </div>

    </div>
  )
}