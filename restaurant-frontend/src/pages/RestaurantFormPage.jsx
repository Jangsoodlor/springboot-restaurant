import { useState } from "react";
import api from "../api/axios";

export default function RestaurantFormPage() {
  const [form, setForm] = useState({ name: "", rating: "", location: "" });
  const [submitted, setSubmitted] = useState(null);

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  function handleSubmit(e) {
    e.preventDefault();
    setSubmitted(form);


    async function submit() {
      try {
        await api.post('/api/restaurants', form);
      } catch (error) {
        console.error(error);
      }
      setForm({ name: "", rating: "", location: "" });
    }

    submit();
  }
  return (
    <div className="min-h-screen flex items-center justify-center p-10 bg-gray-100">
      <div className="bg-white p-8 rounded-2xl shadow-xl w-full max-w-md">
        <h1 className="text-2xl font-bold mb-6">Add Restaurant</h1>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block mb-1 font-medium">Name</label>
            <input
              name="name"
              value={form.name}
              onChange={handleChange}
              className="w-full p-2 border rounded-lg"
              required
            />
          </div>

          <div>
            <label className="block mb-1 font-medium">Rating</label>
            <input
              name="rating"
              value={form.rating}
              onChange={handleChange}
              className="w-full p-2 border rounded-lg"
              required
            />
          </div>

          <div>
            <label className="block mb-1 font-medium">Location</label>
            <input
              name="location"
              value={form.location}
              onChange={handleChange}
              className="w-full p-2 border rounded-lg"
              required
            />
          </div>

          <button
            type="submit"
            className="w-full p-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
          >
            Submit
          </button>
        </form>

      </div>
    </div>
  );
}
