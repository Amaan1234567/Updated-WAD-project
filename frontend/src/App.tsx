import { useState, useEffect } from "react";
import "./App.css";
import { createClient, type Session } from "@supabase/supabase-js"; // Import Session type
import axios from "axios";

// Use Vite's env syntax
const supabaseUrl = import.meta.env.VITE_SUPABASE_URL;
const supabaseKey = import.meta.env.VITE_SUPABASE_ANON_KEY;
const supabase = createClient(supabaseUrl, supabaseKey);

function App() {
  // 1. Explicitly tell TypeScript this state can be a Session OR null
  const [session, setSession] = useState<Session | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Get initial session
    supabase.auth.getSession().then(({ data: { session } }) => {
      setSession(session);
      setLoading(false); // Fixed the typo here!
    });

    // Listen for auth changes (login/logout)
    const {
      data: { subscription },
    } = supabase.auth.onAuthStateChange((_event, session) => {
      setSession(session);
    });

    return () => subscription.unsubscribe();
  }, []);

  const signInWithGitHub = async () => {
    const { error } = await supabase.auth.signInWithOAuth({
      provider: "github",
      options: {
        redirectTo: "localhost:3000"
      }
    });
    if (error) console.error("Error:", error.message);
  };

  const trigger_get = async () => {
    // TypeScript now knows session might have an access_token
    const token = session?.access_token;

    try {
      const response = await axios.get("http://localhost:8080/products/4", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      console.log("Data:", response.data);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  if (loading) return <div>Loading...</div>;

  return (
    <header>
      {!session ? (
        <button type="button" onClick={signInWithGitHub}>
          Login with Github
        </button>
      ) : (
        <div>
          {/* TypeScript now recognizes session.user */}
          <p>Logged in as: {session.user?.email}</p>
          <button
            type="button"
            className="trigger-button"
            onClick={trigger_get}
          >
            Trigger some products service api
          </button>
          <button onClick={() => supabase.auth.signOut()}>Logout</button>
        </div>
      )}
    </header>
  );
}

export default App;
