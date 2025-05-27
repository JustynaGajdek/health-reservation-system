import React, { useEffect, useState } from "react";
import { Button, Table, Badge, Spinner } from "react-bootstrap";
import {
  getPendingUsers,
  approveUser,
  rejectUser,
} from "../../services/reception.service";

const statusVariant = {
  PENDING: "warning",
  ACTIVE: "success",
  REJECTED: "danger",
};

const ApproveUsers = () => {
  const [users, setUsers] = useState([]);
  const [loadingId, setLoadingId] = useState(null);

  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const data = await getPendingUsers();
        setUsers(data);
      } catch (err) {
        console.error("Failed to load users", err);
      }
    };
    fetchUsers();
  }, []);

  const handleApprove = async (id) => {
    setLoadingId(id);
    try {
      await approveUser(id);
      setUsers((prev) => prev.filter((user) => user.id !== id));
    } catch (err) {
      console.error("Error approving user", err);
    } finally {
      setLoadingId(null);
    }
  };

  const handleReject = async (id) => {
    setLoadingId(id);
    try {
      await rejectUser(id);
      setUsers((prev) => prev.filter((user) => user.id !== id));
    } catch (err) {
      console.error("Error rejecting user", err);
    } finally {
      setLoadingId(null);
    }
  };

  return (
    <div className="container mt-4">
      <h2>Pending User Accounts</h2>
      <Table bordered hover responsive className="bg-white">
        <thead>
          <tr>
            <th>Name</th>
            <th>PESEL</th>
            <th>Email</th>
            <th>Status</th>
            <th style={{ minWidth: "160px" }}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.length > 0 ? (
            users.map((user) => (
              <tr key={user.id}>
                <td>
                  {user.firstName} {user.lastName}
                </td>
                <td>{user.pesel || "—"}</td>
                <td>{user.email}</td>
                <td>
                  <Badge bg={statusVariant[user.status] || "secondary"}>
                    {user.status}
                  </Badge>
                </td>
                <td className="d-flex gap-2">
                  <Button
                    size="sm"
                    variant="success"
                    onClick={() => handleApprove(user.id)}
                    disabled={loadingId === user.id}
                  >
                    {loadingId === user.id ? (
                      <Spinner size="sm" animation="border" />
                    ) : (
                      "Approve"
                    )}
                  </Button>
                  <Button
                    size="sm"
                    variant="outline-danger"
                    onClick={() => handleReject(user.id)}
                    disabled={loadingId === user.id}
                  >
                    {loadingId === user.id ? (
                      <Spinner size="sm" animation="border" />
                    ) : (
                      "Reject"
                    )}
                  </Button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="5" className="text-center text-muted">
                No pending users
              </td>
            </tr>
          )}
        </tbody>
      </Table>
    </div>
  );
};

export default ApproveUsers;
