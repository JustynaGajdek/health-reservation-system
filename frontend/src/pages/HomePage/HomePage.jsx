import { Link } from "react-router-dom";
import "./HomePage.css";

const HomePage = () => {
  return (
    <main className="home-container">
      <section className="hero">
        <h1 className="home-title">Welcome to the Health Reservation System</h1>
        <p className="home-description">
          A simple and secure platform for booking medical appointments, requesting prescriptions, and tracking vaccinations.
        </p>
        <div className="cta-buttons">
          <Link to="/login">
            <button className="btn primary">Log In</button>
          </Link>
          <Link to="/register">
            <button className="btn secondary">Register</button>
          </Link>
        </div>
      </section>

    <section className="features">
      <h2>Why choose us?</h2>
      <ul className="features-list">
       <li><span className="icon">✔️</span> Simple appointment scheduling</li>
       <li><span className="icon">🔒</span> Secure user authentication</li>
        <li><span className="icon">💊</span> Online prescription requests</li>
        <li><span className="icon">👥</span> Role-based dashboards for staff and patients</li>
      </ul>
   </section>

      <footer className="footer">
        <p>© 2025 Justyna Gajdek – Health Reservation System | Portfolio Project</p>
      </footer>
    </main>
  );
};

export default HomePage;
