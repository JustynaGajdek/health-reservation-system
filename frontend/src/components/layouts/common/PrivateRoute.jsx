import { Navigate } from 'react-router-dom'
import { useAuthContext } from '../../../context/AuthContext'

const PrivateRoute = ({ children }) => {
  const { token, loading } = useAuthContext()

  if (loading) return <p>Loading...</p>

  return token ? children : <Navigate to="/login" replace />
}

export default PrivateRoute
