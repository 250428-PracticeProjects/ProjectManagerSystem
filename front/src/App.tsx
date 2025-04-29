import { BrowserRouter, Route, Routes } from 'react-router-dom'
import {HomePrivate, HomePublic, Login, UserRegister, UserUpdate, UserLayout} from './pages/index';
import './App.css'
import { AuthProvider } from './context/AuthContext';
import { UserView } from './pages/users/UserView';

import '@fontsource/roboto/300.css';
import '@fontsource/roboto/400.css';
import '@fontsource/roboto/500.css';
import '@fontsource/roboto/700.css';

function App() {

  return (
    <>
       <AuthProvider>
         <BrowserRouter>
           <Routes>
             <Route path='/' element={<HomePublic />} />
             <Route path='/login' element={<Login />} />
             <Route path='/register' element={<UserRegister />} />
 
             <Route path="private" element={<HomePrivate />}>
               <Route index element={<UserLayout />} /> {/* Default route for private section */}
               <Route path="profile" element={<UserView />} />
               <Route path="profile/update" element={<UserUpdate />} />
               <Route path="*" element={<h1>404 Not Found</h1>} />
             </Route>
           </Routes>
         </BrowserRouter>
       </AuthProvider>
    </>
  )
}

export default App
