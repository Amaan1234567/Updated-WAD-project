import "./App.css";
import { useAuth } from "react-oidc-context";
import axios from "axios";

function App() {
  const auth = useAuth();

  const triggerGet = async () => {
    const token = auth.user?.access_token;

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

  if (auth.isLoading) return <div>Loading...</div>;
  if (auth.error) return <div>Auth error: {auth.error.message}</div>;

  return (
    <header>
      {!auth.isAuthenticated ? (
        <button type="button" onClick={() => auth.signinRedirect()}>
          Login with Keycloak
        </button>
      ) : (
        <div>
          <p>
            Logged in as:{" "}
            {auth.user?.profile.email ?? auth.user?.profile.preferred_username}
          </p>
          <button type="button" className="trigger-button" onClick={triggerGet}>
            Trigger some products service api
          </button>
          <button type="button" onClick={() => auth.signoutRedirect()}>
            Logout
          </button>
        </div>
      )}
    </header>
  );
}

export default App;
