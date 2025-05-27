import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import HomePage from "./pages/HomePage/HomePage";
import LoginPage from "./pages/LoginPage/LoginPage";
import RegisterPage from "./pages/RegisterPage/RegisterPage";
import Dashboard from "./pages/DashboardPage/Dashboard";
import PatientDashboard from "./pages/DashboardPage/PatientDashboard";

import PrivateRoute from "./components/PrivateRoute";
import ReceptionistLayout from "./components/layout/ReceptionistLayout";

import ReceptionistPage from "./pages/ReceptionistPage/ReceptionistPage";
import ApproveUsers from "./pages/ReceptionistPage/ApproveUsers";
import PatientList from "./pages/ReceptionistPage/PatientList";
import PatientDetails from "./pages/ReceptionistPage/PatientDetails";
import AddPatientPage from "./pages/ReceptionistPage/AddPatientPage";
import AppointmentsList from "./components/appointments/AppointmentsList";
import BookAppointment from "./pages/ReceptionistPage/BookAppointment";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        {/* Public */}
        <Route path="/" element={<HomePage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />

        {/* Common protected */}
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

        {/* Receptionist area */}
        <Route
          path="/dashboard/receptionist/*"
          element={
            <PrivateRoute allowedRoles={["RECEPTIONIST"]}>
              <ReceptionistLayout />
            </PrivateRoute>
          }
        >
          {/* Nested under /dashboard/receptionist */}
          <Route index element={<ReceptionistPage />} />
          <Route path="approve" element={<ApproveUsers />} />
          <Route path="patients" element={<PatientList />} />
          <Route path="patients/:id" element={<PatientDetails />} />
          <Route path="add-patient" element={<AddPatientPage />} />
          <Route path="appointments" element={<AppointmentsList />} />
          <Route path="add-appointment" element={<BookAppointment />} />

          {/* Fallback for unknown nested routes */}
          <Route
            path="*"
            element={<Navigate to="/dashboard/receptionist" replace />}
          />
        </Route>

        {/* Global 404 */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>

      <ToastContainer position="top-center" autoClose={3000} />
    </BrowserRouter>
  );
}

export default App;
