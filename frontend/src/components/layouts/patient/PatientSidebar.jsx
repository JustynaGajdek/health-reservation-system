import React from "react";
import { NavLink } from "react-router-dom";
import { CalendarDays, Plus, Clock, Settings } from "lucide-react";

const menuItems = [
  {
    to: "/appointments",
    icon: <CalendarDays size={18} />,
    label: "Appointments",
  },
  { to: "/book-visit", icon: <Plus size={18} />, label: "Book Visit" },
  { to: "/history", icon: <Clock size={18} />, label: "History" },
  { to: "/settings", icon: <Settings size={18} />, label: "Settings" },
];

const PatientSidebar = () => (
  <nav className="dashboard-sidebar">
    <ul className="nav flex-column">
      {menuItems.map(({ to, icon, label }) => (
        <li key={to}>
          <NavLink
            to={to}
            className={({ isActive }) => `nav-link${isActive ? " active" : ""}`}
          >
            {icon} {label}
          </NavLink>
        </li>
      ))}
    </ul>
  </nav>
);

export default PatientSidebar;
