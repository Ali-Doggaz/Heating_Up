import React from 'react';
import Form from './pages/Form';
import { Route, Switch } from 'react-router';
import HomePage from './pages/HomePage';

const routes = [
  { path:"/home", Component:HomePage, name:"home" },
  { path:"/", Component:Form, name:'form'}
]

function App() {
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