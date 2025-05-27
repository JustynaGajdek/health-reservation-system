import React from "react";
import AppointmentCard from "./AppointmentCard";

const AppointmentsList = ({ appointments }) => {
  if (!appointments || appointments.length === 0) {
    return <p className="text-muted">No upcoming appointments found.</p>;
  }

  return (
    <div className="appointments-list">
      {appointments.map((appointment) => (
        <AppointmentCard key={appointment.id} appointment={appointment} />
      ))}
    </div>
  );
};

export default AppointmentsList;
