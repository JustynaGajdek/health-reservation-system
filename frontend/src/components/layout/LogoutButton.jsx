import React from 'react';
import { Button } from 'react-bootstrap';
import { useAuthContext } from '../../context/AuthContext';

const LogoutButton = () => {
  const { logout } = useAuthContext();

  return (
    <Button
      variant="outline-secondary"
      size="sm"
      onClick={logout}
      className="ms-3"
    >
      Logout
    </Button>
  );
};

export default LogoutButton;
