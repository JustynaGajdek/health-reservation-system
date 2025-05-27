import React, { useEffect, useState, useCallback } from "react";
import { useAuthContext } from "../../context/AuthContext";
import { Spinner, Button, Card, Row, Col } from "react-bootstrap";
import { CalendarCheck } from "lucide-react";
import { toast } from "react-toastify";
import { useNavigate } from "react-router-dom";

import AppointmentsList from "../../components/appointments/AppointmentsList";
import {
  getUnassignedAppointments,
  getPendingUsers,
  cancelAppointment as cancelAppt,
} from "../../services/reception.service";

const ReceptionistPage = () => {
  const { user } = useAuthContext();
  const navigate = useNavigate();

  const [appointments, setAppointments] = useState([]);
  const [pendingAccounts, setPendingAccounts] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchData = useCallback(async () => {
    setLoading(true);
    try {
      const [apptData, usersData] = await Promise.all([
        getUnassignedAppointments(),
        getPendingUsers(),
      ]);
      setAppointments(apptData);
      setPendingAccounts(usersData);
    } catch (err) {
      console.error("Receptionist data loading failed", err);
      toast.error("Failed to load dashboard data");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  const handleCancel = async (id) => {
    try {
      await cancelAppt(id);
      toast.success("Appointment cancelled");
      fetchData();
    } catch (err) {
      console.error("Failed to cancel appointment", err);
      toast.error("Error cancelling appointment");
    }
  };

  if (loading) {
    return (
      <div className="text-center mt-5">
        <Spinner animation="border" role="status" />
      </div>
    );
  }

  return (
    <main className="container mt-4">
      <h1 className="mb-4">Welcome, {user?.firstName || "Receptionist"}!</h1>

      <Row className="mb-4">
        <Col md={6}>
          <Card bg="warning" text="white" className="p-3">
            <Card.Title>Accounts to Approve</Card.Title>
            <Card.Text className="fs-2">{pendingAccounts.length}</Card.Text>
            <Button
              variant="light"
              size="sm"
              onClick={() => navigate("approve")}
            >
              Manage
            </Button>
          </Card>
        </Col>
        <Col md={6}>
          <Card bg="primary" text="white" className="p-3">
            <Card.Title>Upcoming Appointments</Card.Title>
            <Card.Text className="fs-2">{appointments.length}</Card.Text>
            <Button
              variant="light"
              size="sm"
              onClick={() => navigate("appointments")}
            >
              View All
            </Button>
          </Card>
        </Col>
      </Row>

      <h3 className="mb-3 d-flex align-items-center">
        <CalendarCheck className="me-2" /> Unassigned Appointments
      </h3>

      {appointments.length === 0 ? (
        <p className="text-muted">No unassigned appointments found.</p>
      ) : (
        <AppointmentsList
          appointments={appointments.map((a) => ({
            id: a.id,
            date: a.appointmentDate.split("T")[0],
            time: a.appointmentDate.split("T")[1].substring(0, 5),
            doctor: a.doctor?.fullName || "—",
            patient: a.patient?.fullName || "—",
            status: a.status,
          }))}
          onCancel={handleCancel}
        />
      )}
    </main>
  );
};

export default ReceptionistPage;
