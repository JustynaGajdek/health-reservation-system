import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import axios from 'axios';
import './RegisterPage.css';

const RegisterPage = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    password: '',
    pesel: '',
    dateOfBirth: '',
    address: '',
  });

  const [error, setError] = useState('');

  const handleChange = (e) => {
    setFormData(prev => ({
      ...prev,
      [e.target.name]: e.target.value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      await axios.post('http://localhost:8080/patients/register', formData);
      navigate('/login');
    } catch (err) {
      console.error(err);
      setError('Registration failed. Please try again.');
    }
  };

  return (
    <div className="register-container">
      <form onSubmit={handleSubmit} className="register-form">
        <h2>Register</h2>
        {error && <p className="error-message">{error}</p>}

        {Object.entries(formData).map(([key, value]) => (
          <div className="form-group" key={key}>
            <label>{key}</label>
            <input
              type={key === 'password' ? 'password' : 'text'}
              name={key}
              value={value}
              onChange={handleChange}
              required
            />
          </div>
        ))}

        <button type="submit" className="register-button">Create Account</button>

        <div className="form-links">
          <Link to="/login">Already have an account? Log in</Link>
          <Link to="/">← Back to Home</Link>
        </div>
      </form>
    </div>
  );
};

export default RegisterPage;
