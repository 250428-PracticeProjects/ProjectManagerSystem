
import Stack from '@mui/material/Stack';
import CircularProgress from '@mui/material/CircularProgress';

export default function Loading() {
  return (
    <>
      <div className="overlay">
        <Stack sx={{ color: 'grey.500' }} spacing={2} direction="row" justifyContent="center" alignItems="center">
          <CircularProgress color="secondary" />
        </Stack>
      </div>
    </>
  );
}