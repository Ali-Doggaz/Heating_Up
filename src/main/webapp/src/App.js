import React,{useEffect} from 'react';
import Form from './pages/Form';
import { Route, Switch } from 'react-router';
import HomePage from './pages/HomePage';
import { useHistory } from 'react-router';
import Search from './pages/Search';

const routes = [
  { path:"/search",Component:Search, name:'search'},
  { path:"/home", Component:HomePage, name:"home" },
  { path:"/", Component:Form, name:'form'},
]

function App() {
  const history = useHistory();
  useEffect(()=>{
    if(localStorage.getItem('userData'))
      history.push("/home")
  })


  return (
    <div className="App">
      <div className="background"></div>
      <Switch>
      {
        routes.map(({path,Component,name})=>(
          <Route path={path} key={name}>
            <Component/>
          </Route>
        ))
      }
      </Switch>
    </div>
  );
}

export default App;