import React from 'react';
import DoctorSidebar from './DoctorSidebar';
import DoctorHeader from './DoctorHeader';
import { Outlet } from 'react-router-dom';

const DoctorLayout = () => (
  <div className="d-flex min-vh-100">
    {/* Sidebar on the left */}
    <DoctorSidebar />

    {/* Main content area */}
    <div className="flex-grow-1 d-flex flex-column">
      {/* Top navigation bar */}
      <DoctorHeader />

      {/* Page content */}
      <main className="flex-grow-1 p-4 bg-light">
        <Outlet />
      </main>
    </div>
  </div>
);

export default DoctorLayout;
