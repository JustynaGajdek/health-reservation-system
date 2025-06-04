import React from 'react';
import DoctorNavbar from './DoctorNavbar';
import LogoutButton from '../common/LogoutButton';

const DoctorHeader = () => (
  <header className="d-flex justify-content-between align-items-center bg-white shadow-sm px-4 py-3">
    <DoctorNavbar />
    <LogoutButton />
  </header>
);

export default DoctorHeader;
