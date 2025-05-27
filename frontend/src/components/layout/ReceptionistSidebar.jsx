import React from 'react';
import { NavLink } from 'react-router-dom';
import {
  UserPlus,
  CalendarPlus,
  Users,
  Calendar,
  ShieldCheck,
  Settings
} from 'lucide-react';

const ReceptionistSidebar = () => {
  return (
    <aside
      className="d-flex flex-column p-3 bg-light"
      style={{ minHeight: '100vh', width: '250px' }}
      aria-label="Receptionist navigation"
    >
      <h2 className="text-primary mb-4">Reception</h2>

      <ul className="nav nav-pills flex-column mb-auto gap-2">
        <li>
          <NavLink
            to="/dashboard/receptionist/patients"
            className={({ isActive }) =>
              `nav-link ${isActive ? 'active text-primary fw-bold' : 'text-dark'}`
            }
          >
            <Users className="me-2" size={20} aria-hidden="true" />
            Patients
          </NavLink>
        </li>
        <li>
          <NavLink
            to="/dashboard/receptionist/appointments"
            className={({ isActive }) =>
              `nav-link ${isActive ? 'active text-primary fw-bold' : 'text-dark'}`
            }
          >
            <Calendar className="me-2" size={20} aria-hidden="true" />
            Appointments
          </NavLink>
        </li>
        <li>
          <NavLink
            to="/dashboard/receptionist/approve"
            className={({ isActive }) =>
              `nav-link ${isActive ? 'active text-primary fw-bold' : 'text-dark'}`
            }
          >
            <ShieldCheck className="me-2" size={20} aria-hidden="true" />
            To Approve
          </NavLink>
        </li>
        <li>
          <NavLink
            to="/dashboard/receptionist/add-patient"
            className={({ isActive }) =>
              `nav-link ${isActive ? 'active text-primary fw-bold' : 'text-dark'}`
            }
          >
            <UserPlus className="me-2" size={20} aria-hidden="true" />
            Add Patient
          </NavLink>
        </li>
        <li>
          <NavLink
            to="/dashboard/receptionist/add-appointment"
            className={({ isActive }) =>
              `nav-link ${isActive ? 'active text-primary fw-bold' : 'text-dark'}`
            }
          >
            <CalendarPlus className="me-2" size={20} aria-hidden="true" />
            Add Appointment
          </NavLink>
        </li>
        <li>
          <NavLink
            to="/dashboard/receptionist/settings"
            className={({ isActive }) =>
              `nav-link ${isActive ? 'active text-primary fw-bold' : 'text-dark'}`
            }
          >
            <Settings className="me-2" size={20} aria-hidden="true" />
            Settings
          </NavLink>
        </li>
      </ul>
    </aside>
  );
};

export default ReceptionistSidebar;
