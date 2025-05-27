import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Spinner, Button, ListGroup, Card } from 'react-bootstrap';
import { toast } from 'react-toastify';
import { getPatientById } from '../../services/patient.service';

const PatientDetails = () => {
  const { id } = useParams();
  const navigate = useNavigate();

  const [patient, setPatient] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDetails = async () => {
      setLoading(true);
      try {
        const data = await getPatientById(id);
        setPatient(data);
      } catch (error) {
        console.error('Failed to load patient:', error);
        toast.error('Could not load patient details.');
      } finally {
        setLoading(false);
      }
    };
    fetchDetails();
  }, [id]);

  if (loading) {
    return (
      <div className="text-center mt-5">
        <Spinner animation="border" role="status" />
      </div>
    );
  }

  if (!patient) {
    return <p className="text-center mt-5 text-muted">No data available.</p>;
  }

  const {
    firstName,
    lastName,
    email,
    pesel,
    phoneNumber,
    address,
    dateOfBirth
  } = patient;

  return (
    <Card className="container mt-4" style={{ maxWidth: '600px' }}>
      <Card.Header>
        <h2>Patient Details</h2>
      </Card.Header>
      <ListGroup variant="flush">
        <ListGroup.Item><strong>Name:</strong> {firstName} {lastName}</ListGroup.Item>
        <ListGroup.Item><strong>Email:</strong> {email}</ListGroup.Item>
        <ListGroup.Item><strong>PESEL:</strong> {pesel}</ListGroup.Item>
        <ListGroup.Item><strong>Phone:</strong> {phoneNumber}</ListGroup.Item>
        <ListGroup.Item><strong>Address:</strong> {address}</ListGroup.Item>
        <ListGroup.Item>
          <strong>Date of Birth:</strong>{' '}
          {new Date(dateOfBirth).toLocaleDateString()}
        </ListGroup.Item>
      </ListGroup>
      <Card.Footer>
        <Button variant="secondary" onClick={() => navigate(-1)}>
          ← Back
        </Button>
      </Card.Footer>
    </Card>
  );
};

export default PatientDetails;
