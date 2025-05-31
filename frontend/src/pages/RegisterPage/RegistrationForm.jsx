import React from 'react';
import { FormProvider } from 'react-hook-form';
import { useRegistrationForm } from '../../hooks/useRegistrationForm';
import PersonalInfoFields from './PersonalInfoFields';
import ContactInfoFields from './ContactInfoFields';
import AccountFields from './AccountFields';
import ConsentField from './ConsentField';
import './RegistrationForm.css';

export default function RegistrationForm() {
  const methods = useRegistrationForm();
  const { handleSubmit, setFocus, formState: { errors, isSubmitting } } = methods;

  return (
    <FormProvider {...methods}>
      <div className="register-container">
        <form
          onSubmit={handleSubmit(
            methods.onSubmit,
            (errs) => setFocus(Object.keys(errs)[0])
          )}
          className="register-form"
          noValidate
        >
          <h2>Create Account</h2>

          {/* global error */}
          {errors.root && (
            <div id="form-error" role="alert" className="error">
              {errors.root.message}
            </div>
          )}

          {/* Personal Info */}
          <section>
            <h3>Personal Information</h3>
            <div className="section-grid">
              <PersonalInfoFields />
            </div>
          </section>

          {/* Contact Info */}
          <section>
            <h3>Contact Information</h3>
            <div className="section-grid">
              <ContactInfoFields />
            </div>
          </section>

          {/* Account */}
          <section>
            <h3>Account</h3>
            <div className="section-grid">
              <AccountFields />
            </div>
          </section>

          {/* Consent */}
          <ConsentField />

          {/* Submit */}
          <button
            type="submit"
            className="register-button"
            disabled={isSubmitting}
            aria-busy={isSubmitting}
          >
            {isSubmitting
              ? <><span className="spinner" aria-hidden="true"></span>Registering…</>
              : 'Create Account'}
          </button>
        </form>
      </div>
    </FormProvider>
  );
}
