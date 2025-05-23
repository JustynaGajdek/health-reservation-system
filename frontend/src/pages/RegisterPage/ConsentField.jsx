import React from 'react';
import { useFormContext } from 'react-hook-form';

export default function ConsentField() {
  const { register, formState: { errors } } = useFormContext();

  return (
    <div className="consent-wrapper">
      <input
        id="consent"
        type="checkbox"
        {...register('consent', { required: 'You must agree before continuing' })}
        aria-required="true"
        aria-invalid={!!errors.consent}
      />
      <label htmlFor="consent">
        I agree to the Privacy Policy and data processing.
      </label>
      {errors.consent && <p className="error">{errors.consent.message}</p>}
    </div>
  );
}
