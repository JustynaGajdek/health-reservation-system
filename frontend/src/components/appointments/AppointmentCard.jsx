import React from 'react';
import PropTypes from 'prop-types';
import { Button, Badge, Spinner, OverlayTrigger, Tooltip } from 'react-bootstrap';
import { format } from 'date-fns';
import { Eye, Pencil, Trash } from 'lucide-react';

const statusVariant = {
  CONFIRMED: 'success',
  PENDING: 'warning',
  CANCELLED: 'danger'
};

const statusTooltip = {
  CONFIRMED: 'Appointment is confirmed',
  PENDING: 'Appointment is pending approval',
  CANCELLED: 'Appointment was cancelled'
};

const AppointmentCard = ({ appointment, onDetails, onCancel, onEdit, isLoading }) => {
  const {
    date,
    time,
    doctor = 'Unknown Doctor',
    specialization = 'Unknown Specialization',
    patient,
    status
  } = appointment || {};

  const formattedDate = date ? format(new Date(date), 'PPP') : 'Date not set';
  const formattedTime = time ?? 'Time not set';

  const badgeColor = statusVariant[status] || 'secondary';
  const badgeTooltip = statusTooltip[status] || 'Status unknown';

  const renderButtons = () => (
    <div className="d-flex gap-2 mt-3">
      {onDetails && (
        <Button size="sm" variant="outline-primary" onClick={onDetails} title="View details">
          <Eye size={16} className="me-1" />
          Details
        </Button>
      )}
      {onEdit && (
        <Button size="sm" variant="outline-secondary" onClick={onEdit} title="Edit appointment">
          <Pencil size={16} className="me-1" />
          Edit
        </Button>
      )}
      {onCancel && (
        <Button size="sm" variant="outline-danger" onClick={onCancel} title="Cancel appointment">
          <Trash size={16} className="me-1" />
          Cancel
        </Button>
      )}
    </div>
  );

  return (
    <div
      className="card bg-light text-dark mb-3"
      role="region"
      aria-label={`Appointment ${patient ? 'for ' + patient : 'details'}`}
      tabIndex={0}
    >
      <div className="card-body">
        <div className="d-flex justify-content-between align-items-center mb-2">
          <h5 className="mb-0">
            {formattedDate} at {formattedTime}
            {patient && <> – {patient}</>}
          </h5>

          {status && (
            <OverlayTrigger placement="top" overlay={<Tooltip>{badgeTooltip}</Tooltip>}>
              <Badge bg={badgeColor}>{status}</Badge>
            </OverlayTrigger>
          )}
        </div>

        <p className="mb-2">{doctor}, {specialization}</p>

        {isLoading ? (
          <div className="d-flex align-items-center gap-2 mt-3">
            <Spinner size="sm" animation="border" />
            <span>Loading actions...</span>
          </div>
        ) : (
          renderButtons()
        )}
      </div>
    </div>
  );
};

AppointmentCard.propTypes = {
  appointment: PropTypes.shape({
    date: PropTypes.string,
    time: PropTypes.string,
    doctor: PropTypes.string,
    specialization: PropTypes.string,
    patient: PropTypes.string,
    status: PropTypes.oneOf(['CONFIRMED', 'PENDING', 'CANCELLED'])
  }).isRequired,
  onDetails: PropTypes.func,
  onCancel: PropTypes.func,
  onEdit: PropTypes.func,
  isLoading: PropTypes.bool
};

export default AppointmentCard;
