import React, { useState, useEffect } from "react";
import { Spinner } from "react-bootstrap";
import { createAppointment } from "../../services/reception.service";
import axios from "../../services/api";

const BookAppointment = () => {
  const [form, setForm] = useState({
    date: "",
    time: "",
    doctorId: "",
    patientPesel: "",
    type: "IN_PERSON",
  });

  const [doctors, setDoctors] = useState([]);
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState("");

  useEffect(() => {
    const fetchDoctors = async () => {
      try {
        const res = await axios.get("/public/doctors");
        setDoctors(res.data);
      } catch (err) {
        console.error("Failed to load doctors", err);
      }
    };
    fetchDoctors();
  }, []);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setSuccess("");

    const payload = {
      appointmentDate: `${form.date}T${form.time}`,
      doctorId: form.doctorId,
      patientPesel: form.patientPesel,
      appointmentType: form.type,
    };

    try {
      await createAppointment(payload);
      setSuccess("Appointment successfully booked!");
      setForm({
        date: "",
        time: "",
        doctorId: "",
        patientPesel: "",
        type: "IN_PERSON",
      });
    } catch (err) {
      alert("Error booking appointment.");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-4">
      <h2>Book Appointment</h2>

      <form
        onSubmit={handleSubmit}
        className="p-3 border rounded bg-white shadow-sm"
      >
        {success && <div className="alert alert-success">{success}</div>}

        <div className="mb-3">
          <label htmlFor="date" className="form-label">
            Date
          </label>
          <input
            type="date"
            id="date"
            name="date"
            className="form-control"
            value={form.date}
            onChange={handleChange}
            required
          />
        </div>

        <div className="mb-3">
          <label htmlFor="time" className="form-label">
            Time
          </label>
          <input
            type="time"
            id="time"
            name="time"
            className="form-control"
            value={form.time}
            onChange={handleChange}
            required
          />
        </div>

        <div className="mb-3">
          <label htmlFor="doctorId" className="form-label">
            Doctor
          </label>
          <select
            id="doctorId"
            name="doctorId"
            className="form-select"
            value={form.doctorId}
            onChange={handleChange}
            required
          >
            <option value="">-- Select --</option>
            {doctors.map((doc) => (
              <option key={doc.id} value={doc.id}>
                {doc.fullName}
              </option>
            ))}
          </select>
        </div>

        <div className="mb-3">
          <label htmlFor="patientPesel" className="form-label">
            Patient PESEL
          </label>
          <input
            type="text"
            id="patientPesel"
            name="patientPesel"
            className="form-control"
            value={form.patientPesel}
            onChange={handleChange}
            pattern="\d{11}"
            title="PESEL must be 11 digits"
            required
          />
        </div>

        <div className="mb-3">
          <label htmlFor="type" className="form-label">
            Type
          </label>
          <select
            id="type"
            name="type"
            className="form-select"
            value={form.type}
            onChange={handleChange}
          >
            <option value="IN_PERSON">In-person</option>
            <option value="TELECONSULTATION">Teleconsultation</option>
          </select>
        </div>

        <button type="submit" className="btn btn-success" disabled={loading}>
          {loading ? <Spinner size="sm" animation="border" /> : "Book"}
        </button>
      </form>
    </div>
  );
};

export default BookAppointment;
