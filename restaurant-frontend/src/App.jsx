import { BrowserRouter, Routes, Route } from "react-router-dom";
import Login from "./pages/Login";
import Register from './pages/Register'
import Restaurant from "./pages/Restaurant";
import RestaurantFormPage from "./pages/RestaurantFormPage";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/restaurant" element={<Restaurant />} />
        <Route path="/restaurant/create" element={<RestaurantFormPage />} />
      </Routes>
    </BrowserRouter>
  );
}
