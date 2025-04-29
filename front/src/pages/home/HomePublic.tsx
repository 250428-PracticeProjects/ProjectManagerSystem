import { useNavigate } from 'react-router-dom';

export const HomePublic = () => {

  const navigate = useNavigate();
  return (
    <>
        <div>HomePublic</div>
        <button onClick={() => navigate('/login')}>Login</button>
        <button onClick={() => navigate('/register')}>Register</button>
    </>
  )
}
