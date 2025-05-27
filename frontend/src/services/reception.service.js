import api from "./api";

export const getPendingUsers = async () => {
  const { data } = await api.get("/reception/users/pending");
  return data;
};

export const getUnassignedAppointments = async () => {
  const { data } = await api.get("/reception/appointments/unassigned");
  return data;
};

export const cancelAppointment = async (id) => {
  return api.delete(`/reception/appointments/${id}`);
};

export const approveUser = async (id) =>
  api.put(`/reception/users/approve/${id}`);
export const rejectUser = async (id) =>
  api.put(`/reception/users/reject/${id}`);
export const createAppointment = async (data) =>
  api.post("/reception/appointments", data);
