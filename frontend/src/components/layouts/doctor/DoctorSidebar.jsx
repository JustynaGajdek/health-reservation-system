import React, { useState } from 'react';
import { NavLink } from 'react-router-dom';
import {
  Menu,
  X,
  Calendar,
  Users,
  FileText,
  ActivityHeart,
  Settings,
} from 'lucide-react';

const menuItems = [
  { to: '/doctor/today', icon: Calendar, label: 'Today\'s Appointments' },
  { to: '/doctor/patients', icon: Users, label: 'Patients' },
  { to: '/doctor/vaccinations', icon: ActivityHeart, label: 'Vaccinations' },
  { to: '/doctor/prescriptions', icon: FileText, label: 'Prescriptions' },
  { to: '/doctor/history', icon: FileText, label: 'Visit History' },
  { to: '/doctor/settings', icon: Settings, label: 'Settings' },
];

const DoctorSidebar = () => {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <aside
      className={`bg-[#dde9f9] text-gray-800 transition-all duration-200 
        ${isOpen ? 'w-64' : 'w-16'} fixed md:relative h-full flex flex-col`}
    >
      {/* Collapse/Expand Button for mobile */}
      <button
        aria-label={isOpen ? 'Collapse menu' : 'Expand menu'}
        onClick={() => setIsOpen(prev => !prev)}
        className="p-2 mt-2 ml-2 text-gray-600 hover:text-gray-900 md:hidden"
      >
        {isOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
      </button>

      {/* Logo (shown only when expanded) */}
      {isOpen && (
        <div className="h-16 flex items-center justify-center text-2xl font-bold">
          HealthSys
        </div>
      )}

      {/* Navigation Links */}
      <nav className="mt-4 flex-1">
        {menuItems.map(({ to, icon: Icon, label }) => (
          <NavLink
            key={to}
            to={to}
            className={({ isActive }) =>
              `flex items-center px-4 py-3 hover:bg-blue-200 rounded-md 
              ${isActive ? 'bg-blue-300' : ''}`
            }
            aria-label={label}
          >
            <Icon className="w-5 h-5" />
            {isOpen && <span className="ml-3">{label}</span>}
          </NavLink>
        ))}
      </nav>
    </aside>
  );
};

export default DoctorSidebar;
