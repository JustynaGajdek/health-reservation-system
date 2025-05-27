import api from './api';


export const getAllPatients = async () => {
  try {
    const { data } = await api.get('/reception/patients');
    return data;
  } catch (err) {
    console.error('getAllPatients error:', err);
    throw new Error(
      err.response?.data?.message ||
      'Unable to load patient list. Please try again later.'
    );
  }
};


export const getPatientById = async (id) => {
  try {
    const { data } = await api.get(`/reception/patients/${id}`);
    return data;
  } catch (err) {
    console.error(`getPatientById(${id}) error:`, err);
    throw new Error(
      err.response?.data?.message ||
      'Unable to load patient details. Please try again later.'
    );
  }
};


export const createPatient = async (patientData) => {
  try {
    const { data } = await api.post('/reception/patients', patientData);
    return data;
  } catch (err) {
    console.error('createPatient error:', err);
    if (err.response?.status === 400) {
      throw new Error(err.response.data.message || 'Invalid patient data.');
    }
    if (err.response?.status === 409) {
      throw new Error('A patient with this email or PESEL already exists.');
    }
    throw new Error(
      'Unable to create patient. Please try again or contact support.'
    );
  }
};
