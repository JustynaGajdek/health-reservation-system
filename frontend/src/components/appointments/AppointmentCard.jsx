import React from 'react';

const AppointmentCard = () => {
  const mock = {
    date: '2025-05-20',
    time: '10:00',
    doctor: 'dr Anna Nowak',
    specialization: 'Internal Medicine',
  };

  return (
    <div
      className="dashboard-card"
      role="region"
      aria-labelledby="next-appointment"
    >
      <h2 id="next-appointment">Next Appointment</h2>
      <p>{mock.date} at {mock.time}</p>
      <p>Doctor: {mock.doctor}, {mock.specialization}</p>

      <div className="d-flex gap-2 mb-3">
        <button className="btn btn-outline-secondary">View Details</button>
        <button className="btn btn-danger">Cancel</button>
      </div>
    </div>
  );
};

export default AppointmentCard;
