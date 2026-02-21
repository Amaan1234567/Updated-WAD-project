import './App.css'
import { SignedIn, SignedOut, SignInButton, SignUpButton, UserButton, useAuth} from '@clerk/clerk-react';
import axios from 'axios';



function App() {
  
  const { getToken } = useAuth();
  const trigger_get = async () => {
    const token = await getToken();
    const response = await axios.get('http://localhost:8080/products/1', {
      headers: {
        'Authorization': `Bearer ${token}`,

      }
    }).then(
      response => console.log(response.data)
    ).catch(error => console.error("error fetching data:", error()))
    console.log(response)
  }

  return (
    <header>
      {/* Show the sign-in and sign-up buttons when the user is signed out */}
      <SignedOut>
        <SignInButton />
        <SignUpButton />
        <button type="button" className='trigger-button' onClick={trigger_get}>Trigger some products service api</button>
      </SignedOut>
      {/* Show the user button when the user is signed in */}
      <SignedIn>
        <div className='container'>
          <UserButton />
          <button type="button" className='trigger-button' onClick={trigger_get}>Trigger some products service api</button>
        </div>
      </SignedIn>
    </header>
  );
}

export default App