import { Routes, Route } from "react-router-dom";
import PatientDashboard from "./PatientDashboard";
import ReceptionistDashboard from "../ReceptionistPage/ReceptionistPage";


const Dashboard = () => {
  return (
    <Routes>
      <Route path="patient" element={<PatientDashboard />} />
      <Route path="receptionist" element={<ReceptionistDashboard />} />

    </Routes>
  );
};

export default Dashboard;
