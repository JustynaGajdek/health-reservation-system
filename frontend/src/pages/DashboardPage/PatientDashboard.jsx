import { useAuthContext } from "../../context/AuthContext";
import PatientNavbar from "../../components/layout/PatientNavbar";
import PatientSidebar from "../../components/layout/PatientSidebar";
import AppointmentCard from "../../components/appointments/AppointmentCard";
import { useNavigate } from 'react-router-dom';

const PatientDashboard = () => {
  const { user } = useAuthContext();
  const nav = useNavigate();

  return (
    <div className="dashboard-wrapper">
      <PatientSidebar />
      <div className="flex-grow-1">
        <PatientNavbar />
        <main className="dashboard-main">
          <h1 className="dashboard-header">
            Welcome, {user?.firstName || "Patient"}!
          </h1>

          <div className="d-flex flex-column align-items-center">
            <AppointmentCard />
            <button
              className="btn btn-primary w-100 mt-4"
              onClick={() => nav('/book-visit')}
            >
              Book Another Visit
            </button>
          </div>
        </main>
      </div>
    </div>
  );
};

export default PatientDashboard;
