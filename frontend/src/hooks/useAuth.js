import { useState, useEffect } from 'react'
import { login as loginService } from '../services/auth.service'

export default function useAuth() {
  const [token, setToken] = useState(() => localStorage.getItem('token'))
  const [user, setUser]   = useState(null)
  const [loading, setLoading] = useState(!!token)

  useEffect(() => {
    const loadUser = async () => {
      try {
        const { data } = await fetchProfile()
        setUser(data)
      } catch (error) {
        console.error('Profile loading failed:', error)
        logout() 
      } finally {
        setLoading(false)
      }
    }

    if (token) {
      loadUser()
    } else {
      setLoading(false)
    }
  }, [token])

  const login = async credentials => {
    const { data } = await loginService(credentials)
    localStorage.setItem('token', data.token)
    setToken(data.token)
  }

  const logout = () => {
    localStorage.removeItem('token')
    setToken(null)
    setUser(null)
  }

  return { user, token, loading, login, logout }
}