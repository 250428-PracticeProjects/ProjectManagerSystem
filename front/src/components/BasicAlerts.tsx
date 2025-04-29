import Alert, { AlertColor } from '@mui/material/Alert';
import Stack from '@mui/material/Stack';

export default function BasicAlerts({ severity, message }: { severity: AlertColor, message: string }) {
                {/* <Alert severity="success">This is a success Alert.</Alert>
      <Alert severity="info">This is an info Alert.</Alert>
      <Alert severity="warning">This is a warning Alert.</Alert>
      <Alert severity="error">This is an error Alert.</Alert> */}
  return (
    <Stack sx={{ width: '100%' }} spacing={2}>
      <Alert variant="outlined" severity={severity}>{message} </Alert>
    </Stack>
  );
}