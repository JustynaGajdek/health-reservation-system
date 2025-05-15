import { BrowserRouter as Router, Routes, Route, useNavigate } from "react-router-dom";
import "./App.css";
import HomePage from "./pages/HomePage/HomePage";
import LoginPage from "./pages/LoginPage/LoginPage";
import Dashboard from "./pages/DashboardPage/Dashboard";
import PrivateRoute from './components/PrivateRoute';

function App() {

  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route
         path="/dashboard"
          element={
          <PrivateRoute>
            <Dashboard />
          </PrivateRoute>
        }
      />
      </Routes>
    </Router>
  );
}

export default App;
