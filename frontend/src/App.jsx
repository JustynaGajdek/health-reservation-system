import { useState, useEffect } from "react";
import "./App.css";

function App() {
  const [springMessage, setSpringMessage] = useState("");

  useEffect(() => {
    fetch("http://localhost:8080/api/hello")
      .then((response) => response.text())
      .then((message) => {
        console.log(message);
        setSpringMessage(message);
      })
      .catch(() => {
        setSpringMessage("Failed to load message");
      });
  }, []);

  return (
    <div className="container vh-100 bg-light">
      {}
      <div className="row h-100 d-flex justify-content-center align-items-center">
        {}
        <div className="col-auto text-center">
          <h1 className="display-4 text-primary shadow p-4 rounded">
            Hello World from React!
          </h1>
          <h2 className="text-success">
            {springMessage || "Loading message from Spring..."}
          </h2>
        </div>
      </div>
    </div>
  );
}

export default App;
