import React from 'react';
import { useFormContext } from 'react-hook-form';

export default function PersonalInfoFields() {
  const { register, formState: { errors } } = useFormContext();

  return (
    <>
      {/* First Name */}
      <div className="form-group required">
        <label htmlFor="firstName">First Name</label>
        <input
          id="firstName"
          type="text"
          aria-required="true"
          aria-invalid={!!errors.firstName}
          {...register('firstName')}
        />
        {errors.firstName && <p className="error">{errors.firstName.message}</p>}
      </div>

      {/* Last Name */}
      <div className="form-group required">
        <label htmlFor="lastName">Last Name</label>
        <input
          id="lastName"
          type="text"
          aria-required="true"
          aria-invalid={!!errors.lastName}
          {...register('lastName')}
        />
        {errors.lastName && <p className="error">{errors.lastName.message}</p>}
      </div>

      {/* PESEL */}
      <div className="form-group required">
        <label htmlFor="pesel">PESEL</label>
        <input
          id="pesel"
          inputMode="numeric"
          placeholder="11 digits"
          aria-required="true"
          aria-invalid={!!errors.pesel}
          {...register('pesel')}
        />
        {errors.pesel && <p className="error">{errors.pesel.message}</p>}
      </div>

      {/* Date of Birth */}
      <div className="form-group required">
        <label htmlFor="dateOfBirth">Date of Birth</label>
        <input
          id="dateOfBirth"
          type="date"
          placeholder="dd.MM.rrrr"
          aria-required="true"
          aria-invalid={!!errors.dateOfBirth}
          {...register('dateOfBirth')}
        />
        {errors.dateOfBirth && <p className="error">{errors.dateOfBirth.message}</p>}
      </div>
    </>
  );
}
