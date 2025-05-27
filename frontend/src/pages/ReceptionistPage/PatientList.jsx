import React, { useEffect, useState } from 'react';
import { Table, Button, InputGroup, FormControl, Spinner } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { toast } from 'react-toastify';
import { getAllPatients } from '../../services/patient.service';

const PatientList = () => {
  const [patients, setPatients] = useState([]);
  const [filtered, setFiltered] = useState([]);
  const [search, setSearch] = useState('');
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchPatients = async () => {
      setLoading(true);
      try {
        const data = await getAllPatients();
        setPatients(data);
        setFiltered(data);
      } catch (err) {
        console.error('Failed to fetch patients:', err);
        toast.error('Unable to load patients. Please try again later.');
      } finally {
        setLoading(false);
      }
    };
    fetchPatients();
  }, []);

  useEffect(() => {
    const q = search.toLowerCase();
    setFiltered(
      patients.filter(p =>
        [p.firstName, p.lastName, p.email, p.pesel]
          .join(' ')
          .toLowerCase()
          .includes(q)
      )
    );
  }, [search, patients]);

  if (loading) {
    return (
      <div className="text-center mt-5">
        <Spinner animation="border" role="status" />
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <h2>Patient List</h2>

      <InputGroup className="mb-3 mt-3">
        <FormControl
          placeholder="Search patients..."
          value={search}
          onChange={e => setSearch(e.target.value)}
          aria-label="Search patients"
        />
      </InputGroup>

      <Table striped bordered hover responsive>
        <thead>
          <tr>
            <th>Name</th>
            <th>PESEL</th>
            <th>Email</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {filtered.length === 0 ? (
            <tr>
              <td colSpan="4" className="text-center text-muted">
                {search ? 'No matching patients found.' : 'No patients available.'}
              </td>
            </tr>
          ) : (
            filtered.map(p => (
              <tr key={p.id}>
                <td>{p.firstName} {p.lastName}</td>
                <td>{p.pesel || '—'}</td>
                <td>{p.email}</td>
                <td>
                  <Button
                    variant="outline-primary"
                    size="sm"
                    onClick={() => navigate(`/reception/patients/${p.id}`)}
                  >
                    View
                  </Button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </Table>
    </div>
  );
};

export default PatientList;
