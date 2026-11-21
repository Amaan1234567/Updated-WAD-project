import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { AuthProvider, type AuthProviderProps } from "react-oidc-context";
import "./index.css";
import App from "./App.tsx";

const oidcConfig: AuthProviderProps = {
  authority: "http://localhost:8090/realms/plantio",
  client_id: "public-auth",
  redirect_uri: window.location.origin, // http://localhost:3000
  post_logout_redirect_uri: window.location.origin,
  scope: "openid profile email",
  // Clean up ?code=...&state=... from the URL after login redirect
  onSigninCallback: () => {
    window.history.replaceState({}, document.title, window.location.pathname);
  },
};

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <AuthProvider {...oidcConfig}>
      <App />
    </AuthProvider>
  </StrictMode>,
);
