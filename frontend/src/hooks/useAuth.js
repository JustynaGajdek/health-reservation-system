import { useState, useEffect } from 'react'
import { login as loginService } from '../services/auth.service'

export default function useAuth() {
  const [token, setToken] = useState(() => localStorage.getItem('token'))
  const [user, setUser]   = useState(null)

  useEffect(() => {
    if (token) {
      setUser({ /* dane z tokena */ })
    }
  }, [token])

  const login = async creds => {
    const { data } = await loginService(creds)
    setToken(data.token)
    localStorage.setItem('token', data.token)
    setUser(data.user)
  }

  const logout = () => {
    setToken(null)
    localStorage.removeItem('token')
    setUser(null)
  }

  return { user, token, login, logout }
}
