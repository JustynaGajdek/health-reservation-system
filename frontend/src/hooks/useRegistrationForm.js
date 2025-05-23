import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import * as yup from "yup";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import api from "../services/api";

const schema = yup.object().shape({
  firstName: yup.string().required("First name is required"),
  lastName: yup.string().required("Last name is required"),
  pesel: yup
    .string()
    .matches(/^\d{11}$/, "PESEL must be 11 digits")
    .required("PESEL is required"),
  dateOfBirth: yup.date().required("Date of birth is required"),
  email: yup.string().email("Invalid email").required("Email is required"),
  phone: yup
    .string()
    .matches(/^\d{9,15}$/, "Phone must be 9–15 digits")
    .required("Phone is required"),
  address: yup.string().required("Address is required"),
  password: yup
    .string()
    .min(6, "Password must be at least 6 chars")
    .required("Password is required"),
  confirmPassword: yup
    .string()
    .oneOf([yup.ref("password")], "Passwords must match")
    .required("Confirm your password"),
  consent: yup.bool().oneOf([true], "You must accept terms"),
});

export function useRegistrationForm() {
  const navigate = useNavigate();
  const methods = useForm({ resolver: yupResolver(schema) });

  const onSubmit = async (data) => {
    const { confirmPassword, ...payload } = data;
    try {
      await api.post("/auth/register", payload);

      toast.success("Account created. Waiting for receptionist approval.");
      methods.reset();
      navigate("/");
    } catch (err) {
      methods.setError("root", {
        type: "server",
        message: err.response?.data?.message || "Registration failed",
      });
    }
  };

  return { ...methods, onSubmit };
}
