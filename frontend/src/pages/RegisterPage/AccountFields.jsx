import React, { useState } from 'react';
import { useFormContext } from 'react-hook-form';
import { Eye, EyeOff } from 'lucide-react';

export default function AccountFields() {
  const { register, formState: { errors } } = useFormContext();
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);

  return (
    <>
      {/* Password */}
      <div className="form-group required password-wrapper">
        <label htmlFor="password">Password</label>
        <input
          id="password"
          type={showPassword ? 'text' : 'password'}
          {...register('password')}
        />
        <button
          type="button"
          className="toggle-visibility"
          onClick={() => setShowPassword(prev => !prev)}
          aria-label={showPassword ? 'Hide password' : 'Show password'}
        >
          {showPassword
            ? <EyeOff size={20} />
            : <Eye size={20} />
          }
        </button>
        {errors.password && (
          <p className="error">{errors.password.message}</p>
        )}
      </div>

      {/* Confirm Password */}
      <div className="form-group required password-wrapper">
        <label htmlFor="confirmPassword">Confirm Password</label>
        <input
          id="confirmPassword"
          type={showConfirm ? 'text' : 'password'}
          {...register('confirmPassword')}
        />
        <button
          type="button"
          className="toggle-visibility"
          onClick={() => setShowConfirm(prev => !prev)}
          aria-label={showConfirm ? 'Hide confirm password' : 'Show confirm password'}
        >
          {showConfirm
            ? <EyeOff size={20} />
            : <Eye size={20} />
          }
        </button>
        {errors.confirmPassword && (
          <p className="error">{errors.confirmPassword.message}</p>
        )}
      </div>
    </>
  );
}
