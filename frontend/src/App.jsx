import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import "./styles/globals.css";
import HomePage from "./pages/HomePage/HomePage";
import LoginPage from "./pages/LoginPage/LoginPage";
import Dashboard from "./pages/DashboardPage/Dashboard";
import PrivateRoute from "./components/PrivateRoute";
import RegisterPage from "./pages/RegisterPage/RegisterPage";
import PatientDashboard from "./pages/DashboardPage/PatientDashboard";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route
          path="/dashboard"
          element={
            <PrivateRoute>
              <Dashboard />
            </PrivateRoute>
          }
        />
        <Route
          path="/dashboard/patient"
          element={
            <PrivateRoute>
              <PatientDashboard />
            </PrivateRoute>
          }
        />
      </Routes>
    </Router>
  );
}

export default App;
