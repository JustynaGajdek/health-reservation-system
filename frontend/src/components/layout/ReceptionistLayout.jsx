import React from "react";
import ReceptionistNavbar from "./ReceptionistNavbar";
import ReceptionistSidebar from "./ReceptionistSidebar";
import LogoutButton from "./LogoutButton";
import { Outlet } from "react-router-dom";

const ReceptionistLayout = () => (
  <div className="d-flex">
    <ReceptionistSidebar />
    <div className="flex-grow-1">
      <div className="d-flex justify-content-between align-items-center bg-white shadow-sm p-3">
        <ReceptionistNavbar />
        <LogoutButton />
      </div>
      <div className="p-4">
        <Outlet />
      </div>
    </div>
  </div>
);

export default ReceptionistLayout;
