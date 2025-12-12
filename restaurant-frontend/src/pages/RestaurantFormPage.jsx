import { useState } from "react";
import { useNavigate } from 'react-router-dom'
import api from "../api/axios";

export default function RestaurantFormPage() {
  const [form, setForm] = useState({ name: "", rating: "", location: "" });
  const [submitted, setSubmitted] = useState(null);
  const navigate = useNavigate()


  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  function handleSubmit(e) {
    e.preventDefault();
    setSubmitted(form);


    async function submit() {
      try {
        await api.post('/api/restaurants', form);
        alert("Restaurant Created 🎉🎉🎉")
      } catch (error) {
        alert(error)
        console.error(error);
      }
      setForm({ name: "", rating: "", location: "" });
    }

    submit();
  }
  return (
    <div style={{ padding: '2rem' }}>
      <div>
        <h1 style={{ marginBottom: '2rem' }}>
          Create a new Restaurant
        </h1>

        <form onSubmit={handleSubmit}>
          <div style={{ paddingBottom: '1rem' }}>
            <label style={{ paddingRight: '1rem' }}>Name</label>
            <input
              name="name"
              value={form.name}
              onChange={handleChange}
              required
            />
          </div>

          <div style={{ paddingBottom: '1rem' }}>
            <label style={{ paddingRight: '1rem' }}>Rating</label>
            <input
              name="rating"
              value={form.rating}
              onChange={handleChange}
              required
            />
          </div>

          <div style={{ paddingBottom: '1rem' }}>
            <label style={{ paddingRight: '1rem' }}>Location</label>
            <input
              name="location"
              value={form.location}
              onChange={handleChange}
              required
            />
          </div>

          <button
            style={{ marginTop: '1rem' }}
            type="submit"
          >
            Submit
          </button>
        </form>
        <button
          style={{ marginTop: '1rem' }}
          onClick={() => { navigate('/restaurant') }}
        >
          Restaurant List
        </button>

      </div>
    </div>
  );
}
