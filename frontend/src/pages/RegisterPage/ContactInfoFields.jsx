import React from 'react'
import { useFormContext } from 'react-hook-form'

export default function ContactInfoFields() {
  const { register, formState:{errors} } = useFormContext()
  return (
    <>
      <div className="form-group">
        <label htmlFor="email">Email *</label>
        <input id="email" type="email" {...register('email')} />
        {errors.email && <p className="error">{errors.email.message}</p>}
      </div>
      <div className="form-group">
        <label htmlFor="phone">Phone *</label>
        <input id="phone" type="tel" {...register('phone')} />
        {errors.phone && <p className="error">{errors.phone.message}</p>}
      </div>
      <div className="form-group">
        <label htmlFor="address">Address *</label>
        <input id="address" type="text" {...register('address')} />
        {errors.address && <p className="error">{errors.address.message}</p>}
      </div>
    </>
  )
}
