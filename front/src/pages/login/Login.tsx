import React, { useState } from 'react';
import { TextField, Button, Grid, Typography, CardContent, Card, Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import Loading from '../../components/Loading';
import BasicAlerts from '../../components/BasicAlerts';
import { useAuth } from '../../hooks/useAuth';
import { IFormLogin } from '../../interfaces/IFormLogin';


export const Login = () => {
  const navigate = useNavigate();
  const { data, error, loading, login } = useAuth();
  const [formData, setFormData] = useState<IFormLogin>({
    email: '',
    password: '',
  });

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    setFormData((prevData) => ({
      ...prevData,
      [name]: value,
    }));
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();

    try {
      await login(formData);
      // if (data) {
      //   navigate('/private');
      // }
    } catch (error) {
      console.error('Login failed:', error);
    }
  };

  return (
    <>
      <Box
        display="flex"
        justifyContent="center"
        alignItems="center"
        minHeight="100vh"
      >
        <Card sx={{ maxWidth: 600 }} variant="outlined">
          <CardContent>
            <Typography variant="h4" component="h2" gutterBottom align="center">
              Log In
            </Typography>

            {loading && <Loading />}

            {error && <BasicAlerts severity={"error"} message={error} />}

            <form onSubmit={handleSubmit} style={{ marginTop: '16px' }}>
              <Grid container spacing={2} direction="row" columns={16}>
                <Grid size={8}>
                  <TextField
                    label="Email"
                    type="email"
                    name="email"
                    fullWidth
                    value={formData.email}
                    onChange={handleChange}
                    required
                  />
                </Grid>
                <Grid size={8}>
                  <TextField
                    label="Contraseña"
                    type="password"
                    name="password"
                    fullWidth
                    value={formData.password}
                    onChange={handleChange}
                    required
                  />
                </Grid>
                <Grid size={8} alignItems={"right"} justifyContent="right" display="flex" sx={{ mt: 2 }} spacing={2}>
                  <Button type="submit" variant="contained" color="primary">
                    Login
                  </Button>
                </Grid>
                <Grid size={8} alignItems={"left"} justifyContent="left" display="flex" sx={{ mt: 2 }} spacing={2}>
                  <Button type="submit" variant="contained" color="secondary" onClick={() => navigate('/')}>
                    Cancel
                  </Button>
                </Grid>
              </Grid>
            </form>
          </CardContent>
        </Card>
      </Box>

    </>
  );
};
