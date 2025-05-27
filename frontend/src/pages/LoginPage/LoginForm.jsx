import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuthContext } from "../../context/AuthContext";
import "./LoginForm.css";
import { toast } from "react-toastify";

const LoginForm = () => {
  const navigate = useNavigate();
  const { login, user } = useAuthContext();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    if (user?.role) {
      switch (user.role) {
        case "PATIENT":
          navigate("/dashboard/patient");
          break;
        case "RECEPTIONIST":
          navigate("/dashboard/receptionist");
          break;
        case "DOCTOR":
          navigate("/doctor");
          break;
        case "ADMIN":
          navigate("/admin");
          break;
        default:
          navigate("/");
      }
    }
  }, [user, navigate]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    try {
      await login({ email, password });
      toast.success("Logged in!");
    } catch (err) {
      console.error(err);
      setError("Invalid email or password. Please try again.");
    }
  };

  return (
    <div className="login-container">
      <form onSubmit={handleSubmit} className="login-form">
        <h2>Log In</h2>
        {error && <p className="error-message">{error}</p>}
        <div className="form-group">
          <label htmlFor="email">Email:</label>
          <input
            id="email"
            type="email"
            placeholder="you@email.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>
        <div className="form-group">
          <label>Password:</label>
          <input
            type="password"
            placeholder="********"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit" className="login-button">
          Log In
        </button>

        <div className="form-links">
          <Link to="/register">Don't have an account? Register</Link>
          <Link to="/">← Back to Home</Link>
        </div>
      </form>
    </div>
  );
};

export default LoginForm;
