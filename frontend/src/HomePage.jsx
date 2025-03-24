import { Link } from "react-router-dom";
import "./HomePage.css";

const HomePage = () => {
  return (
    <div className="homepage">
      <h1>Welcome to Our Clinic</h1>
      <p>Please log in to book an appointment or request a prescription.</p>
      <Link to="/login">
        <button className="login-button">Log In</button>
      </Link>
    </div>
  );
};

export default HomePage;
