import React, { createContext, useContext } from 'react'
import useAuth from '../hooks/useAuth'

const AuthContext = createContext(null)

export const AuthProvider = ({ children }) => {
  const auth = useAuth()
  return (
    <AuthContext.Provider value={auth}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuthContext = () => {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuthContext must be inside AuthProvider')
  return ctx
}
