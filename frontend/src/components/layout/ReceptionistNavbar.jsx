import React from 'react';
import { Navbar } from 'react-bootstrap';
import { useAuthContext } from '../../context/AuthContext';

const ReceptionistNavbar = () => {
  const { user } = useAuthContext();

  return (
    <Navbar bg="white" className="shadow-sm" expand="lg">
      <div className="container-fluid">
        <Navbar.Brand className="fw-bold text-primary">
          Reception Panel
        </Navbar.Brand>
        <div className="ms-auto text-muted small">
          Logged in as: {user?.firstName || 'Receptionist'}
        </div>
      </div>
    </Navbar>
  );
};

export default ReceptionistNavbar;
