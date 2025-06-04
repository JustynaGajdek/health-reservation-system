import React from 'react';
import { Navbar, Nav, Image } from 'react-bootstrap';
import { Bell } from 'lucide-react';
import { useAuthContext } from '../../context/AuthContext';

const DoctorNavbar = () => {
  const { user } = useAuthContext();

  return (
    <Navbar bg="white" className="shadow-sm" expand="lg">
      <div className="container-fluid d-flex justify-content-between align-items-center">
        <Navbar.Brand className="fw-bold text-primary">
          Doctor Panel
        </Navbar.Brand>

        <Nav className="d-flex align-items-center gap-3">
          <Bell
            role="button"
            aria-label="Notifications"
            className="text-muted"
          />
          <div className="d-flex flex-column text-end">
            <span className="small text-muted">Logged in as:</span>
            <span className="fw-semibold">Dr {user?.firstName || 'Doctor'}</span>
          </div>
          <Image
            src={user?.avatarUrl || '/default-avatar.png'}
            roundedCircle
            height={36}
            width={36}
            alt="Profile"
          />
        </Nav>
      </div>
    </Navbar>
  );
};

export default DoctorNavbar;
