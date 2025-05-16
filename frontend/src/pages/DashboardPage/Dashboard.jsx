import { useAuthContext } from '../../context/AuthContext'
import { useNavigate } from 'react-router-dom'

const Dashboard = () => {
  const { logout } = useAuthContext()
  const navigate = useNavigate()

    const handleLogout = () => {
    logout()
    navigate('/')
  }

    return (
      <div>
        <h1>Welcome!</h1>
        <p>You are now logged in to the clinic system.</p>
        <button onClick={handleLogout}>Log out</button>
      </div>
    );
  };
  
  export default Dashboard;
  