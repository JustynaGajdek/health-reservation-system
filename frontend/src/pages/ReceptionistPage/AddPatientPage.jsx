import React, { useState } from "react";
import { Form, Button, Alert } from "react-bootstrap";
import api from "../../services/api";

const AddPatientPage = () => {
  const [form, setForm] = useState({
    firstName: "",
    lastName: "",
    email: "",
    pesel: "",
    phone: "",
    address: "",
    dateOfBirth: "",
  });

  const [errors, setErrors] = useState({});
  const [success, setSuccess] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const validate = () => {
    const newErrors = {};
    if (!form.firstName.trim()) newErrors.firstName = "First name is required";
    if (!form.lastName.trim()) newErrors.lastName = "Last name is required";
    if (!form.email.match(/^[\w-.]+@([\w-]+\.)+[\w-]{2,4}$/))
      newErrors.email = "Please enter a valid email address";
    if (!form.pesel.match(/^\d{11}$/))
      newErrors.pesel = "PESEL must be exactly 11 digits";
    if (!form.phone.match(/^\+?\d{9,15}$/))
      newErrors.phone = "Please enter a valid phone number";
    if (!form.address.trim()) newErrors.address = "Address is required";
    if (!form.dateOfBirth) newErrors.dateOfBirth = "Date of birth is required";

    return newErrors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const newErrors = validate();

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    try {
      setLoading(true);
      await api.post("/reception/patients", form);
      setSuccess(true);
      setForm({
        firstName: "",
        lastName: "",
        email: "",
        pesel: "",
        phone: "",
        address: "",
        dateOfBirth: "",
      });
      setErrors({});
    } catch (error) {
      console.error(error);
      alert("Something went wrong while adding the patient.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-4" style={{ maxWidth: "600px" }}>
      <h2 className="mb-4">Add New Patient</h2>

      {success && <Alert variant="success">Patient successfully added.</Alert>}

      <Form onSubmit={handleSubmit} noValidate>
        <h5 className="mb-3">Personal Information</h5>

        <Form.Group className="mb-3">
          <Form.Label>First Name</Form.Label>
          <Form.Control
            name="firstName"
            placeholder="Enter first name"
            value={form.firstName}
            onChange={handleChange}
            isInvalid={!!errors.firstName}
          />
          <Form.Control.Feedback type="invalid">
            {errors.firstName}
          </Form.Control.Feedback>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Last Name</Form.Label>
          <Form.Control
            name="lastName"
            placeholder="Enter last name"
            value={form.lastName}
            onChange={handleChange}
            isInvalid={!!errors.lastName}
          />
          <Form.Control.Feedback type="invalid">
            {errors.lastName}
          </Form.Control.Feedback>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Date of Birth</Form.Label>
          <Form.Control
            type="date"
            name="dateOfBirth"
            value={form.dateOfBirth}
            onChange={handleChange}
            isInvalid={!!errors.dateOfBirth}
          />
          <Form.Control.Feedback type="invalid">
            {errors.dateOfBirth}
          </Form.Control.Feedback>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>PESEL</Form.Label>
          <Form.Control
            name="pesel"
            placeholder="Enter 11-digit PESEL"
            value={form.pesel}
            onChange={handleChange}
            isInvalid={!!errors.pesel}
          />
          <Form.Control.Feedback type="invalid">
            {errors.pesel}
          </Form.Control.Feedback>
        </Form.Group>

        <h5 className="mt-4 mb-3">Contact Information</h5>

        <Form.Group className="mb-3">
          <Form.Label>Email</Form.Label>
          <Form.Control
            name="email"
            type="email"
            placeholder="Enter email address"
            value={form.email}
            onChange={handleChange}
            isInvalid={!!errors.email}
          />
          <Form.Control.Feedback type="invalid">
            {errors.email}
          </Form.Control.Feedback>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Phone Number</Form.Label>
          <Form.Control
            name="phone"
            placeholder="Enter phone number"
            value={form.phone}
            onChange={handleChange}
            isInvalid={!!errors.phone}
          />
          <Form.Control.Feedback type="invalid">
            {errors.phone}
          </Form.Control.Feedback>
        </Form.Group>

        <Form.Group className="mb-4">
          <Form.Label>Address</Form.Label>
          <Form.Control
            name="address"
            placeholder="Enter full address"
            value={form.address}
            onChange={handleChange}
            isInvalid={!!errors.address}
          />
          <Form.Control.Feedback type="invalid">
            {errors.address}
          </Form.Control.Feedback>
        </Form.Group>

        <Button type="submit" disabled={loading}>
          {loading ? "Saving..." : "Add Patient"}
        </Button>
      </Form>
    </div>
  );
};

export default AddPatientPage;
